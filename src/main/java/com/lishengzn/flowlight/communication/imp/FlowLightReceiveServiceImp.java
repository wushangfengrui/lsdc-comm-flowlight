package com.lishengzn.flowlight.communication.imp;

import com.lishengzn.comm.entity.device.ServerDevice;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.communication.FlowLightBotton;
import com.lishengzn.flowlight.communication.FlowLightReceiveService;
import com.lishengzn.flowlight.communication.TCPClientFactory;
import com.lishengzn.flowlight.entry.FlowLightControlCabinet;
import com.lishengzn.flowlight.packet.FlowPacketModel;
import com.lishengzn.flowlight.packet.FlowPacketModelFactory;
import com.lishengzn.flowlight.util.FlowSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FlowLightReceiveServiceImp implements FlowLightReceiveService {

    private static final Logger LOG = LoggerFactory.getLogger(FlowLightReceiveServiceImp.class);
    private InputStream in;
    private OutputStream out;
    private BufferedInputStream bis;
    private volatile boolean initialized;
    private FlowLightControlCabinet controller;
    private Socket socket;
    private FlowLightBotton fLowLightBotton;

    public FlowLightReceiveServiceImp(FlowLightBotton fLowLightBotton){
        this.fLowLightBotton = fLowLightBotton;
    }


    @Override
    public void initialize() {
        if(!isInitialized()){
            try {
                this.in=socket.getInputStream();
                this.bis = new BufferedInputStream(in);
                this.out= socket.getOutputStream();
            } catch (IOException e) {
                throw new SimpleException("与车辆连接异常，请重新连接！");
            }
            this.initialized=true;
            runActualTask();
        }
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public void terminate() {
        if(isInitialized()){
            this.initialized=false;
        }
    }

    protected void runActualTask() {
        new Thread(() -> handlReceivedMsg(), "receiveThread" + System.currentTimeMillis()).start();
    }

    @Override
    public void setDevice(ServerDevice device) {
        this.controller = (FlowLightControlCabinet) device;
    }

    @Override
    public ServerDevice getDevice() {
        return this.controller;
    }

    @Override
    public void setMaster(Socket socket) {
        this.socket=socket;
    }

    @Override
    public Socket getMaster() {
        return this.socket;
    }

    private void handlReceivedMsg() {
        byte[] packetModel_byte;
        FlowPacketModel packetModel=null;
        try {
            LOG.info("========vehilce client receiving-- start");
            while(isInitialized()){
                if((packetModel_byte= FlowSocketUtil.readNextPacketData(bis))!=null){
                    packetModel = FlowSocketUtil.bytesTransforModel(packetModel_byte);
                    handlePacketModel(packetModel);
                }
            }
            LOG.info("========vehilce client receiving-- end");
        }catch (IOException e){
            LOG.error("========vehilce client receiving-- end");
            if(isInitialized()){
                LOG.error("与小车[{}]通讯异常", controller.getDeviceId(),e);
                TCPClientFactory.reconnect(controller.getIp());
                throw new SimpleException("与小车["+controller.getDeviceId()+"]通讯异常");
            }
        }
    }

    private void handlePacketModel(FlowPacketModel packetModel) {

        try {
            String data = new String(packetModel.getData());
            if(data.startsWith("t")){
                /**
                 * confirm按下后发送优先指令是  "t[address][status][address][status]"
                 * address:4个字节
                 * status:2个字节 00：正常  01：标签异常  10：缺货
                 *
                 * 优先指令应答 "O",异常应答"N"
                 * 指令应答"o",异常应答"n"
                 */
                synchronized (controller.getControllerSyncObjCollection().getPriorityOrderSyncObj()) {
                    if ((data.length() - 1) % 6 != 0) {
                        FlowPacketModel flowPacketModel = FlowPacketModelFactory.getPriorityModelExceptionResponse(packetModel.getSeriaNo());
                        FlowSocketUtil.sendPacketData(out, flowPacketModel);
                        LOG.error("接收到以优先指令异常，data:{}", data);
                    }else{
                        FlowPacketModel flowPacketModel = FlowPacketModelFactory.getPriorityModelResponse(packetModel.getSeriaNo());
                        FlowSocketUtil.sendPacketData(out, flowPacketModel);
                        LOG.debug("接收到以优先指令，data:{}", data);
                    }
                }
                for (int i = 0; i < (data.length() - 1) / 6; i++) {
                    String address = data.substring(6 * i + 1, 6 * i + 5);
                    String status = data.substring(6 * i + 5, 6 * i + 7);
                    if (!"00".equals(status)) {
                        LOG.error("接收指令应答异常，serial:{},data:{}",packetModel.getSeriaNo(),data);
                        return;
                    }
                    if (address.startsWith("91")) {
                        LOG.debug("接收到条形码扫描枪或RS-232C接口:serial:{},address:{}",packetModel.getSeriaNo(), address);
                    }else{
                        LOG.debug("接收到电子标签：{},serial:{},优先指令", address,packetModel.getSeriaNo());
                    }
                    new Thread(() ->  fLowLightBotton.tigger(address), "FlowLightTigger" + System.currentTimeMillis()).start();
                }
            }else if(data.startsWith("o")){
                LOG.debug("收到指令应答数据，serial:{}", packetModel.getSeriaNo());
                notifyRequest(packetModel,controller.getControllerSyncObjCollection().getInstructionSyncObj());
            }else if(data.startsWith("n")){
                LOG.info("收到不正常指令应答数据，serial:{}，data:{}", packetModel.getSeriaNo(), data);
            }
        }catch (Exception e){
            // ExceptionHandler.INSTANCE.accept(new SimpleException("车辆["+vehicle.getDeviceId()+"]通讯异常"));
        }
    }
}


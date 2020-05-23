package com.lishengzn.flowlight.communication.imp;

import com.lishengzn.comm.entity.device.ServerDevice;
import com.lishengzn.comm.entity.sync.RequestSyncObj;
import com.lishengzn.flowlight.communication.FlowLightSenderService;
import com.lishengzn.flowlight.entry.FlowLightControlCabinet;
import com.lishengzn.flowlight.instruct.init.InitZ;
import com.lishengzn.flowlight.instruct.signalLight.SignalLightM;
import com.lishengzn.flowlight.instruct.work.StartOperation;
import com.lishengzn.flowlight.packet.FlowPacketModel;
import com.lishengzn.flowlight.packet.FlowPacketModelFactory;
import com.lishengzn.flowlight.packet.FlowSeriaNo;
import com.lishengzn.flowlight.util.FlowSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class FlowLightSenderServiceImp implements FlowLightSenderService {
    private static Logger LOG = LoggerFactory.getLogger(FlowLightSenderServiceImp.class);
    private Socket socket;
    private volatile FlowLightControlCabinet controller;

    @Override
    public void setDevice(ServerDevice device) {
        this.controller = (FlowLightControlCabinet)device;
    }

    @Override
    public ServerDevice getDevice() {
        return controller;
    }

    @Override
    public void setMaster(Socket master) {
        this.socket=master;
    }


    @Override
    public Socket getMaster() {
        return socket;
    }

    @Override
    public void initialize() {
        FlowSeriaNo.setPriorityOrderSyncObj(controller.getControllerSyncObjCollection().getPriorityOrderSyncObj());
    }

    /**
     * 变更灯颜色，闪烁等状态
     * @param address 地址
     * @param modeArray 状态 ModeArray
     */
    public void sendLightColor(String address,byte[] modeArray){
        String[] addresses = new String[1];
        addresses[0] = address;
        RequestSyncObj syncObj = controller.getControllerSyncObjCollection().getInstructionSyncObj();
        FlowPacketModel packetModel = new FlowPacketModel(new SignalLightM(addresses,modeArray).getBytes());

        synchronized (syncObj) {
            LOG.info("向小车[{}]发送变更颜色命令,序号：{}!",controller.getDeviceId(), packetModel.getSeriaNo());
            FlowPacketModel responsePacketModel = FlowSocketUtil.sendRequest(packetModel,syncObj,this);
            FlowPacketModelFactory.checkResponse(responsePacketModel.getData());
        }
    }

    /**
     * 对灯操作前必须发送
     * 在灯被按过后需要再次操作前也要发送
     * @param address
     */
    public void sendStartOperation(String address){
        RequestSyncObj syncObj = controller.getControllerSyncObjCollection().getInstructionSyncObj();
        FlowPacketModel packetModel = new FlowPacketModel(new StartOperation(address).getBytes());

        synchronized (syncObj) {
            LOG.info("向小车[{}]发送start operation命令,序号：{}!",controller.getDeviceId(), packetModel.getSeriaNo());
            FlowPacketModel responsePacketModel = FlowSocketUtil.sendRequest(packetModel,syncObj,this);
            FlowPacketModelFactory.checkResponse(responsePacketModel.getData());
        }
    }

    /**
     * 程序启动时需要执行命令
     */
    public void sendZMode(){
        RequestSyncObj syncObj = controller.getControllerSyncObjCollection().getInstructionSyncObj();
        FlowPacketModel packetModel = new FlowPacketModel(new InitZ().getBytes());

        synchronized (syncObj) {
            LOG.info("向小车[{}]发送所有指示灯初始化命令,序号：{}!",controller.getDeviceId(), packetModel.getSeriaNo());
            FlowPacketModel responsePacketModel = FlowSocketUtil.sendRequest(packetModel,syncObj,this);
            FlowPacketModelFactory.checkResponse(responsePacketModel.getData());
        }
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void terminate() {

    }
}

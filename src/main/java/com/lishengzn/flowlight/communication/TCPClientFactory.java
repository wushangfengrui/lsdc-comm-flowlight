package com.lishengzn.flowlight.communication;

import com.lishengzn.comm.communication.service.MessageReceiverService;
import com.lishengzn.comm.communication.service.MessageSenderService;
import com.lishengzn.comm.entity.device.ServerDevice;
import com.lishengzn.comm.pool.ObjectPool;
import com.lishengzn.flowlight.communication.imp.FlowLightBottonImpl;
import com.lishengzn.flowlight.communication.imp.FlowLightReceiveServiceImp;
import com.lishengzn.flowlight.communication.imp.FlowLightSenderServiceImp;
import com.lishengzn.flowlight.entry.FlowLightControlCabinet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class TCPClientFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TCPClientFactory.class);

    public static FlowTCPClient createFlowLightTCPClient(FlowLightControlCabinet flowLightController) throws IOException {
        destroyTCPClient(flowLightController.getIp());
        FlowTCPClient<FlowLightControlCabinet, MessageSenderService<Socket>, MessageReceiverService<Socket>> client =
                new FlowTCPClient<>(flowLightController, FlowTCPClient.ClientType.INDUSTRIAL_COMPUTER);
        client.initialize(new FlowLightSenderServiceImp(), new FlowLightReceiveServiceImp(new FlowLightBottonImpl()));
        return client;
    }

    private static void closeLastClient(ServerDevice device){
        FlowTCPClient lastClient=null;
        if((lastClient = ObjectPool.getPoolObjOrNull(device.getIp(), FlowTCPClient.class))!=null){
            lastClient.close();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void reconnect(String ip){
        FlowTCPClient client=ObjectPool.getPoolObjOrNull(ip, FlowTCPClient.class);
        if(client!=null){
            LOG.info("重新连接TCPClient，ip:{}",ip);
            client.reconnect();
            System.gc();
        }
    }
    public static void destroyTCPClient(String ip){
        FlowTCPClient client=ObjectPool.getPoolObjOrNull(ip, FlowTCPClient.class);
        if(client!=null){
            LOG.info("销毁TCPClient，ip:{}",ip);
            client.close();
            System.gc();
        }
    }
}

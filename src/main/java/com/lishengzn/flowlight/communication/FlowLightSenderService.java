package com.lishengzn.flowlight.communication;

import com.lishengzn.comm.communication.service.MessageSenderService;

import java.net.Socket;

public interface FlowLightSenderService extends MessageSenderService<Socket> {
    void sendLightColor(String address,byte[] modeArray);
    void sendStartOperation(String address);
    void sendZMode();
}

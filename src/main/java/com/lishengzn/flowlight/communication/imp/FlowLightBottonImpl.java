package com.lishengzn.flowlight.communication.imp;

import com.lishengzn.comm.pool.ObjectPool;
import com.lishengzn.flowlight.communication.FlowLightBotton;
import com.lishengzn.flowlight.communication.FlowTCPClient;
import com.lishengzn.flowlight.entry.FlowLightControlCabinet;
import com.lishengzn.flowlight.instruct.ModeArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowLightBottonImpl implements FlowLightBotton {
    private static final Logger LOG = LoggerFactory.getLogger(FlowLightBottonImpl.class);
    @Override
    public void tigger(String address) {
        LOG.info("灯按钮被触发{}",address);
        FlowTCPClient<FlowLightControlCabinet, FlowLightSenderServiceImp, FlowLightReceiveServiceImp> tcpClient = ObjectPool.getPoolObj("192.168.1.254",FlowTCPClient.class);
        tcpClient.getMessageSenderService().sendStartOperation("1002");
        tcpClient.getMessageSenderService().sendLightColor("1002", ModeArray.Normally_Audio_red);
    }

}

package com.lishengzn.flowlight.communication;

import com.lishengzn.comm.communication.service.MessageReceiverService;
import com.lishengzn.comm.entity.sync.RequestSyncObj;
import com.lishengzn.flowlight.constants.FlowConstants;
import com.lishengzn.flowlight.packet.FlowPacketModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

public interface FlowLightReceiveService extends MessageReceiverService<Socket> {

    Logger LOG = LoggerFactory.getLogger(FlowLightReceiveService.class);
    default   void  notifyRequest(FlowPacketModel packetModel, RequestSyncObj syncObj) throws InterruptedException {
        if(syncObj.getPacketSerialNo()==packetModel.getSeriaNo()){
            boolean b =syncObj.offer(packetModel, FlowConstants.MAX_WAIT_TIME, TimeUnit.SECONDS);
            if(!b){
                LOG.debug("offer faild,seriano:{}",packetModel.getSeriaNo());
            }
        }else{
            LOG.debug("seriano not matched,sendSeriano:{},receivedSeriano:{}",syncObj.getPacketSerialNo(),packetModel.getSeriaNo());
        }
    }
}

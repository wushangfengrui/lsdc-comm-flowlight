package com.lishengzn.flowlight.entry;

import com.lishengzn.comm.entity.sync.RequestSyncObj;
import com.lishengzn.comm.entity.sync.RequestSyncObjCollection;
import com.lishengzn.flowlight.packet.FlowPacketModel;

import java.io.Serializable;

public class ControllerSyncObjCollection extends RequestSyncObjCollection implements Serializable {

    /** 指令锁 */
    private RequestSyncObj<FlowPacketModel> instructionSyncObj = new RequestSyncObj();

    /** 优先指令锁 */
    private RequestSyncObj<FlowPacketModel> priorityOrderSyncObj = new RequestSyncObj();

    public RequestSyncObj<FlowPacketModel> getInstructionSyncObj() {
        return instructionSyncObj;
    }

    public RequestSyncObj<FlowPacketModel> getPriorityOrderSyncObj() {
        return priorityOrderSyncObj;
    }
}

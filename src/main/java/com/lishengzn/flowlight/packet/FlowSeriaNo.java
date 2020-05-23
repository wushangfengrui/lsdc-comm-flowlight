package com.lishengzn.flowlight.packet;

import com.lishengzn.comm.entity.sync.RequestSyncObj;

/** 序号范围在000~999 之间*/
public class FlowSeriaNo {

    private volatile static RequestSyncObj<FlowPacketModel> priorityOrderSyncObj;

    private static int serialNo=-1;

    public static void setPriorityOrderSyncObj(RequestSyncObj<FlowPacketModel> syncObj){
        priorityOrderSyncObj = syncObj;
    }

    public  static int getNextSerialNo() {
        synchronized (priorityOrderSyncObj){
            serialNo++;
            if(serialNo == 1000){
                serialNo = 0;
            }
        }
        return serialNo;
    }

    public static void init(){
        serialNo = 0;
    }

    public static int getCurrentSerialNo(){
        return serialNo;
    }
}

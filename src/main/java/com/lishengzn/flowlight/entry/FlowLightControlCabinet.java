package com.lishengzn.flowlight.entry;

import com.lishengzn.comm.entity.device.ServerDevice;

public class FlowLightControlCabinet extends ServerDevice {

    /** 锁对象的整合类，只提供getter */
    private ControllerSyncObjCollection controllerSyncObjCollection = new ControllerSyncObjCollection();

    public ControllerSyncObjCollection getControllerSyncObjCollection() {
        return controllerSyncObjCollection;
    }

    @Override
    public String getPoolKey() {
        return getTcpClientKey(ip,port);
    }


    public static String getTcpClientKey(String ip,int port){
        return ip+":"+port;
    }
}

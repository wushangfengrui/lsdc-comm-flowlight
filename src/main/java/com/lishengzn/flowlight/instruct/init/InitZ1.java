package com.lishengzn.flowlight.instruct.init;

import com.lishengzn.flowlight.instruct.Instruct;

/**
 * Z(初始化设置)
 * Z
 * 初始化每一设备的动作模式
 */
public class InitZ1 implements Instruct {
    public byte[] getBytes(){
        return "Z1".getBytes();
    }
}

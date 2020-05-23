package com.lishengzn.flowlight.instruct.signalLight;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.constants.FlowConstants;
import com.lishengzn.flowlight.instruct.Instruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * T[1][2]
 * [1]:address
 * [2]:button
 */
public class SignalLightT  implements Instruct {

    public static final byte BUTTON_ON  = (byte)1;
    public static final byte BUTTON_OFF = (byte)0;
    /**
     * 允许设定范围8001~8099
     */
    private String address;
    /**
     * ON/OFF 指定
     */
    private byte sw;

    public SignalLightT(String address, byte sw) {
        this.address = address;
        this.sw = sw;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getSw() {
        return sw;
    }

    public void setSw(byte sw) {
        this.sw = sw;
    }

    public byte[] getBytes(){
        Objects.requireNonNull(this.address, "信号灯指令T中信号灯地址不能为空");
        Objects.requireNonNull(this.sw, "信号灯指令T中ON/OFF 指定不能为空");
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("T".getBytes()));
        byteList.addAll(Bytes.asList(address.getBytes()));
        byteList.add(sw);
        if(byteList.size() > FlowConstants.MAX_DATA_LENGTH){
            throw new SimpleException("命令超过最大长度");
        }
        return Bytes.toArray(byteList);
    }


}

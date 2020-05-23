package com.lishengzn.flowlight.instruct.signalLight;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.constants.FlowConstants;
import com.lishengzn.flowlight.instruct.Instruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * M[1][2]00[3]0
 * [1]:address
 * [2]:exprCondition
 * [3]:fickerSpeed
 */
public class SignalLightM implements Instruct {

    public static final StringBuffer EXPRCONDITION_LIGHTSOUT = new StringBuffer("00");
    public static final StringBuffer EXPRCONDITION_LIGHTSON  = new StringBuffer("01");
    public static final StringBuffer EXPRCONDITION_FICKER    = new StringBuffer("10");

    public static final StringBuffer FICKERSPEED_STANDARD    = new StringBuffer("0");
    public static final StringBuffer FICKERSPEED_HIGH_SPEED  = new StringBuffer("1");
    /**
     * 允许设定范围8001~8099
     */
    private String[] addresses;

    public SignalLightM(String[] addresses, byte[] modeArray) {
        this.addresses = addresses;
        this.modeArray = modeArray;
    }

    private byte[] modeArray;

    public String[] getAddresses() {
        return addresses;
    }


    public void setAddresses(String[] addresses) {
        this.addresses = addresses;
    }

    public byte[] getModeArray() {
        return modeArray;
    }

    public void setModeArray(byte[] modeArray) {
        this.modeArray = modeArray;
    }

    public byte[] getBytes(){
        Objects.requireNonNull(this.addresses, "信号灯指令M中信号灯地址不能为空");
        Objects.requireNonNull(this.modeArray, "信号灯指令M中表示指令数据不能为空");
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("M".getBytes()));
        byteList.addAll(Bytes.asList(modeArray));
        for(String address:addresses){
            if(address.length() != 4){
                throw new SimpleException("address格式不正确,address:"+address);
            }
            byteList.addAll(Bytes.asList(address.getBytes()));
        }
        if(byteList.size() > FlowConstants.MAX_DATA_LENGTH){
            throw new SimpleException("命令超过最大长度");
        }
        return Bytes.toArray(byteList);
    }


}

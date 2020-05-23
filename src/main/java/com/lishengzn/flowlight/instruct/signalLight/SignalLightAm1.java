package com.lishengzn.flowlight.instruct.signalLight;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.instruct.Instruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 可以反复设定的信号灯
 * Am1[1][31h][2][3]
 * [1]:address
 * [2]:signal
 * [3]:buzzing
 */
public class SignalLightAm1 implements Instruct {
    public static  final byte OFF        = 0x11;
    public static  final byte ON         = 0x12;
    /** 闪烁 */
    public static  final byte FLICKER    = 0x13;
    /** 高速 */
    public static  final byte HIGH_SPEED = 0x14;

    private int maxLength = 64;

    /**
     * 信号灯地址，4个字节，范围8001~8099
     */
    private String address;

    /**
     * 信号灯
     */
    private byte signal;

    /**
     * 蜂鸣器
     */
    private byte buzzing;

    public SignalLightAm1() {
    }

    public SignalLightAm1(String address, byte signal, byte buzzing) {
        this.address = address;
        this.signal = signal;
        this.buzzing = buzzing;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getSignal() {
        return signal;
    }

    public void setSignal(byte signal) {
        this.signal = signal;
    }

    public byte getBuzzing() {
        return buzzing;
    }

    public void setBuzzing(byte buzzing) {
        this.buzzing = buzzing;
    }

    public byte[] getBytes(){
        Objects.requireNonNull(this.address, "信号灯指令Ami中电子标签地址不能为空");
        Objects.requireNonNull(this.signal, "信号灯指令Ami中信号灯不能为空");
        Objects.requireNonNull(this.buzzing, "信号灯指令Ami中蜂鸣器不能为空");
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("Am1".getBytes()));
        byteList.addAll(Bytes.asList(address.getBytes()));
        byteList.add((byte)0x31);
        byteList.add(signal);
        byteList.add(buzzing);
        if(byteList.size() > maxLength){
            throw new SimpleException("命令超过最大长度");
        }
        return Bytes.toArray(byteList);
    }


}

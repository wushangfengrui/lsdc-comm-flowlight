package com.lishengzn.flowlight.instruct.motionInstruc;

import com.google.common.primitives.Bytes;
import com.lishengzn.flowlight.instruct.Instruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 可重复设置模式 可指定Fn键按下后表示数据 分区域作业
 * PP505[1][2][3][4][5][6]
 * [1]:'00' 不需要指定[6]
 * [2]:Block No 区域号码 允许设定范围00~99
 * [3]:Mode Array 模式组合，可省略
 * [4]:address 标签地址  0001~7999
 * [5]:display Data 表示数据
 * [6]: Fn键按下后表示数据
 */
public class MotionInstrucPP5 implements Instruct {
    private String fnsw = "00";
    /**
     * 两个字节
     */
    private String blockNo;
    private byte[] modeArray;
    /**
     * 0000~7999
     */
    private String address;
    private String displayData = "\t\t\t\t\t";
    private String afterDisplayData = "\t\t\t\t\t";

    public MotionInstrucPP5(String blockNo, byte[] modeArray, String address) {
        this.blockNo = blockNo;
        this.modeArray = modeArray;
        this.address = address;
    }

    public String getFnsw() {
        return fnsw;
    }

    public void setFnsw(String fnsw) {
        this.fnsw = fnsw;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public byte[] getModeArray() {
        return modeArray;
    }

    public void setModeArray(byte[] modeArray) {
        this.modeArray = modeArray;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayData() {
        return displayData;
    }

    public void setDisplayData(String displayData) {
        this.displayData = displayData;
    }

    public String getAfterDisplayData() {
        return afterDisplayData;
    }

    public void setAfterDisplayData(String afterDisplayData) {
        this.afterDisplayData = afterDisplayData;
    }

    @Override
    public byte[] getBytes() {
        Objects.requireNonNull(this.fnsw, "信号灯指令PP5中电子标签地址不能为空");
        Objects.requireNonNull(this.blockNo, "信号灯指令PP5中信号灯不能为空");
        Objects.requireNonNull(this.modeArray, "信号灯指令PP5中蜂鸣器不能为空");
        Objects.requireNonNull(this.address, "信号灯指令PP5中电子标签地址不能为空");
        Objects.requireNonNull(this.displayData, "信号灯指令PP5中信号灯不能为空");

        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("PP505".getBytes()));
        byteList.addAll(Bytes.asList(fnsw.getBytes()));
        byteList.addAll(Bytes.asList(blockNo.getBytes()));
        byteList.addAll(Bytes.asList(modeArray));
        byteList.addAll(Bytes.asList(address.getBytes()));
        byteList.addAll(Bytes.asList(displayData.getBytes()));
        if(!"00".equals(fnsw)){
            byteList.addAll(Bytes.asList(afterDisplayData.getBytes()));
        }
        return Bytes.toArray(byteList);
    }
}

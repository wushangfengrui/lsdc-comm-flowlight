package com.lishengzn.flowlight.packet;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.constants.FlowConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowPacketModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Byte syncHead= FlowConstants.LED_SYNC_STX;
    /** 序号范围在000~999 之间*/
    private int seriaNo=-1;
    /** 序号范围在0001~1015 之间*/
    private int dataLength;

    private byte[] data;

    private Byte syncEnd= FlowConstants.LED_SYNC_ETX;

    public FlowPacketModel() {
    }

    public FlowPacketModel(byte[] data) {
        this.seriaNo = FlowSeriaNo.getNextSerialNo();
        this.data = data;
        this.dataLength = data.length;
        if(dataLength > FlowConstants.MAX_DATA_LENGTH || dataLength < 1){
            throw new SimpleException("数据长度超出LED通讯长度范围，data:"+ Arrays.toString(data));
        }
    }

    public FlowPacketModel(int seriaNo, byte[] data) {
        this.seriaNo = seriaNo;
        this.dataLength = data.length;
        this.data = data;
    }

    public FlowPacketModel(int seriaNo, int dataLength, byte[] data) {
        this.seriaNo = seriaNo;
        this.dataLength = dataLength;
        this.data = data;
    }

    public int getSeriaNo() {
        return seriaNo;
    }

    public void setSeriaNo(int seriaNo) {
        this.seriaNo = seriaNo;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] toBytes(){
        List<Byte> byteList=new ArrayList<Byte>();
        byteList.add(syncHead);
        byteList.addAll(Bytes.asList(String.format("%03d", seriaNo).getBytes()));
        byteList.addAll(Bytes.asList(String.format("%04d", dataLength).getBytes()));
        byteList.addAll(Bytes.asList(data));
        byteList.add(syncEnd);
        return Bytes.toArray(byteList);
    }
}

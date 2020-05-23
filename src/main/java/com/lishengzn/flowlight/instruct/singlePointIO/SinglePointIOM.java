package com.lishengzn.flowlight.instruct.singlePointIO;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.constants.FlowConstants;
import com.lishengzn.flowlight.instruct.Instruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * M[1][04h][30h][30h][30h][40h]
 * [1]:address
 */
public class SinglePointIOM implements Instruct {
    /**
     * 1~7999
     */
    private String address;


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public byte[] getBytes(){
        Objects.requireNonNull(this.address, "信号灯指令M中信号灯地址不能为空");
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("M".getBytes()));
        byteList.addAll(Bytes.asList(address.getBytes()));
        byteList.add((byte)0x04);
        byteList.add((byte)0x30);
        byteList.add((byte)0x30);
        byteList.add((byte)0x40);
        byteList.add((byte)0x30);
        if(byteList.size() > FlowConstants.MAX_DATA_LENGTH){
            throw new SimpleException("命令超过最大长度");
        }
        return Bytes.toArray(byteList);
    }


}

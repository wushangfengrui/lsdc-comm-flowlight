package com.lishengzn.flowlight.instruct.work;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.constants.FlowConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StartOperation {
    /**
     * 允许设定范围8001~8099
     */
    private String address;

    public StartOperation(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getBytes(){
        Objects.requireNonNull(this.address, "信号灯指令StartOperation中信号灯地址不能为空");
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("P1".getBytes()));
        // 区域号
        byteList.addAll(Bytes.asList("00".getBytes()));
        byteList.addAll(Bytes.asList(address.getBytes()));
        byteList.addAll(Bytes.asList("88888".getBytes()));
        if(byteList.size() > FlowConstants.MAX_DATA_LENGTH){
            throw new SimpleException("命令超过最大长度");
        }
        return Bytes.toArray(byteList);
    }
}

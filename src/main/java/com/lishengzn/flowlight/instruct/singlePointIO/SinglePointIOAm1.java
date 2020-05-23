package com.lishengzn.flowlight.instruct.singlePointIO;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.instruct.Instruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 可以反复设定的信号灯
 * Am1[1][32h][22h][11h]
 * [1]:address
 */
public class SinglePointIOAm1  implements Instruct {

    private int maxLength = 64;

    /**
     * 信号灯地址，4个字节，范围1~7999
     */
    private String address;



    public SinglePointIOAm1() {
    }

    public SinglePointIOAm1(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getBytes(){
        Objects.requireNonNull(this.address, "信号灯指令Ami中电子标签地址不能为空");
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("Am1".getBytes()));
        byteList.addAll(Bytes.asList(address.getBytes()));
        byteList.add((byte)0x32);
        byteList.add((byte)0x22);
        byteList.add((byte)0x11);
        if(byteList.size() > maxLength){
            throw new SimpleException("命令超过最大长度");
        }
        return Bytes.toArray(byteList);
    }


}

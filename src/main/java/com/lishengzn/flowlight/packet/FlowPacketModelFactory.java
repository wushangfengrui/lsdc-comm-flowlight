package com.lishengzn.flowlight.packet;

import com.lishengzn.comm.exception.SimpleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FlowPacketModelFactory {
    private static Logger LOG = LoggerFactory.getLogger(FlowPacketModelFactory.class);
    public static FlowPacketModel getPriorityModelResponse(int serial){
        return new FlowPacketModel(serial,new byte[]{(byte)0x4f});
    }

    public static FlowPacketModel getPriorityModelExceptionResponse(int serial){
        return new FlowPacketModel(serial,new byte[]{(byte)0x4e});
    }

    public static boolean checkResponse(byte[] data){
        Objects.requireNonNull(data);
        if(data.length != 1){
            throw new SimpleException("指令应答不对！");
        }
        if(data[0] == (byte)0x6f){
            return true;
        }else if(data[0] == (byte)0x6f){
            LOG.error("指令被拒绝，参数超过最大规定字符数");
            return false;
        }else if(data[0] == (byte)0x6f){
            LOG.error("指令被拒绝，前后两指令具有相同序列号");
            return false;
        }else{
            throw new SimpleException("指令应答不对！");
        }
    }
}

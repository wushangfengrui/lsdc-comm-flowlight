package com.lishengzn.flowlight.instruct.init;

import com.google.common.primitives.Bytes;
import com.lishengzn.comm.exception.SimpleException;
import com.lishengzn.flowlight.constants.FlowConstants;
import com.lishengzn.flowlight.instruct.Instruct;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制器的动作说明
 * @[1]0[2]0[3]
 * [1]:控制器试图向电子标签传送指令次数 0：5次，1：0
 */
public class Action  implements Instruct {
    public static final byte TIME_5 = (byte)0;
    public static final byte TIME_0 = (byte)1;

    public static final byte EQUIPMENT_SIGNAL = (byte)0;
    public static final byte EQUIPMENT_DISPLAY = (byte)1;

    public static final byte BOTTOM_UNUSE = (byte)0;
    public static final byte BOTTOM_USE = (byte)1;

    private byte time = TIME_5;

    private byte equipment = EQUIPMENT_SIGNAL;

    private byte bottom = BOTTOM_UNUSE;

    public Action(byte time, byte equipment, byte bottom) {
        this.time = time;
        this.equipment = equipment;
        this.bottom = bottom;
    }

    public byte getTime() {
        return time;
    }

    public void setTime(byte time) {
        this.time = time;
    }

    public byte getEquipment() {
        return equipment;
    }

    public void setEquipment(byte equipment) {
        this.equipment = equipment;
    }

    public byte getBottom() {
        return bottom;
    }

    public void setBottom(byte bottom) {
        this.bottom = bottom;
    }

    public byte[] getBytes(){
        List<Byte> byteList = new ArrayList<>();
        byteList.addAll(Bytes.asList("@".getBytes()));
        byteList.add(time);
        byteList.add((byte)0);
        byteList.add(equipment);
        byteList.add((byte)0);
        byteList.add(bottom);
        if(byteList.size() > FlowConstants.MAX_DATA_LENGTH){
            throw new SimpleException("命令超过最大长度");
        }
        return Bytes.toArray(byteList);
    }
}

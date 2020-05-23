package com.lishengzn.flowlight.instruct;

import com.lishengzn.comm.exception.SimpleException;

public class ModeArray {
    /**  闪烁有声 Scintillation audio*/
    /** 白色 */
    public static final byte[] Scintillation_Audio_white = getBytes("m1$33$33$24");
    public static final byte[] Scintillation_Audio_red   = getBytes("m1$33$11$24");
    public static final byte[] Scintillation_Audio_green = getBytes("m1$31$31$24");
    public static final byte[] Scintillation_Audio_blue  = getBytes("m1$31$13$24");
    public static final byte[] Scintillation_Audio_yellow= getBytes("m1$33$31$24");
    /** 紫色 */
    public static final byte[] Scintillation_Audio_purple= getBytes("m1$33$13$24");
    /** 青色 */
    public static final byte[] Scintillation_Audio_cyan  = getBytes("m1$31$33$24");

    /**  闪烁无声 Scintillation silent*/
    public static final byte[] Scintillation_Silent_white = getBytes("m1$33$33$21");
    public static final byte[] Scintillation_Silent_red   = getBytes("m1$33$11$21");
    public static final byte[] Scintillation_Silent_green = getBytes("m1$31$31$21");
    public static final byte[] Scintillation_Silent_blue  = getBytes("m1$31$13$21");
    public static final byte[] Scintillation_Silent_yellow= getBytes("m1$33$31$21");
    /** 紫色 */
    public static final byte[] Scintillation_Silent_purple= getBytes("m1$33$13$21");
    /** 青色 */
    public static final byte[] Scintillation_Silent_cyan  = getBytes("m1$31$33$21");

    /**  常亮有声 Normally on the audio*/
    public static final byte[] Normally_Audio_white = getBytes("m1$32$22$24");
    public static final byte[] Normally_Audio_red   = getBytes("m1$32$11$24");
    public static final byte[] Normally_Audio_green = getBytes("m1$31$21$24");
    public static final byte[] Normally_Audio_blue  = getBytes("m1$31$12$24");
    public static final byte[] Normally_Audio_yellow= getBytes("m1$32$21$24");
    /** 紫色 */
    public static final byte[] Normally_Audio_purple= getBytes("m1$32$12$24");
    /** 青色 */
    public static final byte[] Normally_Audio_cyan  = getBytes("m1$31$22$24");

    /**  常亮无声 Normally on the silent*/
    public static final byte[] Normally_Silent_white= getBytes("m1$32$22$21");
    public static final byte[] Normally_Silent_red  = getBytes("m1$32$11$21");
    public static final byte[] Normally_Silent_green= getBytes("m1$31$21$21");
    public static final byte[] Normally_Silent_blue = getBytes("m1$31$12$21");
    public static final byte[] Normally_Silent_yellow=getBytes("m1$32$21$21");
    /** 紫色 */
    public static final byte[] Normally_Silent_purple=getBytes("m1$32$12$21");
    /** 青色 */
    public static final byte[] Normally_Silent_cyan = getBytes("m1$31$22$21");

    private static byte[] getBytes(String color){
        if(color.length() != 11){
            throw new SimpleException("ModeArray 常量异常");
        }
        byte[] bytes = new byte[5];
        bytes[0] = 'm';
        bytes[1] = '1';
        bytes[2] = Byte.valueOf(color.substring(3,5),16);
        bytes[3] = Byte.valueOf(color.substring(6,8),16);
        bytes[4] = Byte.valueOf(color.substring(9,11),16);
        return bytes;
    }


    private byte[] modeArray = new byte[0];
    /**
     * 添加表示条件
     */
    public byte[] addSaidConditions(byte[] bytes){
        byte[] temp = new byte[modeArray.length+bytes.length];
        System.arraycopy(modeArray,0,temp,0,modeArray.length);
        System.arraycopy(bytes,0,temp,modeArray.length,bytes.length);
        modeArray = temp;
        return modeArray;
    }

}

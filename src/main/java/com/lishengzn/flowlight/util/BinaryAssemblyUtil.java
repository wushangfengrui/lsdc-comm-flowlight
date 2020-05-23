package com.lishengzn.flowlight.util;

import java.nio.ByteOrder;

public class BinaryAssemblyUtil {

    private static ByteOrder btodr=null;

    /** 设置全局字节序
     * @param bo 字节序
     */
    public static void setGloableByteOrder(ByteOrder bo){
        btodr=bo;
    }

    public static int bytes2Int(byte[] src){
        return Integer.valueOf(new String(src));
    }

}

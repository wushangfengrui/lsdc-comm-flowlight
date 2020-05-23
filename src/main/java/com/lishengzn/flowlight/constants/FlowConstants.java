package com.lishengzn.flowlight.constants;

public interface FlowConstants {
    /** LED 通讯包头，固定字节*/
    byte LED_SYNC_STX= 0x02;

    /** LED 通讯包尾，固定字节*/
    byte LED_SYNC_ETX= 0x03;

    int MAX_DATA_LENGTH = 1015;
    int MAX_WAIT_TIME = 5; // 单位 s
    int MAC_RESEND_TIME = 5; // 次
    int MAC_RECONNECT_TIME = 5; // 次
}

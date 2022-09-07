package com.github.zzzj1233.ex;

import com.github.zzzj1233.protocol.ErrorPacket;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2022-09-06 18:59
 */
public class MysqlProtocolException extends IOException {

    public MysqlProtocolException(ErrorPacket errorPacket) {
        super(errorPacket.toString());
    }

    public MysqlProtocolException() {
    }

    public MysqlProtocolException(String message) {
        super(message);
    }

    public MysqlProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public MysqlProtocolException(Throwable cause) {
        super(cause);
    }
}

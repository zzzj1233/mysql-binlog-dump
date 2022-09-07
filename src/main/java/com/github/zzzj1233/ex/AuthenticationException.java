package com.github.zzzj1233.ex;

import com.github.zzzj1233.protocol.ErrorPacket;

/**
 * @author zzzj
 * @create 2022-09-06 18:59
 */
public class AuthenticationException extends MysqlProtocolException {

    public AuthenticationException(ErrorPacket errorPacket) {
        super(errorPacket);
    }

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

}

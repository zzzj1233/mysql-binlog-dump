package com.zzzj.exception;

import com.zzzj.command.ErrPacket;

/**
 * @author zzzj
 * @create 2023-07-20 14:34
 */
public class UnExpectErrPacketException extends RuntimeException {

    private final int errorCode;

    private final String errorMessage;

    public UnExpectErrPacketException(ErrPacket errPacket) {
        super(String.format("Encountered an unexpected ErrPacket , errCode = %d , errMsg = %s %n", errPacket.getErrorCode(), errPacket.getErrorMessage()));

        errorCode = errPacket.getErrorCode();

        errorMessage = errPacket.getErrorMessage();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}

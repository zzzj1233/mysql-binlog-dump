package com.zzzj.command;

/**
 * @author zzzj
 * @create 2023-07-20 11:12
 */
public abstract class ResponsePacket {

    public boolean isOkPacket() {
        return OkPacket.class.isAssignableFrom(this.getClass());
    }

    public boolean isErrPacket() {
        return ErrPacket.class.isAssignableFrom(this.getClass());
    }

    public boolean isEofPacket() {
        return EofPacket.class.isAssignableFrom(this.getClass());
    }

}

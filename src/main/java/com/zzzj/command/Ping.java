package com.zzzj.command;

public class Ping implements Command {

    @Override
    public byte[] toBytes() {
        return new byte[]{0x0E};
    }

}

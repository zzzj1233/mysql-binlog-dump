package com.zzzj.command;

/**
 * @author zzzj
 * @create 2023-07-20 11:35
 */
public interface Command {

    byte[] toBytes();

    default int sequence() {
        return 0;
    }

}

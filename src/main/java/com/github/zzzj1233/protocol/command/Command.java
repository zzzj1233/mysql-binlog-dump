package com.github.zzzj1233.protocol.command;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2022-09-06 15:36
 */
public interface Command {

    byte[] toByteArray() throws IOException;

}

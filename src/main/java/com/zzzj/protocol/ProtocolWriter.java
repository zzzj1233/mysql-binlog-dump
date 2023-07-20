package com.zzzj.protocol;

import com.zzzj.command.Command;
import com.zzzj.command.ProtocolPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zzzj
 * @create 2023-07-20 11:39
 */
public class ProtocolWriter {

    private final OutputStream outputStream;

    private static final Logger log = LoggerFactory.getLogger(ProtocolWriter.class);

    public ProtocolWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeAndFlush(Command command) {
        try {
            outputStream.write(new ProtocolPacket(command.sequence(), command.toBytes()).toBytes());
            outputStream.flush();
        } catch (IOException e) {
            log.error("Write to mysql server error : ", e);
        }

    }

}

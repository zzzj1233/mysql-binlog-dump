package com.zzzj.command;

import com.zzzj.protocol.Capability;
import com.zzzj.protocol.Reader;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2023-07-20 11:03
 */
public class EofPacket extends ResponsePacket {

    private int warnings;

    private int serverStatus;

    public EofPacket(int capability, Reader reader) throws IOException {
        if ((capability & Capability.CLIENT_PROTOCOL_41) != 0) {
            warnings = reader.readInt2();
            serverStatus = reader.readInt2();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EofPacket{");
        sb.append("warnings=").append(warnings);
        sb.append(", serverStatus=").append(serverStatus);
        sb.append('}');
        return sb.toString();
    }

}

package com.zzzj.command;

import com.zzzj.protocol.Capability;
import com.zzzj.protocol.MySQLServerStatus;
import com.zzzj.protocol.Reader;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2023-07-20 10:53
 */
public class OkPacket extends ResponsePacket {

    private int affectedRows;

    private int lastInsertId;

    private int serverStatus;

    private int warnings;

    private String info;

    private String sessionStatusInfo;

    public OkPacket(int capability, Reader reader) throws IOException {

        this.affectedRows = reader.readLengthInt();

        this.lastInsertId = reader.readLengthInt();

        this.serverStatus = reader.readInt2();

        this.warnings = reader.readInt2();

        if ((capability & Capability.CLIENT_SESSION_TRACK) != 0)
            info = reader.readLengthString();

        if ((serverStatus & MySQLServerStatus.SERVER_SESSION_STATE_CHANGED) != 0)
            sessionStatusInfo = reader.readLengthString();
        else
            info = reader.readEOFString();

    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OkPacket{");
        sb.append("affectedRows=").append(affectedRows);
        sb.append(", lastInsertId=").append(lastInsertId);
        sb.append(", serverStatus=").append(serverStatus);
        sb.append(", warnings=").append(warnings);
        sb.append(", info='").append(info).append('\'');
        sb.append('}');
        return sb.toString();
    }

}

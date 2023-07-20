package com.zzzj.command;

import com.zzzj.protocol.Capability;
import com.zzzj.protocol.Reader;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2023-07-20 11:01
 */
public class ErrPacket extends ResponsePacket {

    private int errorCode;

    private String sqlStateMarker;

    private String sqlState;

    private String errorMessage;

    public ErrPacket(int capability, Reader reader) throws IOException {

        this.errorCode = reader.readInt2();

        if ((capability & Capability.CLIENT_PROTOCOL_41) != 0) {
            sqlStateMarker = reader.readString(1);
            sqlState = reader.readString(5);
        }

        this.errorMessage = reader.readEOFString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ErrPacket{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", errorMessage='").append(errorMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getSqlStateMarker() {
        return sqlStateMarker;
    }

    public String getSqlState() {
        return sqlState;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

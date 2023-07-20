package com.zzzj.events;

import com.zzzj.protocol.Reader;

import java.io.IOException;

public class FormatDescriptionEvent extends BinlogEvent {

    private final int binlogVersion;

    private final String mysqlServerVersion;

    private final int createTimeStamp;

    public FormatDescriptionEvent(byte[] bodyBytes) throws IOException {

        super(bodyBytes);

        Reader reader = new Reader(bodyBytes);

        binlogVersion = reader.readInt2();

        mysqlServerVersion = reader.readNullEndString();

        createTimeStamp = reader.readInt4();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FormatDescriptionEvent{");
        sb.append("binlogVersion=").append(binlogVersion);
        sb.append(", mysqlServerVersion='").append(mysqlServerVersion).append('\'');
        sb.append(", createTimeStamp=").append(createTimeStamp);
        sb.append('}');
        return sb.toString();
    }

}

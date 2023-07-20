package com.zzzj.events;

import com.zzzj.protocol.Reader;

import java.io.IOException;

public class RotateEvent extends BinlogEvent {

    private final long position;

    private final String binlogFileName;

    public RotateEvent(byte[] bodyBytes) throws IOException {

        super(bodyBytes);

        Reader reader = new Reader(bodyBytes);

        position = reader.readInt8();

        binlogFileName = reader.readEOFString();

    }

    public long getPosition() {
        return position;
    }

    public String getBinlogFileName() {
        return binlogFileName;
    }

    @Override
    public String toString() {
        return "RotateEvent{" +
                "position=" + position +
                ", binlogFileName='" + binlogFileName + '\'' +
                '}';
    }

}

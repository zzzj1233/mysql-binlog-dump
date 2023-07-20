package com.zzzj.events;

import com.zzzj.protocol.Reader;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2023-07-20 16:26
 */
public class BinlogEventHeader {

    private final int timestamp;

    private final int eventType;

    private final int serverId;

    private final int eventSize;

    private final int position;

    private final int flags;

    private final int bodySize;

    public BinlogEventHeader(Reader reader) throws IOException {

        this.timestamp = reader.readInt4();

        this.eventType = reader.readInt1();

        this.serverId = reader.readInt4();

        this.eventSize = reader.readInt4();

        this.position = reader.readInt4();

        this.flags = reader.readInt2();

        this.bodySize = eventSize - 4 - 1 - 4 - 4 - 4 - 2;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getEventType() {
        return eventType;
    }

    public int getServerId() {
        return serverId;
    }

    public int getEventSize() {
        return eventSize;
    }

    public int getPosition() {
        return position;
    }

    public int getFlags() {
        return flags;
    }

    public int getBodySize() {
        return bodySize;
    }
}

package com.zzzj.command;

import com.zzzj.utils.BytesWriter;


/**
 * @author zzzj
 * @create 2023-07-19 17:32
 */
public class ProtocolPacket {

    private final int seq;

    private final byte[] payload;

    public ProtocolPacket(int seq, byte[] payload) {
        byte[] buf;

        this.seq = seq;

        this.payload = payload;
    }

    public int getSeq() {
        return seq;
    }

    public byte[] getPayload() {
        return payload;
    }

    public byte[] toBytes() {

        BytesWriter writer = new BytesWriter();

        writer.writeInt(payload.length, 3);

        writer.writeInt(seq, 1);

        writer.writeBytes(payload);

        return writer.toBytes();
    }


    @Override
    public String toString() {
        return String.format("Seq = %d , payloadLength = %d %n ", seq, payload.length);
    }
}

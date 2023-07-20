package com.zzzj.protocol;

import com.zzzj.command.EofPacket;
import com.zzzj.command.ErrPacket;
import com.zzzj.exception.UnExpectErrPacketException;
import com.zzzj.response.ServerHandShake;
import com.zzzj.command.ProtocolPacket;
import com.zzzj.command.ResponsePacket;
import com.zzzj.factory.ResponsePacketFactory;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.zzzj.command.ClientHandShake.CLIENT_CAPABILITY;

/**
 * @author zzzj
 * @create 2023-07-19 17:41
 */
public class Reader {

    private final InputStream inputStream;

    public Reader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Reader(byte[] bytes) {
        this(new ByteArrayInputStream(bytes));
    }

    public Reader(ProtocolPacket packet) {
        this(packet.toBytes());
    }

    public EofPacket readEOFPacket() throws IOException {
        ResponsePacket responsePacket = ResponsePacketFactory.readPacket(CLIENT_CAPABILITY, this.readPacket());
        if (!responsePacket.isEofPacket())
            throw new IllegalStateException("ResponsePacket not match the expect packet : EofPacket ; truly packet type = " + responsePacket.getClass().getSimpleName());

        return (EofPacket) responsePacket;
    }

    public ResponsePacket readResponsePacket() throws IOException {
        return ResponsePacketFactory.readPacket(CLIENT_CAPABILITY, this.readPacket());
    }

    public ProtocolPacket readPacket() throws IOException {

        int payloadLength = readInt3();

        int seq = readInt1();

        ProtocolPacket packet = new ProtocolPacket(seq, readBytes(payloadLength));

        if (packet.getPayload().length > 0 && (packet.getPayload()[0] & 0XFF) == 0xFF) {
            // ERR_PACKET
            throw new UnExpectErrPacketException((ErrPacket) ResponsePacketFactory.readPacket(CLIENT_CAPABILITY, packet));
        }

        return packet;
    }

    public ServerHandShake readHandShake() throws IOException {

        int protocol = readInt1();

        String version = readNullEndString();

        int threadId = readInt4();

        String authPluginDataPart1 = readNullEndString();

        int capabilityLow = readInt2();

        int charset = readInt1();

        int statusFlag = readInt2();

        int capabilityHigh = readInt2();

        int capability = capabilityHigh << 16 | (capabilityLow);

        skip(1);

        // reversed
        skip(10);

        String authPluginDataPart2 = readNullEndString();

        String authPluginName = null;

        if ((capability & Capability.CLIENT_PLUGIN_AUTH) != 0) {
            authPluginName = readNullEndString();
        }

        return new ServerHandShake(protocol, version, threadId, capability, authPluginDataPart1, authPluginDataPart2, authPluginName);
    }

    public Reader skip(long skip) throws IOException {

        inputStream.skip(skip);

        return this;
    }

    public int readLengthInt() throws IOException {
        int len = readInt1();

        if (len < 0xFC)
            return len;

        if (len == 0XFC)
            return readInt2();

        if (len == 0XFD)
            return readInt3();

        return readInt4();
    }

    public int readInt1() throws IOException {
        return inputStream.read();
    }

    public int readInt(int len) throws IOException {

        if (len > 4) throw new IllegalArgumentException("Invalid int length : " + len);

        int value = 0;

        for (int i = 0; i < len; i++)
            value |= inputStream.read() << (i << 3);

        return value;
    }

    public long readInt8() throws IOException {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            int byteValue = inputStream.read();
            if (byteValue == -1) {
                throw new EOFException("End of file reached while reading 8 bytes.");
            }
            value |= (long) (byteValue & 0xFF) << (8 * i);
        }
        return value;
    }


    public int readInt2() throws IOException {
        return inputStream.read() |
                inputStream.read() << 8;
    }

    public int readInt3() throws IOException {
        return inputStream.read() |
                inputStream.read() << 8 |
                inputStream.read() << 16;
    }

    public int readInt4() throws IOException {
        return inputStream.read() |
                inputStream.read() << 8 |
                inputStream.read() << 16 |
                inputStream.read() << 24;
    }

    public byte[] readRemain() throws IOException {
        return readBytes(inputStream.available());
    }

    public byte[] readBytes(int length) throws IOException {
        byte[] bytes = new byte[length];

        inputStream.read(bytes);

        return bytes;
    }

    public String readLengthString() throws IOException {

        int first = readInt1();

        int len;

        if (first < 0xFC)
            len = first;
        else if (first == 0XFC)
            len = readInt2();
        else if (first == 0XFD)
            len = readInt3();
        else
            len = readInt4();

        return readString(len);
    }

    public String readString(int length) throws IOException {
        if (length == 0) return "";

        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++)
            bytes[i] = (byte) inputStream.read();

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public int available() throws IOException {
        return inputStream.available();
    }

    public boolean readable() throws IOException {
        return inputStream.available() != 0;
    }

    public String readEOFString() throws IOException {

        int available = inputStream.available();

        if (available == 0) return "";

        return readString(available);
    }

    public String readNullEndString() throws IOException {

        List<Byte> list = new ArrayList<>();

        Byte b;

        while ((b = ((byte) inputStream.read())) != 0) list.add(b);

        byte[] bytes = new byte[list.size()];

        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

}

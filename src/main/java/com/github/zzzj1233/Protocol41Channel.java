package com.github.zzzj1233;

import com.github.zzzj1233.constant.CapabilityFlags;
import com.github.zzzj1233.constant.PacketType;
import com.github.zzzj1233.ex.MysqlProtocolException;
import com.github.zzzj1233.protocol.ErrorPacket;
import com.github.zzzj1233.protocol.ResultSet;
import com.github.zzzj1233.protocol.command.Command;
import com.github.zzzj1233.utils.BytesReader;
import com.github.zzzj1233.utils.BytesUtils;
import com.github.zzzj1233.utils.BytesWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用protocol41与MYSQL进行交互
 * <p>
 * 具体形式表现在客户端认证时,flags加上 {@link CapabilityFlags#CLIENT_PROTOCOL_41}
 *
 * @author zzzj
 * @create 2022-09-06 18:15
 */
public class Protocol41Channel {

    private final OutputStream outputStream;

    private final InputStream inputStream;

    private int sequence;

    public Protocol41Channel(Socket socket) throws IOException {
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void write(byte[] body) throws IOException {
        int len = body.length;

        BytesWriter writer = new BytesWriter();

        writer.writeInt(len, 3);
        writer.writeInt(sequence, 1);
        writer.writeBytes(body);
        writer.write(outputStream);
        outputStream.flush();

        incrSequence();
    }

    public void write(Command command) throws IOException {
        write(command.toByteArray());
    }

    private int readLength() throws IOException {
        int result = 0;
        for (int i = 0; i < 3; ++i) {
            result |= (inputStream.read() << (i * 8));
        }
        return result;
    }

    public byte[] read() throws IOException {
        int len = readLength();

        int sequence = inputStream.read();

        if (sequence != this.sequence) {
            // ERROR
            throw new IOException("unexpected sequence : " + sequence + " , channel sequence : " + this.sequence);
        }

        incrSequence();

        byte[] bytes = new byte[len];

        inputStream.read(bytes);

        if (bytes[0] == PacketType.ERR) {
            throw new MysqlProtocolException(new ErrorPacket(bytes));
        }

        return bytes;
    }

    public ResultSet readResultSet() throws IOException {
        byte[] columnCountBytes = this.read();

        int columnCount = BytesUtils.bytesToInt(columnCountBytes);

        List<String> columns = new ArrayList<>(columnCount);

        // ColumnDefinition
        for (int i = 0; i < columnCount; i++) {
            byte[] columnBytes = this.read();

            BytesReader reader = new BytesReader(columnBytes);

            String catelog = reader.readLenencStr();
            String schema = reader.readLenencStr();
            String table = reader.readLenencStr();
            String orgTable = reader.readLenencStr();
            String name = reader.readLenencStr();
            String orgName = reader.readLenencStr();

            columns.add(name);
        }

        // skip EOF
        // 如果设置 CapabilityFlags.CLIENT_DEPRECATE_EOF, 则不会响应eof包
        read();

        List<List<String>> values = new ArrayList<>();

        byte[] bytes;

        // EOF代表读到末尾了
        while ((bytes = read())[0] != PacketType.EOF) {

            if (bytes[0] == PacketType.ERR) {
                throw new MysqlProtocolException(new ErrorPacket(bytes));
            }

            BytesReader reader = new BytesReader(bytes);

            LinkedList<String> value = new LinkedList<>();

            for (int i = 0; i < columnCount; i++) {
                value.addLast(reader.readLenencStr());
            }

            values.add(value);
        }

        return new ResultSet(columns, values);
    }

    private void incrSequence() {
        if (sequence + 1 > 255) {
            sequence = 0;
        } else {
            sequence++;
        }
    }

    public void resetSequence() {
        this.sequence = 0;
    }

}

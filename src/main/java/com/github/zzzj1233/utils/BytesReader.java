package com.github.zzzj1233.utils;


import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author zzzj
 * @create 2022-09-05 16:56
 */
public class BytesReader {

    private final byte[] bytes;

    private int index;

    public BytesReader(byte[] bytes) {
        this.bytes = bytes;
    }

    public long readLong(int len) {
        long result = 0;

        for (int i = 0; i < len; i++) {
            result |= bytes[index + i] << (i * 8);
        }

        index += len;

        return result;
    }


    public int readInt(int len) {
        int result = 0;

        for (int i = 0; i < len; i++) {
            result |= (bytes[index + i] & 0xff) << (i * 8);
        }

        index += len;

        return result;
    }

    public byte[] readBytes(int len) {
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = this.bytes[index + i];
        }
        index += len;
        return bytes;
    }

    public String readString(int len) {
        return new String(readBytes(len), StandardCharsets.UTF_8);
    }

    public String readStringUntilZero() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while (bytes[index] != 0) {
            outputStream.write(bytes[index]);
            index++;
        }

        index++;

        return outputStream.toString();
    }

    public String readLenencStr() {
        if (!hasRemain()){
            return null;
        }
        int count = this.readInt(1);
        if (count == 0) {
            return null;
        }
        return this.readString(count);
    }

    public int readLenencInt() {
        int count = this.readInt(1);
        if (count == 0) {
            return 0;
        }
        return this.readInt(count);
    }

    public String readUntilEOF() {
        return readString(bytes.length - index);
    }

    public void skip(int len) {
        index += len;
    }

    public boolean hasRemain() {
        return index < bytes.length;
    }

    public byte[] getBytes() {
        return bytes;
    }
}

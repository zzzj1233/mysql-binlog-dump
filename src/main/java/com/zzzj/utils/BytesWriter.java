package com.zzzj.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author zzzj
 * @create 2023-07-19 19:07
 */
public class BytesWriter {

    private final ByteArrayOutputStream outputStream;

    public BytesWriter() {
        outputStream = new ByteArrayOutputStream();
    }

    public void writeEmpty(long len) {
        for (int i = 0; i < len; i++) outputStream.write(0);
    }

    public void writeEOFString(String value) {
        try {
            outputStream.write(value.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {

        }
    }

    public void writeLengthString(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        if (bytes.length < 0xFB) {
            outputStream.write((byte) bytes.length);
        } else if (bytes.length < 0xFFFF) {
            outputStream.write((byte) 0xFC);
            outputStream.write((short) bytes.length);
        } else if (bytes.length < 0xFFFFFF) {
            outputStream.write((byte) 0xFD);
            outputStream.write(bytes.length);
        } else {
            outputStream.write((byte) 0xFE);
            outputStream.write(bytes.length);
        }

        try {
            outputStream.write(bytes);
        } catch (IOException ignored) {

        }
    }

    public void writeStringNullEnd(String value) {
        try {
            outputStream.write(value.getBytes(StandardCharsets.UTF_8));
            outputStream.write(0);
        } catch (IOException ignored) {

        }

    }

    public void writeBytes(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException ignored) {

        }
    }

    public void writeInt(int value, int len) {

        if (len > 4) throw new IllegalArgumentException("Illegal int len : " + len);

        for (int i = 0; i < len; i++)
            outputStream.write((value >> (i << 3)) & 0xff);

    }

    public byte[] toBytes() {
        return outputStream.toByteArray();
    }

}

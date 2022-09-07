package com.github.zzzj1233.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author zzzj
 * @create 2022-09-06 17:41
 */
public class BytesWriter {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public void write(OutputStream out) throws IOException {
        outputStream.writeTo(out);
    }

    public void writeByte(byte _byte) throws IOException {
        outputStream.write(_byte);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    /**
     * 小端字节序
     */
    public void writeInt(int value, int len) {
        for (int i = 0; i < len; i++) {
            outputStream.write((value >>> (i * 8)) & 0x000000FF);
        }
    }

    public void writeString(String value) throws IOException {
        outputStream.write(value.getBytes(StandardCharsets.UTF_8));
    }

    public void writeStringNull(String value) throws IOException {
        outputStream.write(value.getBytes(StandardCharsets.UTF_8));
        outputStream.write(0);
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

}

package com.github.zzzj1233.utils;

/**
 * @author zzzj
 * @create 2022-09-07 14:25
 */
public class BytesUtils {

    /**
     * 小端字节序,字节转int
     *
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        int result = 0;

        int N = bytes.length;

        for (int i = 0; i < N; i++) {
            result |= bytes[i] << (i * 8);
        }

        return result;
    }

}

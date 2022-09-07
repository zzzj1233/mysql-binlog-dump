package com.github.zzzj1233.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zzzj
 * @create 2022-09-06 17:53
 */
public class MysqlUtils {

    /**
     * SHA1( password ) XOR SHA1( "20-bytes random data from server" <concat> SHA1( SHA1( password ) ) )
     */
    public static byte[] native41Password(String password, String serverRandomData) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] passwordHash = sha.digest(password.getBytes());
        return xor(passwordHash, sha.digest(union(serverRandomData.getBytes(), sha.digest(passwordHash))));
    }

    private static byte[] union(byte[] a, byte[] b) {
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static byte[] xor(byte[] input, byte[] against) {
        byte[] to = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            to[i] = (byte) (input[i] ^ against[i]);
        }
        return to;
    }

}

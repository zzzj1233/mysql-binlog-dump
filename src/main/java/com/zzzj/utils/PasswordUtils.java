package com.zzzj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zzzj
 * @create 2023-07-19 19:17
 */
public class PasswordUtils {

    /**
     * <p>SHA1( password ) XOR SHA1( "salt" <concat> SHA1( SHA1( password ) ) ) </p>
     *
     * <a href="https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_authentication_methods_native_password_authentication.html">native_password算法文档链接</a>
     */
    public static byte[] nativePassword(String password, String salt) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] passwordHash = sha.digest(password.getBytes());
        return xor(passwordHash, sha.digest(union(salt.getBytes(), sha.digest(passwordHash))));
    }

    public static byte[] xor(byte[] input, byte[] against) {
        byte[] to = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            to[i] = (byte) (input[i] ^ against[i % against.length]);
        }
        return to;
    }

    private static byte[] union(byte[] a, byte[] b) {
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }


}

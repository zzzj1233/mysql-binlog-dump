package com.zzzj.command;

import com.zzzj.protocol.Capability;
import com.zzzj.utils.BytesWriter;
import com.zzzj.utils.PasswordUtils;

import static com.zzzj.protocol.Capability.*;

/**
 * @author zzzj
 * @create 2023-07-19 18:00
 */
public class ClientHandShake implements Command {

    public static final int CHARSET_UTF8 = 0x21;

    private final int maxPackageSize = Integer.MAX_VALUE;

    private final String userName;

    private final String password;

    private final String salt;

    private final int seq;

    private static final String PASSWORD_PLUGIN_NAME = "mysql_native_password";

    public static final int CLIENT_CAPABILITY = Capability.CLIENT_PROTOCOL_41 | Capability.CLIENT_PLUGIN_AUTH | CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA | CLIENT_LONG_FLAG | CLIENT_SECURE_CONNECTION;

    public ClientHandShake(
            String userName,
            String password,
            String salt,
            int serverSeq
    ) {
        this.userName = userName;
        this.password = password;
        this.salt = salt;
        this.seq = serverSeq + 1;
    }

    public byte[] toBytes() {
        BytesWriter writer = new BytesWriter();

        writer.writeInt(CLIENT_CAPABILITY, 4);
        writer.writeInt(maxPackageSize, 4);
        writer.writeInt(CHARSET_UTF8, 1);
        writer.writeEmpty(23);
        writer.writeStringNullEnd(userName);
        byte[] password = PasswordUtils.nativePassword(this.password, salt);
        writer.writeInt(password.length, 1);
        writer.writeBytes(password);
        writer.writeStringNullEnd(PASSWORD_PLUGIN_NAME);

        return writer.toBytes();

    }

    @Override
    public int sequence() {
        return this.seq;
    }

    public int getCapability() {
        return CLIENT_CAPABILITY;
    }
}

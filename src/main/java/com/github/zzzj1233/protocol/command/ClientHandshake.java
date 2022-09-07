package com.github.zzzj1233.protocol.command;

import cn.hutool.log.Log;
import com.github.zzzj1233.Protocol41Channel;
import com.github.zzzj1233.constant.PacketType;
import com.github.zzzj1233.ex.AuthenticationException;
import com.github.zzzj1233.protocol.ServerHandshake;
import com.github.zzzj1233.utils.BytesReader;
import com.github.zzzj1233.utils.MysqlUtils;
import com.github.zzzj1233.constant.CapabilityFlags;
import com.github.zzzj1233.utils.BytesWriter;

import java.io.IOException;

/**
 * <a href='https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeResponse'>HandshakeResponse</a>
 *
 * @author zzzj
 * @create 2022-09-06 15:34
 */
public class ClientHandshake implements Command {

    private final String username;

    private final String password;

    private final String database;

    private final ServerHandshake serverHandshake;

    public static final int MAX_PACKET_SIZE = (1 << 24);

    private final String SHA2_PASSWORD = "caching_sha2_password";

    private final String MYSQL_NATIVE = "mysql_native_password";

    private Protocol41Channel channel;

    private boolean switched;

    public ClientHandshake(String username,
                           String password,
                           ServerHandshake serverHandshake,
                           Protocol41Channel channel) {

        this(username, password, null, serverHandshake, channel);
    }

    public ClientHandshake(String username,
                           String password,
                           String database,
                           ServerHandshake serverHandshake,
                           Protocol41Channel channel) {

        this.username = username;
        this.password = password;
        this.database = database;
        this.serverHandshake = serverHandshake;
        this.channel = channel;
    }


    public void sendHandShake() throws IOException {
        sendHandShake(this.toByteArray());
    }

    private void sendHandShake(byte[] bytes) throws IOException {
        channel.write(bytes);
        read();
    }

    private void read() throws IOException {
        byte[] response = channel.read();

        byte packetType = response[0];

        switch (packetType) {
            case PacketType.OK:
                Log.get().info("Authentication successful");
                channel.resetSequence();
                return;
            case PacketType.EOF:
                // https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::AuthSwitchRequest
                if (this.switched) {
                    throw new AuthenticationException("Unexpected packetType : " + packetType);
                }
                sendHandShake(switchAuthentication(response));
                return;
            default:
                throw new AuthenticationException("Unexpected packetType : " + packetType);
        }

    }


    private byte[] toByteArray(String salt) throws IOException {
        int capabilityFlag = CapabilityFlags.CLIENT_PROTOCOL_41 |
                CapabilityFlags.CLIENT_SECURE_CONNECTION |
                CapabilityFlags.CLIENT_LONG_FLAG |
                CapabilityFlags.CLIENT_PLUGIN_AUTH |
                CapabilityFlags.CLIENT_INTERACTIVE;

        if (this.database != null) {
            capabilityFlag |= CapabilityFlags.CLIENT_CONNECT_WITH_DB;
        }

        BytesWriter writer = new BytesWriter();

        writer.writeInt(capabilityFlag, 4);
        writer.writeInt(MAX_PACKET_SIZE, 4);
        writer.writeInt(serverHandshake.getCharSet(), 1);
        for (int i = 0; i < 23; i++) {
            writer.writeByte((byte) 0);
        }
        writer.writeStringNull(username);
        byte[] password = MysqlUtils.native41Password(this.password, salt);
        writer.writeInt(password.length, 1);
        writer.writeBytes(password);
        writer.writeString("mysql_native_password");

        return writer.getBytes();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return toByteArray(serverHandshake.getSalt());
    }

    /**
     * 认证时返回EOF包时,需要用mysql返回的认证方法和salt再次认证一次
     */
    private byte[] switchAuthentication(byte[] response) throws IOException {
        this.switched = true;

        BytesReader reader = new BytesReader(response);

        reader.skip(1);

        String pluginName = reader.readStringUntilZero();

        String authPluginData = reader.readStringUntilZero();

        if (SHA2_PASSWORD.equals(pluginName)) {
            return toByteArray(authPluginData);
        } else if (MYSQL_NATIVE.equals(pluginName)) {
            return MysqlUtils.native41Password(this.password, authPluginData);
        } else {
            throw new IOException("unsupported auth pluginName: " + pluginName);
        }

    }

}

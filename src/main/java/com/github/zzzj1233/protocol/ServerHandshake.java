package com.github.zzzj1233.protocol;

import com.github.zzzj1233.constant.CapabilityFlags;
import com.github.zzzj1233.utils.BytesReader;


/**
 * @author zzzj
 * @create 2022-09-05 16:55
 */
public class ServerHandshake {


    private int protocolVersion;

    private String serverVersion;

    private long connectionId;

    private int charSet;

    private int statusFlag;

    private int capability;

    private String authPluginName;

    private String salt;

    public ServerHandshake(byte[] bytes) throws Exception {
        this.readBytes(bytes);
    }

    public void readBytes(byte[] bytes) {
        BytesReader reader = new BytesReader(bytes);

        // 1. 1              protocol version
        this.protocolVersion = reader.readInt(1);

        // 2. string[NUL]    server version
        this.serverVersion = reader.readStringUntilZero();

        // 3. 4              connection id
        this.connectionId = reader.readLong(4);

        // 4. string[8]      auth-plugin-data-part-1
        String authPluginDataPart1 = reader.readString(8);

        // 5. [00]           filler
        reader.skip(1);

        // 6. 2              capability flags (lower 2 bytes) #能力标识符低2位
        this.capability = reader.readInt(2);

        // if more data in the packet:
        if (!reader.hasRemain()) {
            return;
        }

        // 7.              character set					    #采用的编码集
        this.charSet = reader.readInt(1);

        // 8.              status flags						    #状态标识符
        this.statusFlag = reader.readInt(2);

        // 9.              capability flags (upper 2 bytes)     #扩展服务端能力
        int capabilityUpFags = reader.readInt(2) << 15;

        int authPluginDataLength = 0;

        if (((capabilityUpFags & CapabilityFlags.CLIENT_PLUGIN_AUTH)) > 0) {
            authPluginDataLength = reader.readInt(1);
        } else {
            reader.skip(1);
        }

        reader.skip(10);

        if ((capability & CapabilityFlags.CLIENT_SECURE_CONNECTION) > 0) {
            String authPluginDataPart2 = reader.readStringUntilZero();
            this.salt = authPluginDataPart1 + authPluginDataPart2;
        }

        if (((capabilityUpFags & CapabilityFlags.CLIENT_PLUGIN_AUTH)) > 0) {
            this.authPluginName = reader.readStringUntilZero();
        }
    }


    @Override
    public String toString() {
        return "Handshake{\n" +
                "protocolVersion=" + protocolVersion +
                "\n serverVersion='" + serverVersion + '\'' +
                "\n connectionId=" + connectionId +
                "\n charSet=" + charSet +
                "\n statusFlag=" + statusFlag +
                "\n capability=" + capability +
                "\n salt =" + salt +
                "\n authPluginName='" + authPluginName + "\'\n" +
                '}';
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(long connectionId) {
        this.connectionId = connectionId;
    }

    public int getCharSet() {
        return charSet;
    }

    public void setCharSet(int charSet) {
        this.charSet = charSet;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public int getCapability() {
        return capability;
    }

    public void setCapability(int capability) {
        this.capability = capability;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }

    public void setAuthPluginName(String authPluginName) {
        this.authPluginName = authPluginName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

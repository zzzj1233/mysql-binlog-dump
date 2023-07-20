package com.zzzj.response;

/**
 * @author zzzj
 * @create 2023-07-19 18:00
 */
public class ServerHandShake {

    private final int protocol;

    private final String version;

    private final int threadId;

    private final int capability;

    private final String authPluginDataPart1;

    private final String authPluginDataPart2;

    private final String authPluginName;

    public ServerHandShake(int protocol, String version, int threadId, int capability, String authPluginDataPart1, String authPluginDataPart2, String authPluginName) {
        this.protocol = protocol;
        this.version = version;
        this.threadId = threadId;
        this.capability = capability;
        this.authPluginDataPart1 = authPluginDataPart1;
        this.authPluginDataPart2 = authPluginDataPart2;
        this.authPluginName = authPluginName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("protocol=").append(protocol).append("\n");
        sb.append("version='").append(version).append('\'').append("\n");
        sb.append("threadId=").append(threadId).append("\n");
        sb.append("capability=").append(capability).append("\n");
        sb.append("authPluginDataPart1='").append(authPluginDataPart1).append('\'').append("\n");
        sb.append("authPluginDataPart2='").append(authPluginDataPart2).append('\'').append("\n");
        sb.append("authPluginName='").append(authPluginName).append('\'').append("\n");
        return sb.toString();
    }

    public int getProtocol() {
        return protocol;
    }

    public String getVersion() {
        return version;
    }

    public int getThreadId() {
        return threadId;
    }

    public int getCapability() {
        return capability;
    }

    public String getAuthPluginDataPart1() {
        return authPluginDataPart1;
    }

    public String getAuthPluginDataPart2() {
        return authPluginDataPart2;
    }

    public String getAuthPluginName() {
        return authPluginName;
    }
}

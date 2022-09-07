package com.github.zzzj1233.constant;

/**
 * @author zzzj
 * @create 2022-09-06 15:45
 */
public class CapabilityFlags {

    /**
     * 使用数据库
     */
    public static final int CLIENT_CONNECT_WITH_DB = 0x00000008;

    /**
     * 使用4.1协议
     */
    public static final int CLIENT_PROTOCOL_41 = 0x00000200;

    /**
     * 升级SSL协议
     */
    public static final int CLIENT_SSL = 0x00000800;


    /**
     * 支持 Authentication::Native41协议
     */
    public static final int CLIENT_SECURE_CONNECTION = 0x00008000;

    /**
     * <a href="https://dev.mysql.com/doc/internals/en/secure-password-authentication.html">使用security密码</a>
     */
    public static final int CLIENT_LONG_PASSWORD = 0x00000001;


    /**
     * 请求所有column的flag
     * <pre>
     *   if capabilities & CLIENT_LONG_FLAG {
     *              lenenc_int     [03] length of flags+decimals fields
     *              2              flags
     *              1              decimals
     *   } else {
     *              1              [02] length of flags+decimals fields
     *              1              flags
     *              1              decimals
     *   }
     * </pre>
     */
    public static final int CLIENT_LONG_FLAG = 0x00000004;

    /**
     * 启用交互式链接
     */
    public static final int CLIENT_INTERACTIVE = 0x00000400;

    /**
     * 期望EOF_Packet返回状态标志位
     */
    public static final int CLIENT_DEPRECATE_EOF = 0x01000000;

    /**
     * 支持认证插件
     */
    public static final int CLIENT_PLUGIN_AUTH = 0x00080000;


}

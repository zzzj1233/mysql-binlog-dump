package com.zzzj.command;

import com.zzzj.utils.BytesWriter;

/**
 * @author zzzj
 * @create 2023-07-20 14:43
 */
public class BinlogDumpRequest implements Command {

    /// 指定为非阻塞模式, 可以在主库没有新的二进制日志数据时立即返回
    public static final int BINLOG_DUMP_NON_BLOCK = 1;

    // 指定为读取到指定位置后停止, 不再继续读取后续的二进制日志数据
    public static final int BINLOG_THROUGH_POSITION = 2;

    // 指定为读取到指定 GTID 后停止, 不再继续读取后续的二进制日志数据
    public static final int BINLOG_THROUGH_GTID = 3;

    public static final int COM_BINLOG_DUMP = 0x12;

    public static final int FLAGS = 0;

    private final byte[] bytes;

    public BinlogDumpRequest(int position, int serverId, String binlogFileName) {

        BytesWriter writer = new BytesWriter();
        writer.writeInt(COM_BINLOG_DUMP, 1);
        writer.writeInt(position, 4);
        writer.writeInt(FLAGS, 2);
        writer.writeInt(serverId, 4);
        writer.writeEOFString(binlogFileName);

        bytes = writer.toBytes();
    }

    @Override
    public byte[] toBytes() {
        return bytes;
    }

}

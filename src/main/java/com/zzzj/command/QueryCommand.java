package com.zzzj.command;

import com.zzzj.utils.BytesWriter;


/**
 * @author zzzj
 * @create 2023-07-20 11:35
 */
public class QueryCommand implements Command {

    public static final int COM_QUERY = 0x03;

    private final byte[] bytes;

    public QueryCommand(String queryString) {

        BytesWriter writer = new BytesWriter();
        writer.writeInt(COM_QUERY, 1);
        writer.writeEOFString(queryString);

        bytes = writer.toBytes();
    }

    @Override
    public byte[] toBytes() {
        return bytes;
    }

}

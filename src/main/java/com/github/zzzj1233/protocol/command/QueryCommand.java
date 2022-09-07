package com.github.zzzj1233.protocol.command;

import com.github.zzzj1233.utils.BytesWriter;

import java.io.IOException;

/**
 * <pre>
 *  https://dev.mysql.com/doc/internals/en/com-query.html
 *  1              [03] COM_QUERY
 *  string[EOF]    the query the server shall execute
 * </pre>
 * <p>
 *
 * @author zzzj
 * @create 2022-09-07 10:45
 */
public class QueryCommand implements Command {

    private static final int QUERY_TYPE = 3;

    private final String querySql;

    public QueryCommand(String querySql) {
        this.querySql = querySql;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        BytesWriter writer = new BytesWriter();

        writer.writeInt(QUERY_TYPE, 1);

        writer.writeString(querySql);

        return writer.getBytes();
    }

}

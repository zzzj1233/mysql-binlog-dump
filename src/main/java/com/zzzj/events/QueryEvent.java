package com.zzzj.events;

import com.zzzj.protocol.Reader;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2023-07-20 16:51
 */
public class QueryEvent {

    private final int threadId;

    private final int duration;

    private final int errorCode;

    private final String database;

    private final String queryString;

    // QueryEvent源码定义
    // https://dev.mysql.com/doc/dev/mysql-server/latest/classbinary__log_1_1Query__event.html
    public QueryEvent(byte[] bytes) throws IOException {

        Reader reader = new Reader(bytes);

        threadId = reader.readInt4();

        duration = reader.readInt4();

        // skip database name
        reader.skip(1);

        errorCode = reader.readInt2();

        // skip status
        reader.skip(reader.readInt2());

        this.database = reader.readNullEndString();

        this.queryString = reader.readEOFString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QueryEvent{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", database='").append(database).append('\'');
        sb.append(", queryString='").append(queryString).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package com.github.zzzj1233;

import com.github.zzzj1233.protocol.ResultSet;
import com.github.zzzj1233.protocol.command.QueryCommand;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2022-09-07 10:47
 */
public class BinlogFetcher {

    private final String QUERY_SQL = "show master status";

    private String binlogFileName;

    private long binlogPosition;

    public BinlogFetcher(Protocol41Channel channel) throws IOException {
        this.fetch(channel);
    }

    private void fetch(Protocol41Channel channel) throws IOException {
        QueryCommand queryCommand = new QueryCommand(QUERY_SQL);

        channel.write(queryCommand);

        ResultSet resultSet = channel.readResultSet();

        this.binlogFileName = resultSet.getValues().get(0).get(0);
        this.binlogPosition = Long.parseLong(resultSet.getValues().get(0).get(1));
    }

    public void dump() {

    }
}

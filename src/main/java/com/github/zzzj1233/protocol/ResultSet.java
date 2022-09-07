package com.github.zzzj1233.protocol;

import java.util.List;

/**
 * https://dev.mysql.com/doc/internals/en/protocoltext-resultset.html
 *
 * @author zzzj
 * @create 2022-09-07 12:08
 */
public class ResultSet {

    private final List<String> columns;

    private final List<List<String>> values;

    public ResultSet(List<String> columns, List<List<String>> values) {
        this.columns = columns;
        this.values = values;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<List<String>> getValues() {
        return values;
    }
}

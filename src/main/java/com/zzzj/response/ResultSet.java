package com.zzzj.response;

import com.zzzj.command.ProtocolPacket;
import com.zzzj.protocol.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzzj
 * @create 2023-07-20 12:08
 */
public class ResultSet {

    public final Reader reader;

    private ColumnDefinition[] columnDefinitions;

    private List<Object[]> rowValues;

    public ResultSet(Reader reader) throws IOException {

        this.reader = reader;

        this.read();
    }

    protected void read() throws IOException {

        // 1. column count
        ProtocolPacket packet = reader.readPacket();

        int columnCount = readColumnCount(packet.getPayload());

        // 2. column definition
        this.columnDefinitions = new ColumnDefinition[columnCount];

        for (int i = 0; i < columnCount; i++) {
            columnDefinitions[i] = new ColumnDefinition(reader.readPacket().getPayload());
        }

        // 3. EOF
        reader.readEOFPacket();

        // 4. 看看是rowPacket还是EOFPacket
        this.rowValues = new ArrayList<>();

        while (reader.available() > 9) {

            ProtocolPacket rowPacket = reader.readPacket();

            Reader reader = new Reader(rowPacket.getPayload());

            Object[] rowValue = new Object[columnCount];

            for (int i = 0; i < columnCount; i++)
                rowValue[i] = reader.readLengthString();

            rowValues.add(rowValue);
        }

        // 5. EOF
        reader.readEOFPacket();

    }

    public static int readColumnCount(byte[] payload) throws IOException {
        return new Reader(payload).readLengthInt();
    }

    public static class ColumnDefinition {

        public final String def;

        public final String schema;

        public final String table;

        public final String orgTable;

        public final String name;

        public final String orgName;

        public final int fieldLength;

        public final int charset;

        public final int columnLength;

        public final int type;

        public final int flags;

        public final int decimals;

        public ColumnDefinition(byte[] payload) throws IOException {

            Reader reader = new Reader(payload);

            this.def = reader.readLengthString();

            this.schema = reader.readLengthString();

            this.table = reader.readLengthString();

            this.orgTable = reader.readLengthString();

            this.name = reader.readLengthString();

            this.orgName = reader.readLengthString();

            this.fieldLength = reader.readLengthInt();

            this.charset = reader.readInt2();

            this.columnLength = reader.readInt4();

            this.type = reader.readInt1();

            this.flags = reader.readInt2();

            this.decimals = reader.readInt1();

        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("ColumnDefinition{");
            sb.append("def='").append(def).append('\'');
            sb.append(", schema='").append(schema).append('\'');
            sb.append(", table='").append(table).append('\'');
            sb.append(", orgTable='").append(orgTable).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append(", orgName='").append(orgName).append('\'');
            sb.append(", fieldLength=").append(fieldLength);
            sb.append(", charset=").append(charset);
            sb.append(", columnLength=").append(columnLength);
            sb.append(", type=").append(type);
            sb.append(", flags=").append(flags);
            sb.append(", decimals=").append(decimals);
            sb.append('}');
            return sb.toString();
        }
    }

    public ColumnDefinition[] getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setColumnDefinitions(ColumnDefinition[] columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public List<Object[]> getRowValues() {
        return rowValues;
    }

    public void setRowValues(List<Object[]> rowValues) {
        this.rowValues = rowValues;
    }
}

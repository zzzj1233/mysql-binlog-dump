package com.zzzj.events;

public class BinlogEventType {

    public static final int FORMAT_DESCRIPTION_EVENT = 0x0f;

    public static final int QUERY_EVENT = 0x02;

    public static final int XID_EVENT = 0x12;

    public static final int TABLE_MAP_EVENT = 0x13;

    public static final int WRITE_ROWS_EVENT = 0x16;

    public static final int UPDATE_ROWS_EVENT = 0x17;

    public static final int DELETE_ROWS_EVENT = 0x19;

    public static final int ROTATE_EVENT = 0x04;

    public static final int ANNOTATE_ROWS_EVENT = 0xa3;

    public static final int GTID_EVENT = 0x1e;

    public static final int BEGIN_LOAD_QUERY_EVENT = 0x1c;

    public static final int EXECUTE_LOAD_QUERY_EVENT = 0x1d;

    public static final int INCIDENT_EVENT = 0x1a;

    public static final int HEARTBEAT_LOG_EVENT = 0x1f;

    public static final int GARBAGE_COLLECTION_EVENT = 0xa4;

    public static final int TRANSACTION_CONTEXT_EVENT = 0x22;

    public static final int VIEW_CHANGE_EVENT = 0xfc;

    public static final int XA_PREPARE_LOG_EVENT = 0x18;

    public static final int TRANSACTION_PAYLOAD_EVENT = 0x23;

}

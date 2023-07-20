package com.zzzj.events;

import java.util.HashMap;
import java.util.Map;

import static com.zzzj.events.BinlogEventType.*;

public abstract class BinlogEvent {

    public BinlogEvent(byte[] bodyBytes) {

    }

    public static final Map<Integer, Class<? extends BinlogEvent>> EVENT_CLASS_MAP;

    static {
        EVENT_CLASS_MAP = new HashMap<>();
        EVENT_CLASS_MAP.put(ROTATE_EVENT, RotateEvent.class);
        EVENT_CLASS_MAP.put(QUERY_EVENT, QueryEvent.class);
        EVENT_CLASS_MAP.put(FORMAT_DESCRIPTION_EVENT, FormatDescriptionEvent.class);
    }
}

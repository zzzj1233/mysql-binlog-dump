package com.zzzj;

import com.zzzj.command.ProtocolPacket;
import com.zzzj.events.BinlogEventHeader;
import com.zzzj.events.QueryEvent;
import com.zzzj.protocol.Reader;

/**
 * @author zzzj
 * @create 2023-07-20 16:24
 */
public class BinlogParseTest {

    public static void main(String[] args) throws Exception {
        String raw = "89000005006debb864020100000088000000c804000000000a000000000000000500002e000000000000012000a04500000000060373746404ff00ff00c0000901000000000000000c0163616e616c0012ff0063616e616c00555044415445206063616e616c602e60757365726020534554206061676560203d202735272057484552452060696460203d2033";

        byte[] bytes = hexToByte(raw);

        ProtocolPacket protocolPacket = new Reader(bytes).readPacket();

        Reader reader = new Reader(protocolPacket.getPayload());

        // EOF and skip OK
        if (reader.readInt1() == 0XFE) {

        }

        BinlogEventHeader eventHeader = new BinlogEventHeader(reader);

        System.out.println(new QueryEvent(reader.readRemain()));
    }

    public static byte[] hexToByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }


}

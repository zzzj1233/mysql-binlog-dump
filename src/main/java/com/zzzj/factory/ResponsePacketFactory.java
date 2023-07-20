package com.zzzj.factory;

import com.zzzj.command.*;
import com.zzzj.protocol.Reader;

import java.io.IOException;

/**
 * @author zzzj
 * @create 2023-07-20 11:14
 */
public class ResponsePacketFactory {

    public static ResponsePacket readPacket(int capability, ProtocolPacket protocolPacket) throws IOException {

        Reader reader = new Reader(protocolPacket.getPayload());

        int mark = reader.readInt1();

        if (mark == 0) return new OkPacket(capability, reader);

        else if ((mark & 0xFF) == 0XFF) return new ErrPacket(capability, reader);

        else if ((mark & 0xFE) == 0XFE)
            if (protocolPacket.getPayload().length <= 9)
                return new EofPacket(capability, reader);
            else
                return new OkPacket(capability, reader);

        else throw new IllegalArgumentException("Illegal mark : " + mark);
    }

}

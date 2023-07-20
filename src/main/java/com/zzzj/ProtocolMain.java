package com.zzzj;

import com.zzzj.command.*;
import com.zzzj.events.BinlogEvent;
import com.zzzj.events.BinlogEventHeader;
import com.zzzj.exception.UnExpectErrPacketException;
import com.zzzj.protocol.ProtocolWriter;
import com.zzzj.protocol.Reader;
import com.zzzj.response.ResultSet;
import com.zzzj.response.ServerHandShake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zzzj
 * @create 2023-07-19 18:08
 */
public class ProtocolMain {

    private static final Logger log = LoggerFactory.getLogger(ProtocolMain.class);

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket();

        socket.connect(new InetSocketAddress("localhost", 3306));

        ProtocolWriter protocolWriter = new ProtocolWriter(socket.getOutputStream());

        Reader reader = new Reader(socket.getInputStream());

        ProtocolPacket requestProtocolPacket = reader.readPacket();

        ServerHandShake serverHandShake = new Reader(requestProtocolPacket.getPayload()).readHandShake();

        ClientHandShake response = new ClientHandShake("root", "123456", serverHandShake.getAuthPluginDataPart1() + serverHandShake.getAuthPluginDataPart2(), requestProtocolPacket.getSeq());

        protocolWriter.writeAndFlush(response);

        reader.readResponsePacket();

        protocolWriter.writeAndFlush(new QueryCommand("show master status;"));

        ResultSet resultSet = new ResultSet(reader);

        String binlogFileName = resultSet.getRowValues().get(0)[0].toString();

        Integer position = Integer.parseInt(resultSet.getRowValues().get(0)[1].toString());

        log.debug("position = {} ", position);

        protocolWriter.writeAndFlush(new QueryCommand("SELECT @@server_id;"));

        resultSet = new ResultSet(reader);

        Integer serverId = Integer.parseInt(resultSet.getRowValues().get(0)[0].toString());

        log.debug("serverId = {} ", serverId);

        // 发送COM_BINLOG_DUMP命令
        BinlogDumpRequest request = new BinlogDumpRequest(position, serverId, binlogFileName);

        protocolWriter.writeAndFlush(request);

        while (true) {

            int payloadLength = reader.readInt(3);

            // skip seq
            reader.skip(1);

            int packetType = reader.readInt1();

            // error
            if ((packetType & 0xFF) == 0xFF) {
                throw new UnExpectErrPacketException(new ErrPacket(ClientHandShake.CLIENT_CAPABILITY, reader));
            } else if ((packetType & 0XFE) == 0XFE && payloadLength <= 9) { // EOF
                log.info("Receive EOF packet , Stop listening for binlog events");
                break;
            }

            // ReadEventHeader
            BinlogEventHeader eventHeader = new BinlogEventHeader(reader);

            Class<? extends BinlogEvent> eventClass = BinlogEvent.EVENT_CLASS_MAP.get(eventHeader.getEventType());

            byte[] bodyBytes = reader.readBytes(eventHeader.getBodySize());

            if (eventClass == null) {
                log.info("Not support event type :  0X{} ", Integer.toHexString(eventHeader.getEventType()));

                continue;
            }

            BinlogEvent binlogEvent = eventClass.getConstructor(byte[].class).newInstance((Object) bodyBytes);

            log.info("Listening for a new event :  {} ", binlogEvent);

            // 定时发送心跳
            protocolWriter.writeAndFlush(new Ping());
        }

        socket.close();
    }

}

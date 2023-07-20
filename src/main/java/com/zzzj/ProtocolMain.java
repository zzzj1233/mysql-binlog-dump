package com.zzzj;

import com.zzzj.command.BinlogDumpRequest;
import com.zzzj.command.ClientHandShake;
import com.zzzj.command.ProtocolPacket;
import com.zzzj.command.QueryCommand;
import com.zzzj.protocol.ProtocolWriter;
import com.zzzj.protocol.Reader;
import com.zzzj.response.ResultSet;
import com.zzzj.response.ServerHandShake;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zzzj
 * @create 2023-07-19 18:08
 */
public class ProtocolMain {


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

        System.out.println("position = " + position);

        protocolWriter.writeAndFlush(new QueryCommand("SELECT @@server_id;"));

        resultSet = new ResultSet(reader);

        Integer serverId = Integer.parseInt(resultSet.getRowValues().get(0)[0].toString());

        System.out.println("serverId = " + serverId);

        // 发送COM_BINLOG_DUMP命令
        BinlogDumpRequest request = new BinlogDumpRequest(position, serverId, binlogFileName);

        protocolWriter.writeAndFlush(request);

        System.in.read();

        socket.close();
    }

}

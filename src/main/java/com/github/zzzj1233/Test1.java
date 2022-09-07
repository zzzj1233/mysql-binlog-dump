package com.github.zzzj1233;

import com.github.zzzj1233.protocol.ServerHandshake;
import com.github.zzzj1233.protocol.command.ClientHandshake;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zzzj
 * @create 2022-09-05 16:19
 */
public class Test1 {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();

        socket.connect(new InetSocketAddress("localhost", 3306));

        Protocol41Channel channel = new Protocol41Channel(socket);

        // 1. 接收握手包
        ServerHandshake handshake = new ServerHandshake(channel.read());

        // 2. 发送认证包
        ClientHandshake clientHandshake = new ClientHandshake("root", "root", handshake, channel);

        clientHandshake.sendHandShake();

        // 3. 获取binlog的filename和position : 'show master status'
        BinlogFetcher binlogFetcher = new BinlogFetcher(channel);

        // 4. 发送dump请求

        socket.close();
    }


}

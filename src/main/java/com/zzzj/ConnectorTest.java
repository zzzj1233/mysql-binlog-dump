package com.zzzj;

import com.github.shyiko.mysql.binlog.BinaryLogClient;

/**
 * @author zzzj
 * @create 2023-07-20 15:43
 */
public class ConnectorTest {

    public static void main(String[] args) throws Exception {

        BinaryLogClient binaryLogClient = new BinaryLogClient("root", "123456");

        binaryLogClient.registerEventListener(System.out::println);

        binaryLogClient.connect();

        System.in.read();

        binaryLogClient.disconnect();

    }

}

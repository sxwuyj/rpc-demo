package com.abc.server;

/**
 * Created by v_yanjunwu on 2020/4/15
 */
public class PrcServerStart {
    public static void main(String[] args) throws Exception {

        RpcServer server = new RpcServer();
        server.publish("com.abc.service");
        server.start();
    }
}

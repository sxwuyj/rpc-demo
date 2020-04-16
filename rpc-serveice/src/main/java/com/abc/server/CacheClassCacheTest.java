package com.abc.server;

/**
 * Created by v_yanjunwu on 2020/4/13
 */
public class CacheClassCacheTest {
    public static void main(String[] args) throws Exception {
        new RpcServer().publish("com.abc.service");
    }
}

package com.abc.service;

/**
 * Created by v_yanjunwu on 2020/4/13
 */
public class someServerImpl implements SomeService {
    @Override
    public String hello(String name) {
        return name + "欢迎来到德莱联盟";
    }
}

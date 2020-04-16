package com.abc;

import com.abc.client.RpcProxy;
import com.abc.service.SomeService;

/**
 * Created by v_yanjunwu on 2020/4/13
 */
public class RpcConsumer {
    public static void main(String[] args) {
            SomeService service = RpcProxy.create(SomeService.class);
            System.out.println(service.hello("kkb"));
    }
}

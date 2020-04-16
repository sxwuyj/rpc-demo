package com.abc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by v_yanjunwu on 2020/4/15
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {
    private Object result;
    public Object getResult() {
        return result;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        this.result = msg;
    }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }


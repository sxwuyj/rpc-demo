package com.abc.server;

import com.abc.dto.Invocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * Created by v_yanjunwu on 2020/4/13
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<Invocation> {

    private Map<String, Object> registerMap;

    public RpcServerHandler(Map<String, Object> registerMap) {
        this.registerMap = registerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Invocation msg) throws Exception {
        Object result = "提供者没有该方法";
        if (registerMap.containsKey(msg.getClassName())){
            Object invoker = registerMap.get(msg.getClassName());
            result =  invoker.getClass().getMethod(msg.getMethodName(),msg.getParamTypes()).invoke(invoker,msg.getParamValues());
        }
        channelHandlerContext.writeAndFlush(result);
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

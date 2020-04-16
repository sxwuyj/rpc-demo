package com.abc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import sun.dc.pr.PRError;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v_yanjunwu on 2020/4/13
 */
public class RpcServer  {

    //注册表
    private Map registerMap =  new HashMap<String,Object>();
    //存放指定包实现的业务接口名
    private List<String> classCache = new ArrayList<>();

    /**
     * 发布服务
     * @param basePackage   指定包路径
     */
    public void publish(String basePackage) throws Exception {
        cacheClassCache(basePackage);

        doRegister();
    }

    /**
     * 将指定包实现的业务接口名存放进classCache
     * @param basePackage   指定包路径
     */
    private void cacheClassCache(String basePackage) {
        //获取URL中的资源
        URL resource = this.getClass().getClassLoader()
                .getResource(basePackage.replaceAll("\\.", "/"));
        //如果目录为空,直接返回
        if (resource == null) {
            return;
        }
        File dir = new File(resource.getFile());
        //遍历目录
        for (File file : dir.listFiles()) {
            //如果是目录,递归
            if (file.isDirectory()) {
                cacheClassCache(basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                //去掉.class后缀写入classCache
                String fileName = file.getName().replace(".class", "");
                classCache.add(basePackage + "." + fileName);
                System.out.println(classCache);
            }
        }
    }

    /**
     * 将指定包中的业务接口实现类实例写入到注册表
     */
    private void doRegister() throws Exception {
        //如果classCache为空.说明指定包下没有实现类
        if (classCache.size() == 0) {
            return;
        }
        for (String fileName : classCache) {
            //加载实现类到内存中
            Class<?> clazz = Class.forName(fileName);
            registerMap.put(clazz.getInterfaces()[0].getName(),clazz.newInstance());
        }

    }

    public void start() throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup,childGroup)
                     .option(ChannelOption.SO_BACKLOG,1024)
                     .childOption(ChannelOption.SO_KEEPALIVE,true)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                                 ChannelPipeline pipeline = ch.pipeline();
                                 pipeline.addLast(new ObjectEncoder());
                                 pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                         ClassResolvers.cacheDisabled(null)));
                                 pipeline.addLast(new RpcServerHandler(registerMap));
                         }
                     });
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务端已启动,监听端口为:8888");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}

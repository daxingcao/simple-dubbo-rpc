package com.caodaxing.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description
 */
@Slf4j
public class NettyServerAndClientFactory extends NettyConfigFactory implements BaseNettyClient,BaseNettyServer {

    @Override
    public void send(String host, int port) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(ClassLoader.getSystemClassLoader())))
                                    .addLast(new IdleStateHandler(0,10,0));
                            if(getClientHandler() != null){
                                socketChannel.pipeline().addLast(getClientHandler());
                            }
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY,true);
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void start(int port) {
        ServerBootstrap server = new ServerBootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            if (port == 0) {
                throw new RuntimeException("未设置端口!");
            }
            server.group(group, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(ClassLoader.getSystemClassLoader())));
                            if(getServerHandler() != null){
                                channel.pipeline().addLast(getServerHandler());
                            }
                        }
                    })
                    //ServerSocketChannel参数设置，设置socket缓存大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //SocketChannel参数设置，开启心跳检测
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //关闭Nagle算法，保持数据实时发送
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //开启服务器共享端口
                    .childOption(ChannelOption.SO_REUSEADDR, true);
            ChannelFuture future = server.bind(port).sync();
            if(log.isInfoEnabled()){
                log.info("service port '{}' opened successfully.",port);
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

package com.caodaxing.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description Channel基础实现类
 */
public abstract class BaseChannel extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(ClassLoader.getSystemClassLoader())))
                .addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
        if(this.getHandler() != null){
            channel.pipeline().addLast(this.getHandler());
        }
    }

    /**
     * 获取ChannelHandler对象，由子类实现
     * @return
     */
    public abstract ChannelHandler getHandler();

}

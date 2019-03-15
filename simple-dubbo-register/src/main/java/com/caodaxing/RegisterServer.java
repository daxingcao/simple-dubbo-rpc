package com.caodaxing;

import com.caodaxing.netty.NettyServerAndClientFactory;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 注册中心服务启动实现类
 */
@Slf4j
public class RegisterServer extends NettyServerAndClientFactory {

    private ChannelHandler channelHandler;

    public RegisterServer(ChannelHandler handler){
        this.channelHandler = handler;
    }

    @Override
    public void start(int port) {
        log.info("注册中心服务开启中...");
        setServerHandler(this.channelHandler);
        super.start(port);
    }

}

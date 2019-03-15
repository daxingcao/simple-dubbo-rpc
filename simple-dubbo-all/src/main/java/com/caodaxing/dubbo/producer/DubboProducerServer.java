package com.caodaxing.dubbo.producer;

import com.caodaxing.netty.NettyServerAndClientFactory;
import io.netty.channel.ChannelHandler;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description dubbo生产者服务端实现类
 */
public class DubboProducerServer extends NettyServerAndClientFactory {

    private ChannelHandler handler;

    public DubboProducerServer(ChannelHandler handler){
        this.handler = handler;
    }

    @Override
    public void start(int port) {
        setServerHandler(this.handler);
        super.start(port);
    }

}

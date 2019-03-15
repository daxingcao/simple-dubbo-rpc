package com.caodaxing.dubbo.producer;

import com.caodaxing.netty.NettyServerAndClientFactory;
import io.netty.channel.ChannelHandler;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description dubbo生产者客户端实现类
 */
public class DubboProducerClient extends NettyServerAndClientFactory {

    private ChannelHandler handler;

    public DubboProducerClient(ChannelHandler handler){
        this.handler = handler;
    }

    @Override
    public void send(String host, int port) {
        setClientHandler(this.handler);
        super.send(host,port);
    }

}

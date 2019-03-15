package com.caodaxing.dubbo.customer;

import com.caodaxing.netty.NettyServerAndClientFactory;
import io.netty.channel.ChannelHandler;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description dubbo消费者客户端实现类
 */
public class DubboCustomerClient extends NettyServerAndClientFactory {

    private ChannelHandler handler;

    public DubboCustomerClient(ChannelHandler handler) {
        this.handler = handler;
    }

    @Override
    public void send(String host, int port) {
        setClientHandler(this.handler);
        super.send(host,port);
    }

}

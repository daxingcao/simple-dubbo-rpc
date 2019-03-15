package com.caodaxing.netty;

import io.netty.channel.ChannelHandler;

public class NettyConfigFactory {

    private ChannelHandler serverHandler;
    private ChannelHandler clientHandler;

    public ChannelHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ChannelHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public ChannelHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(ChannelHandler clientHandler) {
        this.clientHandler = clientHandler;
    }
}

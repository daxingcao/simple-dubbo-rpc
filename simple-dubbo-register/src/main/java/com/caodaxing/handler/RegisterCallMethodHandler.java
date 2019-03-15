package com.caodaxing.handler;

import com.caodaxing.common.MessageType;
import com.caodaxing.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CountDownLatch;

public class RegisterCallMethodHandler extends ChannelInboundHandlerAdapter {

    private Message message;
    private Object result;
    private CountDownLatch count;

    public RegisterCallMethodHandler(Message message){
        this.message = message;
        this.count = new CountDownLatch(1);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(this.message == null){
            throw new NullPointerException("Message");
        }
        ctx.writeAndFlush(this.message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Message){
            Message message = (Message) msg;
            if(!MessageType.METHOD_RESULT.equals(message.getType())){
                return;
            }
            this.result = message.getResult();
            count.countDown();
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public Object getResult(){
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.result;
    }
}

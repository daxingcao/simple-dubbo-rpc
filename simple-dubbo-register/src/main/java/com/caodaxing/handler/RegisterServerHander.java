package com.caodaxing.handler;

import com.caodaxing.common.MessageType;
import com.caodaxing.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 注册中心主要业务逻辑类，包过生产者注册方法，消费者调用方法，都是在这里做中转；
 */
@Slf4j
@ChannelHandler.Sharable
public class RegisterServerHander extends BaseRegisterHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}建立连接...",ctx.toString());
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            switch (message.getType()) {
                case REGISTER:
                    this.registerMethod(message,ctx);
                    ctx.writeAndFlush(message);
                    break;
                case CALL_METHOD:
                    try{
                        Object result = this.callMethod(message);
                        message.setResult(result);
                        message.setType(MessageType.METHOD_RESULT);
                        ctx.writeAndFlush(message);
                    }catch (RuntimeException e){
                        ctx.writeAndFlush(e);
                    }
                    break;
                default:
                    this.lookRegisterControl();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}

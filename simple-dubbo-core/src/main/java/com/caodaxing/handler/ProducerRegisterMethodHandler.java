package com.caodaxing.handler;

import com.caodaxing.common.MessageType;
import com.caodaxing.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 生产者客户端业务类
 */
public class ProducerRegisterMethodHandler extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(ProducerRegisterMethodHandler.class);

    private Class loader;
    private Method method;
    /**
     * 服务提供者IP地址
     */
    private String host;
    /**
     * 服务提供者暴露端口
     */
    private int port;

    public ProducerRegisterMethodHandler(Class loader, Method method, String host, int port){
        this.loader = loader;
        this.method = method;
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if(loader != null && method != null){
            Class intClass = loader.getInterfaces()[0];
            Message msg = Message.builder().methodName(method.getName()).className(loader.getName())
                    .intsName(intClass.getName()).host(this.host).port(this.port).type(MessageType.REGISTER).build();
            ctx.writeAndFlush(msg);
        }else {
            ctx.channel().close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof Message){
            Message message = (Message) msg;
            if(log.isInfoEnabled()){
                log.info("{}#{}方法注册成功！",message.getIntsName(),message.getMethodName());
            }
        }
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("注册中心未启动！",cause);
        ctx.channel().close();
    }

}

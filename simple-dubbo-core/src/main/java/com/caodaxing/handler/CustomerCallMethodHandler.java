package com.caodaxing.handler;

import com.alibaba.fastjson.JSON;
import com.caodaxing.common.ClassUtils;
import com.caodaxing.common.MessageType;
import com.caodaxing.message.HandlerInfo;
import com.caodaxing.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 消费者客户端handler
 */
@Slf4j
@ChannelHandler.Sharable
public class CustomerCallMethodHandler extends ChannelInboundHandlerAdapter {

    private HandlerInfo info;
    private Object result;
    private Object msg;
    private CountDownLatch countDownLatch;

    public CustomerCallMethodHandler(HandlerInfo info){
        if(info == null || info.getMethod() == null){
            throw new RuntimeException("初始化参数不能为空!");
        }
        this.info = info;
        this.countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Method method = info.getMethod();
        //由于注册中心没有传输的参数对象，会在解码时报NotFoundClassException异常，
        //因此采用转字符串方式，在提供方进行对应的解析
        String args = JSON.toJSONString(info.getArgs());
        Message msg = Message.builder().arguments(args).intsName(method.getDeclaringClass().getName())
                .argumentTypes(ClassUtils.classToString(method.getParameterTypes())).type(MessageType.CALL_METHOD)
                .methodName(method.getName()).build();
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        this.msg = msg;
        if(msg instanceof Message){
            Message message = (Message) msg;
            this.result = message.getResult();
            this.countDownLatch.countDown();
        }else if (msg instanceof RuntimeException) {
            RuntimeException e = (RuntimeException) msg;
            log.error(e.getMessage(),e);
        }else {
            log.info(msg.toString());
        }
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx,cause);
    }

    public Object getResult()throws Throwable{
        if(!(msg instanceof Message)){
            throw new ReflectiveOperationException("method.invoke() failed!");
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.result;
    }

}

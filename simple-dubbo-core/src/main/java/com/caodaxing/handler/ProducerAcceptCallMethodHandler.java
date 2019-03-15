package com.caodaxing.handler;

import com.alibaba.fastjson.JSON;
import com.caodaxing.common.ClassUtils;
import com.caodaxing.common.MessageType;
import com.caodaxing.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class ProducerAcceptCallMethodHandler extends ChannelInboundHandlerAdapter {

    private BeanFactory beanFactory;

    public ProducerAcceptCallMethodHandler(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Message){
            Message message = (Message) msg;
            Class<?> loadClass = Class.forName(message.getClassName());
            Class[] classes = ClassUtils.StringToClass(message.getArgumentTypes());
            Method method = loadClass.getMethod(message.getMethodName(), classes);
            Object bean = beanFactory.getBean(loadClass);
            if(bean == null){
                log.error("没有找到实现类！", new ClassNotFoundException());
                throw new ClassNotFoundException(message.getClassName());
            }
            //对消费者传过来的参数进行解析
            List<Object> json = JSON.parseArray(message.getArguments(), classes);
            Object[] arguments = json.stream().toArray(Object[]::new);
            //利用反射调用方法
            Object invoke = method.invoke(bean, arguments);
            message.setResult(invoke);
            message.setType(MessageType.METHOD_RESULT);
            //返回方法的返回结果给消费端
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}

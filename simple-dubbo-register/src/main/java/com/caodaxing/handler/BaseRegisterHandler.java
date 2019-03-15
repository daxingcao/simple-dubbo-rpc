package com.caodaxing.handler;

import com.caodaxing.context.RegisterContext;
import com.caodaxing.message.Message;
import com.caodaxing.netty.NettyServerAndClientFactory;
import com.caodaxing.netty.Producer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @decription 注册基础实现handler类
 */
@Slf4j
public abstract class BaseRegisterHandler extends ChannelInboundHandlerAdapter implements Producer {

    private volatile RegisterContext context;

    public BaseRegisterHandler(){
        this.context = new RegisterContext();
    }

    @Override
    public Object callMethod(Message msg)throws RuntimeException {
        Map<String, List<Message>> registerMethods = context.getRegisterMethods();
        String key = generateMethodKey(msg);
        List<Message> messages = registerMethods.get(key);
        if(messages == null || messages.isEmpty()){
            if(log.isInfoEnabled()){
                log.info("{} method has no producer!",key);
            }
            throw new RuntimeException("没有提供者！");
        }
        //TODO 留待以后做负载均衡，或者调用特定提供者
        Message message = messages.get(0);
        msg.setClassName(message.getClassName());
        msg.setHost(message.getHost());
        msg.setPort(message.getPort());
        RegisterCallMethodHandler handler = new RegisterCallMethodHandler(msg);
        try {
            NettyServerAndClientFactory factory = new NettyServerAndClientFactory();
            factory.setClientHandler(handler);
            factory.send(message.getHost(),message.getPort());
        } catch (Exception e) {
            throw new RuntimeException("调用提供者接口报错:" + e.getMessage());
        }
        return handler.getResult();
    }

    @Override
    public void registerMethod(Message msg, ChannelHandlerContext ctx) {
        if(msg != null && !StringUtils.isEmpty(msg.getIntsName())){
            String key = generateMethodKey(msg);
            context.registerMethod(key,msg);
            if(log.isInfoEnabled()){
                log.info("成功注册：{}", key);
            }
        }
    }

    public RegisterContext lookRegisterControl(){
        return this.context;
    }

    public RegisterContext getContext(){
        return this.context;
    }

    private String generateMethodKey(Message msg){
        return msg.getIntsName() + "#" + msg.getMethodName();
    }

    public void deleteProducerInfo(ChannelHandlerContext ctx){
        //TODO 删除提供者
    }

}

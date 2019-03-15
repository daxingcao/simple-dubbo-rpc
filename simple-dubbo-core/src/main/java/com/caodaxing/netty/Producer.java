package com.caodaxing.netty;

import com.caodaxing.message.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @decription 生产者接口
 */
public interface Producer {

    /**
     * 调用生产者对应的方法
     * @param msg 调用方法的相关信息
     * @return 方法返回对象
     * @throws RuntimeException 可能出现的异常
     */
    Object callMethod(Message msg) throws RuntimeException;

    /**
     * 生产者注册方法
     * @param msg 注册信息
     * @param ctx 连接通道
     */
    void registerMethod(Message msg, ChannelHandlerContext ctx);

}

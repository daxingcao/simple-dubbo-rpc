package com.caodaxing.netty;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @decription 客户端
 */
public interface Client {

    /**
     * 客户端向服务端发送请求
     * @param host 服务端ip
     * @param port 服务端端口
     */
    void send(String host, int port) throws Exception;

}

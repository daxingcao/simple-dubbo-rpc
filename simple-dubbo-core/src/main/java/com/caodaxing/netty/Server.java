package com.caodaxing.netty;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 服务端
 */
public interface Server {

    /**
     * 开启服务
     * @param port 开启服务所用端口
     */
    void start(int port)throws Exception;

}

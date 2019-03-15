package com.caodaxing;

import com.caodaxing.handler.RegisterServerHander;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 注册中心入口启动类
 */
public class RegisterStart {

    public static void main(String[] args){
        RegisterServerHander hander = new RegisterServerHander();
        new RegisterServer(hander).start(9080);
    }

}

package com.caodaxing.dubbo.customer;

import com.caodaxing.handler.CustomerCallMethodHandler;
import com.caodaxing.message.HandlerInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 消费者接口类jdk动态代理
 */
public class InterfaceClassDynamicProxy implements InvocationHandler {

    private Class target;

    public InterfaceClassDynamicProxy(Class target){
        this.target = target;
    }

    public Object getProxy() throws RuntimeException {
        if (target == null) {
            throw new RuntimeException("the proxy object cannot be empty!");
        }

        return Proxy.newProxyInstance(this.target.getClassLoader(), new Class[]{target}, this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 这里通过远程调用方法返回
        HandlerInfo info = HandlerInfo.builder().method(method).args(args).build();
        CustomerCallMethodHandler handler = new CustomerCallMethodHandler(info);
        new DubboCustomerClient(handler).send("localhost",9080);
        return handler.getResult();
    }

}

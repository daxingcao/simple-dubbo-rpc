package com.caodaxing.context;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 注册中心容器调度器
 */
public interface RegisterContextAware {

    /**
     * 实现该接口类，可以得到RegisterContext容器
     * @param context 容器
     * @return RegisterContext
     */
    RegisterContext setRegisterContext(RegisterContext context);
}

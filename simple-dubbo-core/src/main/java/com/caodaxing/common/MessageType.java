package com.caodaxing.common;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 远程调用消息类型
 */
public enum MessageType {

    /**
     * 注册消息类型
     */
    REGISTER,
    /**
     * 调用方法消息类型
     */
    CALL_METHOD,
    /**
     * 方法返回消息类型
     */
    METHOD_RESULT;

}

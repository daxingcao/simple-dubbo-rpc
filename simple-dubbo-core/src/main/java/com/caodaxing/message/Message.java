package com.caodaxing.message;

import com.caodaxing.common.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 消息类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    /**
     * 调用远程方法返回结果对象
     */
    private Object result;
    /**
     * 消息类型
     */
    private MessageType type;
    /**
     * 方法参数
     */
    private String arguments;
    /**
     * 方法参数类型
     */
    private String[] argumentTypes;
    /**
     * 接口名
     */
    private String intsName;
    /**
     * 接口实现类路径
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 生产者IP地址
     */
    private String host;
    /**
     * 生产者端口号
     */
    private int port;
    /**
     * 是否成功
     */
    private boolean success;

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Message){
            Message msg = (Message) obj;
            if(msg == this){
                return true;
            }
            if(this.host == null){
                if(msg.getHost() == null && this.port == msg.getPort()){
                    return true;
                }
            }else {
                if(this.host.equals(msg.getHost()) && this.port == msg.getPort()){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result*31 + (this.result != null ? this.result.hashCode() : 0);
        result = result*31 + (this.type != null ? this.type.hashCode() : 0);
        result = result*31 + (this.intsName != null ? this.intsName.hashCode() : 0);
        result = result*31 + (this.className != null ? this.className.hashCode() : 0);
        result = result*31 + (this.host != null ? this.host.hashCode() : 0);
        result = result*31 + (this.methodName != null ? this.methodName.hashCode() : 0);
        result = result*31 + this.port;
        result = result*31 + (success ? 1 : 0);
        result = result*31 + Arrays.hashCode(this.argumentTypes);
        result = result*31 + (this.arguments != null ? this.arguments.hashCode() : 0);
        return result;
    }
}

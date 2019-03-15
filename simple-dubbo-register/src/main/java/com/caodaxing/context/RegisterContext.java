package com.caodaxing.context;

import com.caodaxing.message.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 注册中心容器
 */
@Data
public class RegisterContext {

    private volatile Map<String, List<Message>> registerMethods = new ConcurrentHashMap<>();

    public RegisterContext(){}

    public void registerMethod(String key, Message registerInfo){
        List<Message> messages = this.registerMethods.get(key);
        if(messages == null){
            messages = new ArrayList<>();
            this.registerMethods.put(key,messages);
        }
        if (!messages.contains(registerInfo)) {
            messages.add(registerInfo);
        }
    }

}

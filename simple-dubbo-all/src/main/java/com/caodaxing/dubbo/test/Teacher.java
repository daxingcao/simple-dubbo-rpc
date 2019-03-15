package com.caodaxing.dubbo.test;


import com.caodaxing.dubbo.annotation.Producer;
import org.springframework.stereotype.Component;

@Producer
@Component
public class Teacher implements Position {

    @Override
    public String say(Person name) {
        System.out.println("I'm teacher,my name is " + name.getName());
        return "ok";
    }

}

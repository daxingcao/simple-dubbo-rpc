package com.caodaxing.dubbo.test;

import com.caodaxing.dubbo.annotation.DubboWrite;

public class CustomerTest {

    @DubboWrite
    private Position position;

    public void test(){
        Person person = new Person("caodaxing");
        position.say(person);
    }

}

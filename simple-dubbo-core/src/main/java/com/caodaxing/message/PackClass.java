package com.caodaxing.message;

import java.io.Serializable;

public class PackClass<T> implements Serializable {

    public PackClass(T data){
        this.obj = data;
    }

    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}

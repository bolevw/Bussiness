package com.iocm.business.model;

/**
 * Created by liubo on 15/7/21.
 */
public  class NameValue<T> {
    private String name;
    private  T value;

    public NameValue(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

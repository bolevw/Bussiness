package com.iocm.business.model;

import java.io.Serializable;

/**
 * Created by liubo on 4/17/16.
 */
public class MenuModel implements Serializable {

    private String id;
    private String imageSrc;
    private String name;
    private String detail;
    private String function;
    private String money;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "MenuModel{" +
                "id='" + id + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", function='" + function + '\'' +
                '}';
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

package com.iocm.business.model;

/**
 * Created by Administrator on 2016/4/19.
 */

public class OrderItemAVModel  {


    private Integer num;
    private MenuAVModel model;

    public OrderItemAVModel() {
    }


    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public MenuAVModel getModel() {
        return model;
    }

    public void setModel(MenuAVModel model) {
        this.model = model;
    }
}

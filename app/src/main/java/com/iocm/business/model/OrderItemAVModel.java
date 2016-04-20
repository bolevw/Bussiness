package com.iocm.business.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2016/4/19.
 */
@AVClassName("OrderItemAvModel")
public class OrderItemAVModel extends AVObject {


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

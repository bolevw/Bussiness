package com.iocm.business.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
@AVClassName("OrderAvModel")
public class OrderAVModel extends AVObject {
    private String id;
    private String userId;
    private String tableNum;
    private List<OrderItemAVModel> menuList;
    private String username;
    private Integer orderStatus;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItemAVModel> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<OrderItemAVModel> menuList) {
        this.menuList = menuList;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

}

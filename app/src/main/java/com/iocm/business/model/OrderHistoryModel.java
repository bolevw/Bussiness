package com.iocm.business.model;

/**
 * Created by Administrator on 2016/4/21.
 */
public class OrderHistoryModel  {
    private String orderNum;
    private String tablenum;
    private String money;
    private int orderStatus;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTablenum() {
        return tablenum;
    }

    public void setTablenum(String tablenum) {
        this.tablenum = tablenum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}

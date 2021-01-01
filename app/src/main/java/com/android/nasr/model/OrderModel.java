package com.android.nasr.model;

public class OrderModel {

    String orderid;
    String orderdate;
    String status;
    String custName;
    String item;

    public OrderModel(String orderid, String orderdate, String status, String custName, String item) {
        this.orderid = orderid;
        this.orderdate = orderdate;
        this.status = status;
        this.custName = custName;
        this.item = item;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

}

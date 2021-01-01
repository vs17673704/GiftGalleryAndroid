package com.android.nasr.model;

public class CustomerModel {

    String custId;
    String custName;
    String address;
    String phone;
    String email;
    String password;

    public CustomerModel(String custId, String custName, String address, String phone, String email, String password) {
        this.custId = custId;
        this.custName = custName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }


    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

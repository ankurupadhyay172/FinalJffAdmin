package com.androidcafe.jffstaff.Model;

import com.google.firebase.Timestamp;

public class OrderModel {

    String id,address,delivery_charge,order_json,payment_id,total_price,user_id,user_name,status,payment_method,user_model,date_format;
    boolean istakeaway,ispayment;

    public String getUser_model() {
        return user_model;
    }

    public void setUser_model(String user_model) {
        this.user_model = user_model;
    }

    public String getDate_format() {
        return date_format;
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }

    String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public boolean isIspayment() {
        return ispayment;
    }

    public void setIspayment(boolean ispayment) {
        this.ispayment = ispayment;
    }

    Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getOrder_json() {
        return order_json;
    }

    public void setOrder_json(String order_json) {
        this.order_json = order_json;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean isIstakeaway() {
        return istakeaway;
    }

    public void setIstakeaway(boolean istakeaway) {
        this.istakeaway = istakeaway;
    }
}

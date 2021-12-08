package com.ankurupadhyay.finaljffadmin.LocalDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.google.firebase.Timestamp;

import java.util.List;
@Entity
public class MyOrders {


    @NonNull
    @PrimaryKey
    String id;
    String user_json;
    String cart_list;
    String date_time;
    String message;
    String table_no;

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MyOrders(@NonNull String id, String user_json, String cart_list, String date_time) {
        this.id = id;
        this.user_json = user_json;
        this.cart_list = cart_list;
        this.date_time = date_time;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUser_json() {
        return user_json;
    }

    public void setUser_json(String user_json) {
        this.user_json = user_json;
    }

    public String getCart_list() {
        return cart_list;
    }

    public void setCart_list(String cart_list) {
        this.cart_list = cart_list;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}

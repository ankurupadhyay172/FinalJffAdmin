package com.ankurupadhyay.finaljffadmin.LocalDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PendingCart {





    @NonNull
    @PrimaryKey
    String id;
    String json;
    int quantity;
    int price;
    String size;

    public PendingCart(@NonNull String id, String json, int quantity, int price, String size) {
        this.id = id;
        this.json = json;
        this.quantity = quantity;
        this.price = price;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}

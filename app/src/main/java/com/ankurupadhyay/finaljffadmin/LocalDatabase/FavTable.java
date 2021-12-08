package com.ankurupadhyay.finaljffadmin.LocalDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavTable {

    @NonNull
    @PrimaryKey
    String id;
    String json;
    int price;
    boolean isfav;


    public FavTable(@NonNull String id, String json, int price, boolean isfav) {
        this.id = id;
        this.json = json;
        this.price = price;
        this.isfav = isfav;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isIsfav() {
        return isfav;
    }

    public void setIsfav(boolean isfav) {
        this.isfav = isfav;
    }
}

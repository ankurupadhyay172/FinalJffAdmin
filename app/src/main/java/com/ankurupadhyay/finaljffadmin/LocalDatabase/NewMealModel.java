package com.ankurupadhyay.finaljffadmin.LocalDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewMealModel {

    @NonNull
    @PrimaryKey
    String id;
    String category,desc,image,jsize,name,price;
    int order;


    public NewMealModel(String id, String category, String desc, String image, String jsize, String name, String price, int order) {
        this.id = id;
        this.category = category;
        this.desc = desc;
        this.image = image;
        this.jsize = jsize;
        this.name = name;
        this.price = price;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJsize() {
        return jsize;
    }

    public void setJsize(String jsize) {
        this.jsize = jsize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

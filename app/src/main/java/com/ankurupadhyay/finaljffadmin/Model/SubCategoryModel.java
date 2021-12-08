package com.ankurupadhyay.finaljffadmin.Model;

import java.util.List;

public class SubCategoryModel {

    String id,category,desc,image,name,price,size;
    List<String> pricelist;


    public SubCategoryModel(String id, String category, String desc, String image, String name, String price, String size) {
        this.id = id;
        this.category = category;
        this.desc = desc;
        this.image = image;
        this.name = name;
        this.price = price;
        this.size = size;
    }

    public SubCategoryModel() {
    }

    public List<String> getPricelist() {
        return pricelist;
    }

    public void setPricelist(List<String> pricelist) {
        this.pricelist = pricelist;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

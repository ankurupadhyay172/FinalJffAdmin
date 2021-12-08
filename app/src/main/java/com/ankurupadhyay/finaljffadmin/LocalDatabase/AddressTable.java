package com.ankurupadhyay.finaljffadmin.LocalDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AddressTable {
    @PrimaryKey(autoGenerate = false)
    int id;
    String name,pincode,address,city,state,mobile_no,address_type;
    int active;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public AddressTable(int id, String name, String pincode, String address, String city, String state, String mobile_no, String address_type) {
        this.id = id;
        this.name = name;
        this.pincode = pincode;
        this.address = address;
        this.city = city;
        this.state = state;
        this.mobile_no = mobile_no;
        this.address_type = address_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }
}

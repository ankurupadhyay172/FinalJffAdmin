package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ankurupadhyay.finaljffadmin.Model.CartTable;

import java.util.List;

public class DataViewHolder extends ViewModel {

    String id;


    MutableLiveData<Integer> totalprice;
    MutableLiveData<List<CartTable>> mutablecartlist;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        if (id==null)
        {
            id="Pizza";
        }

        return id;
    }


    public MutableLiveData<Integer> getTotalprice(Integer price)

    {

        totalprice = new MutableLiveData<>();
        totalprice.setValue(price);


        return totalprice;



    }


    public MutableLiveData<List<CartTable>> getCart(List<CartTable> cartTableList)
    {

        mutablecartlist = new MutableLiveData<>();
        mutablecartlist.setValue(cartTableList);

        return mutablecartlist;


    }

}

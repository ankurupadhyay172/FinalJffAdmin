package com.ankurupadhyay.finaljffadmin.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.R;


public class ItemDelete extends RecyclerView.ViewHolder {

    public LinearLayout swapLayout,deleteLayout,li_cart,li_sub,li_add;
    public TextView txt_price,txt_addItem,txt_count,txt_title,subtitle,acprice,txt_size;
    public ImageView img_delete,img_product,cart_delete;

    public ItemDelete(@NonNull View itemView) {
        super(itemView);
        txt_size = itemView.findViewById(R.id.txt_size);
        swapLayout = itemView.findViewById(R.id.swapLayout);
        deleteLayout = itemView.findViewById(R.id.deleteLayout);
        img_delete = itemView.findViewById(R.id.img_delete);
        txt_price = itemView.findViewById(R.id.txt_price);

        txt_addItem = itemView.findViewById(R.id.txt_addItem);
        txt_count = itemView.findViewById(R.id.txt_count);
        li_cart = itemView.findViewById(R.id.li_cart);
        li_sub = itemView.findViewById(R.id.li_sub);
        li_add = itemView.findViewById(R.id.li_add);
        img_product = itemView.findViewById(R.id.img_product);
        txt_title = itemView.findViewById(R.id.txt_Title);
        subtitle = itemView.findViewById(R.id.subtitle);
        cart_delete = itemView.findViewById(R.id.delete_cart);
        acprice = itemView.findViewById(R.id.acprice);
    }
}
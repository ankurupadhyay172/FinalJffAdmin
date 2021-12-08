package com.ankurupadhyay.finaljffadmin.ViewHolders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.R;


public class OrderViewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


    public ImageView img_bg;
    public TextView txt_orderNo,txt_date_time,txt_way,txt_item_count,txt_dollar,delivery_charge,total_price,uname,ucontact,uaddress,payment_status,payment_method,date;

    public LinearLayout table_layout;
    public TextView table_no;
    public OrderViewholder(@NonNull View itemView) {
        super(itemView);

        uname = itemView.findViewById(R.id.uname);
        ucontact = itemView.findViewById(R.id.ucontact);
        uaddress = itemView.findViewById(R.id.uaddress);
        img_bg = itemView.findViewById(R.id.img_bg);
        txt_orderNo = itemView.findViewById(R.id.txt_orderNo);
        txt_date_time = itemView.findViewById(R.id.txt_date_time);
        txt_way = itemView.findViewById(R.id.txt_way);
        txt_item_count = itemView.findViewById(R.id.txt_item_count);
        txt_dollar = itemView.findViewById(R.id.txt_dollar);
        delivery_charge = itemView.findViewById(R.id.delivery_charge);
        total_price = itemView.findViewById(R.id.total_price);
        payment_status = itemView.findViewById(R.id.payment_status);
        payment_method = itemView.findViewById(R.id.payment_method);
        date = itemView.findViewById(R.id.date);

        table_layout = itemView.findViewById(R.id.layout_table);
        table_no = itemView.findViewById(R.id.utable_no);


        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {


        menu.setHeaderTitle("Choose the status ");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Cooking");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Out for delivery");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Delivered");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Failed");

    }
}

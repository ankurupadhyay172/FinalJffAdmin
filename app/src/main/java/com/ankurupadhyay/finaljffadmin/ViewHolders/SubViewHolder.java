package com.ankurupadhyay.finaljffadmin.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.R;


public class SubViewHolder extends RecyclerView.ViewHolder {


    public TextView size,title,desc,price,add_item_text, minus_text, plus_text,txt_count,edit,delete;
    public ImageView image,fav;
    public LinearLayout add_cart_linear;
    public SubViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.image);
        title = itemView.findViewById(R.id.title2);
        desc = itemView.findViewById(R.id.desc);
        price = itemView.findViewById(R.id.price);
        size = itemView.findViewById(R.id.size);
        txt_count = itemView.findViewById(R.id.txt_count);
        fav = itemView.findViewById(R.id.fav);

        edit = itemView.findViewById(R.id.edit);
        delete = itemView.findViewById(R.id.delete);




        add_item_text = itemView.findViewById(R.id.add_item_txt);
        minus_text = itemView.findViewById(R.id.minus_txt);
        plus_text = itemView.findViewById(R.id.plus_text);

        add_cart_linear = itemView.findViewById(R.id.add_cart_linear);
    }
}

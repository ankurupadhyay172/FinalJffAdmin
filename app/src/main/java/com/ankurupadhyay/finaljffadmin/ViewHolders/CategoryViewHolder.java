package com.ankurupadhyay.finaljffadmin.ViewHolders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public ImageView image;
    public TextView title;
    public LinearLayout linear;

    public CategoryViewHolder(@NonNull View view) {
        super(view);

        image = (ImageView) view.findViewById(R.id.image);
        title = (TextView) view.findViewById(R.id.title);
        linear = (LinearLayout) view.findViewById(R.id.linear);



        itemView.setOnCreateContextMenuListener(this);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose an action ");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Edit");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Delete");

    }
}

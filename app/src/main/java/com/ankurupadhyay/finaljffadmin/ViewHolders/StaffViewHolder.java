package com.ankurupadhyay.finaljffadmin.ViewHolders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.R;

public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public ImageView imageView;

    public TextView name,mobile;
    public StaffViewHolder(@NonNull View itemView) {
        super(itemView);


        imageView = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        mobile = itemView.findViewById(R.id.mobile);

        itemView.setOnCreateContextMenuListener(this);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose an action ");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Edit");
        menu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"Delete");

    }
}

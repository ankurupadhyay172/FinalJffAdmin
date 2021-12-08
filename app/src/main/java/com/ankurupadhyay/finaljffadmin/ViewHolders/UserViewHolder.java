package com.ankurupadhyay.finaljffadmin.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.R;

public class UserViewHolder extends RecyclerView.ViewHolder {


    public ImageView imageView;

    public TextView name,mobile;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        mobile = itemView.findViewById(R.id.mobile);

    }
}

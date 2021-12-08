package com.ankurupadhyay.finaljffadmin.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView txtcanceled,date,user_name,order,total_amount;

    public ImageView iv_circle;
    public LinearLayout status_layout;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txtcanceled=itemView.findViewById(R.id.txtcanceled);
        date = itemView.findViewById(R.id.date);
        iv_circle=itemView.findViewById(R.id.iv_circle);

        total_amount = itemView.findViewById(R.id.total_amount);
        user_name = itemView.findViewById(R.id.user_name);
        order = itemView.findViewById(R.id.order);
        status_layout = itemView.findViewById(R.id.status_layout);

    }
}

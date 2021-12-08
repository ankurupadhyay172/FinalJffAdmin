package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Model.StaffModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ViewHolders.StaffViewHolder;
import com.ankurupadhyay.finaljffadmin.ViewHolders.UserViewHolder;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffViewHolder> {

    Context context;
    List<StaffModel> list;


    public StaffAdapter(Context context, List<StaffModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.listview, parent, false);

        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.mobile.setText("Contact : "+list.get(position).getId()+"\nType : "+list.get(position).getType());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

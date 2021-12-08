package com.androidcafe.jffstaff.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcafe.jffstaff.Model.OrderModel;
import com.androidcafe.jffstaff.R;

import com.google.gson.Gson;


import java.text.DateFormat;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    Context context;
    List<OrderModel> list;

    Gson gson = new Gson();
    public OrdersAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cancel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        OrderModel model = list.get(position);


        //List<CartTable> cartTableList = gson.fromJson(list.get(position).getOrder_json(),listType);




        holder.tvTitle.setText(model.getUser_name()+"\n"+model.getUser_id());



        String mydate = DateFormat.getDateInstance().format(list.get(position).getTimestamp().toDate());


        String ordertime = DateFormat.getTimeInstance(DateFormat.SHORT).format(list.get(position).getTimestamp().toDate());







        holder.tvDateTime.setText(model.getAddress()+"\n"+"Order Price = ₹"+model.getTotal_price()+"\nDelivery Charge = ₹"+model.getDelivery_charge()+"\n\n"+mydate+" , "+ordertime);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imgCleaning;
        TextView tvTitle,tvDateTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            imgCleaning = itemView.findViewById(R.id.imgCleaning);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);


        }
    }
}

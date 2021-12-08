package com.androidcafe.jffstaff.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcafe.jffstaff.Model.OrderModel;
import com.androidcafe.jffstaff.R;

import java.text.DateFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    Context context;


    List<OrderModel> list;

    public OrderDetailAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_orders,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        OrderModel model = list.get(position);

        try {
            holder.uname.setText(model.getUser_name().toUpperCase());
            holder.ucontact.setText(model.getUser_id());
            holder.uaddress.setText(model.getAddress());
            holder.payment_method.setText(model.getPayment_method());
            if (model.isIspayment())
            holder.payment_status.setText("Paid");
            else
                holder.payment_status.setText("Not Paid");



            String mydate = DateFormat.getDateInstance().format(list.get(position).getTimestamp().toDate());


            String ordertime = DateFormat.getTimeInstance(DateFormat.SHORT).format(list.get(position).getTimestamp().toDate());

            holder.date.setText("Order Date : "+mydate+" \nOrder Time : "+ordertime);
            holder.txt_way.setText("Delivery Status : "+list.get(position).getStatus());
            holder.delivery_charge.setText("₹ "+list.get(position).getDelivery_charge());



        }catch (Exception e)
        {

        }







    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {

        public ImageView img_bg;
        public TextView txt_orderNo,txt_date_time,txt_way,txt_item_count,txt_dollar,delivery_charge,total_price,uname,ucontact,uaddress,payment_status,payment_method,date;

        public MyViewHolder(@NonNull View itemView) {
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
}

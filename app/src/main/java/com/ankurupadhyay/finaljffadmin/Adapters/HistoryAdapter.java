package com.ankurupadhyay.finaljffadmin.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.HistoryModel;
import com.ankurupadhyay.finaljffadmin.Model.NewOrderModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ShowSingleOrderDetailActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    Context context;
    private List<OrderModel> historyModelArrayList;

    Gson gson = new Gson();
    public HistoryAdapter(Context context, List<OrderModel> historyModelArrayList) {
        this.context = context;
        this.historyModelArrayList = historyModelArrayList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final HistoryAdapter.ViewHolder holder, final int position) {

        //holder.txtcanceled.setText(historyModelArrayList.get(position).getTxtcanceled());


        String order_name = "";
        SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy HH:mm a", Locale.getDefault());
        holder.date.setText(""+df.format(historyModelArrayList.get(position).getTimestamp().toDate()));

        holder.user_name.setText(historyModelArrayList.get(position).getUser_name());
        holder.total_amount.setText("₹ "+historyModelArrayList.get(position).getTotal_price());


        Type listType = new TypeToken<List<CartTable>>(){}.getType();
        List<CartTable> cartTableList = gson.fromJson(historyModelArrayList.get(position).getOrder_json(),listType);



        for (CartTable list:cartTableList) {

            NewOrderModel model1 = gson.fromJson(list.getJson(),NewOrderModel.class);
            order_name = order_name+model1.getName()+"\n";
        }
        holder.order.setText(order_name);


            if (historyModelArrayList.get(position).getStatus().equals("Delivered"))
            {
                holder.txtcanceled.setText("Successfully");
                holder.txtcanceled.setTextColor(Color.parseColor("#ff2e63"));
                holder.iv_circle.setColorFilter(Color.parseColor("#ff2e63"));


                holder.status_layout.setBackgroundColor(context.getResources().getColor(R.color.status_success));
            }else
            if (historyModelArrayList.get(position).getStatus().equals("Failed"))
            {
                holder.txtcanceled.setText("Failed");

                holder.status_layout.setBackgroundColor(context.getResources().getColor(R.color.status_failed));
                holder.txtcanceled.setTextColor(Color.parseColor("#e84b4b"));
                holder.iv_circle.setColorFilter(Color.parseColor("#e84b4b"));

            }
            else
            {
                holder.txtcanceled.setText("Pending");
                holder.txtcanceled.setTextColor(Color.parseColor("#fbbd36"));
                holder.iv_circle.setColorFilter(Color.parseColor("#fbbd36"));

                holder.status_layout.setBackgroundColor(context.getResources().getColor(R.color.status_pending));
            }




            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowSingleOrderDetailActivity.class);
                    intent.putExtra("id",historyModelArrayList.get(position).getId());
                    context.startActivity(intent);
                }
            });

//            if(position==0){
//            holder.txtcanceled.setTextColor(Color.parseColor("#e84b4b"));
//            holder.iv_circle.setColorFilter(Color.parseColor("#e84b4b"));
//
//        }
//        if(position==1){
//            holder.txtcanceled.setTextColor(Color.parseColor("#fbbd36"));
//            holder.iv_circle.setColorFilter(Color.parseColor("#fbbd36"));
//
//        }
//        if(position==2){
//            holder.txtcanceled.setTextColor(Color.parseColor("#ff2e63"));
//            holder.iv_circle.setColorFilter(Color.parseColor("#ff2e63"));
//
//        }

    }

    @Override
    public int getItemCount() {

        return historyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtcanceled,date,user_name,order,total_amount;

        ImageView iv_circle;

        LinearLayout status_layout;
        public ViewHolder(View itemView) {

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
}

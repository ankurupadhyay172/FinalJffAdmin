package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.CompleteOrderActivity;
import com.ankurupadhyay.finaljffadmin.Products.EditProductsActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

public class PendingUserAdapter extends RecyclerView.Adapter<PendingUserAdapter.MyViewHolder> {

    Context context;
    List<MyOrders> list;
    List<OrderModel> orderModelList;


    Gson gson = new Gson();


    public PendingUserAdapter(Context context, List<MyOrders> list, List<OrderModel> orderModelList) {
        this.context = context;
        this.list = list;
        this.orderModelList = orderModelList;


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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.listview,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        MyOrders myOrders = list.get(position);



        try {
            UserModel model = gson.fromJson(myOrders.getUser_json(),UserModel.class);
            holder.name.setText(model.getName());
            holder.mobile.setText(model.getMobile_no()+"\n"+myOrders.getDate_time());

            Log.d("mylogvalue23",""+myOrders.getDate_time());

            try {
                Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.imageView);
            }catch (IllegalArgumentException e)
            {

            }

        }catch (Exception e)
        {

        }






        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().write("order_model",myOrders);
                Paper.book().write("complete_order_model",orderModelList.get(position));
                //Intent intent = new Intent(context, CompleteOrderActivity.class);

                Intent intent = new Intent(context, EditProductsActivity.class);
                intent.putExtra("order_json",gson.toJson(myOrders));
                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;

        public TextView name,mobile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);

        }
    }
}

package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ShowSingleOrderDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

public class CompleteOrdersAdapter extends RecyclerView.Adapter<CompleteOrdersAdapter.MyViewHolder> {

    Context context;

    List<OrderModel> list;

    FirebaseFirestore db;
    Gson gson = new Gson();
    public CompleteOrdersAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mangeaddress,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderModel model = list.get(position);


        db.collection(Common.db_user).document(list.get(position).getUser_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel userModel = documentSnapshot.toObject(UserModel.class);

               try {

                    Picasso.get().load(userModel.getImage()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.user_image);
                }catch (Exception e)
                {

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




        try {
            Type listType = new TypeToken<List<CartTable>>(){}.getType();
            List<CartTable> cartTables = gson.fromJson(list.get(position).getOrder_json(),listType);
            int total_price =0;
            for (CartTable cartTable:cartTables)
            {
                total_price =total_price+(cartTable.getPrice()*cartTable.getQuantity());


            }
            total_price = total_price+Integer.parseInt(list.get(position).getDelivery_charge());
            try {
                total_price = total_price-Integer.parseInt(list.get(position).getDiscount());

            }catch (Exception e)
            {

            }
//            Log.d("mydiscount",""+total_price);
//
//            Log.d("mycarttotal",""+total_price);

            holder.price.setText("₹ "+total_price);


        }catch (Exception e)
        {

        }


        holder.user_name.setText(list.get(position).getUser_name());
        holder.user_detail.setText(list.get(position).getAddress()+"\n"+ Common.gethours(list.get(position).getTimestamp().getSeconds()));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowSingleOrderDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
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
        ImageView user_image;
        TextView user_name,user_detail,price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.price);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_detail = itemView.findViewById(R.id.user_detail);
        }
    }
}

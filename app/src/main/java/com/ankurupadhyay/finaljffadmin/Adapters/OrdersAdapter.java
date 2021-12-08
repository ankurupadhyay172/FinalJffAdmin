package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.NewOrderModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ViewHolders.OrderViewholder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrderViewholder> {

    Context context;
    List<OrderModel> list;

    Gson gson = new Gson();
    FirebaseFirestore db;



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public OrdersAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
    }







    @NonNull
    @Override
    public OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_orders, parent, false);


        return new OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewholder holder, int position) {


        try {


            if (list.get(position).getStatus().equals("Pending")||list.get(position).getStatus().equals("Cooking"))
            {
                db.collection("Users").document(list.get(position).getUser_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        try {
                            UserModel userModel = documentSnapshot.toObject(UserModel.class);
                            userModel.setId(documentSnapshot.getId());

                            db.collection(Common.Purchased_Db).document(list.get(position).getId()).update("user_model",gson.toJson(userModel)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                        }catch (Exception e)
                        {

                        }


                    }
                });

            }





        }catch (Exception e)
        {

        }



        try {

            Type listType = new TypeToken<List<CartTable>>(){}.getType();



            List<CartTable> cartTableList = gson.fromJson(list.get(position).getOrder_json(),listType);
            holder.total_price.setText(" = ₹ "+list.get(position).getTotal_price());
            int pos=0,mytotal=0;

            String final_order = "";

            for (CartTable list:cartTableList)
            {
                pos = pos+1;

                int totoal_price = list.getPrice()+list.getExtra();

                NewOrderModel model1 = gson.fromJson(list.getJson(),NewOrderModel.class);
                Log.d("apunkilist",""+cartTableList.size()+"price"+model1.getPrice());

                final_order = final_order+pos+". Category : "+model1.getCategory()+ "\nOrder : ((Size: "+list.getQuantity()+") "+list.getSize()+" )"+model1.getName()+" (₹ "+totoal_price+")\n\n";




                try {
                    mytotal = mytotal+totoal_price*list.getQuantity();


                    Log.d("myprice",""+list.getPrice());

                }
                catch (Exception e)
                {
                    Log.d("apunkivalue",e.getMessage());

                }
            }


            String mydate = DateFormat.getDateInstance().format(list.get(position).getTimestamp().toDate());


            String ordertime = DateFormat.getTimeInstance(DateFormat.SHORT).format(list.get(position).getTimestamp().toDate());



            holder.date.setText(""+mydate+" , "+ordertime);
            holder.txt_orderNo.setText(final_order);
            holder.uname.setText(list.get(position).getUser_name().toUpperCase());
            holder.ucontact.setText(list.get(position).getUser_id());
            holder.uaddress.setText(list.get(position).getAddress());
            holder.txt_item_count.setText("Items X "+cartTableList.size());
            holder.txt_dollar.setText("₹ "+mytotal);
            holder.txt_date_time.setText(Common.gethours(list.get(position).getTimestamp().getSeconds()));
            holder.txt_way.setText("Delivery Status : "+list.get(position).getStatus());


            if (list.get(position).isIspayment())
            {
                holder.payment_status.setText("PAID");
                holder.payment_method.setText(list.get(position).getPayment_method());
            }
            else
            {
                holder.payment_status.setText("NOT PAID");
                holder.payment_method.setText(list.get(position).getPayment_method());
            }






            holder.delivery_charge.setText("₹ "+list.get(position).getDelivery_charge());

            if (list.get(position).getStatus().equals("Successfull"))
            {
                holder.img_bg.setImageResource(R.drawable.rectangle_left_cure_green);
            }
            if (list.get(position).getStatus().equals("Cancelled"))
            {
                holder.img_bg.setImageResource(R.drawable.rectangle_left_cure_red);
            }


            if (list.get(position).getTable_no()!=null)
            {
                holder.table_layout.setVisibility(View.VISIBLE);
                try {
                    holder.table_no.setText(list.get(position).getTable_no().toUpperCase());
                }catch (Exception e)
                {

                }

            }



            holder.ucontact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+list.get(position).getUser_id()));

                    context.startActivity(intent);
                }
            });

        }catch (Exception e)
        {

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

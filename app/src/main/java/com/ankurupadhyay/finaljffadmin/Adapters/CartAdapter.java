package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<ItemDelete> {

    Context context;
    List<CartTable> list;
    MyDatabase myDatabase;
    OnUpdateData onUpdateData;

    Gson gson;
    public CartAdapter(Context context, List<CartTable> list, MyDatabase myDatabase, OnUpdateData onUpdateData) {
        this.context = context;
        this.list = list;
        this.myDatabase = myDatabase;
        this.onUpdateData = onUpdateData;

        gson = new Gson();
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
    public ItemDelete onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custome_layout,parent,false);

        return new ItemDelete(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemDelete holder, final int position) {


        SubCategoryModel model = gson.fromJson(list.get(position).getJson(), SubCategoryModel.class);

        int total_price = list.get(position).getPrice()+list.get(position).getExtra();
        holder.txt_price.setText("₹ "+total_price);
        holder.txt_title.setText(model.getDesc());
        holder.subtitle.setText(model.getName());

        holder.txt_size.setText("Size : "+list.get(position).getSize());


        //myDatabase.dao().updateCartPrice(list.get(position).getPrice(),list.get(position).getId());

        Log.d("apunkafinalprice",""+list.get(position).getQuantity()+"\n"+model.getName());

        try {

            Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.img_product);
        }catch (IllegalArgumentException e)
        {

        }






        holder.txt_count.setText(""+list.get(position).getQuantity());

        holder.li_cart.setVisibility(View.VISIBLE);
        holder.txt_addItem.setVisibility(View.GONE);




        if (myDatabase.dao().cartList().size()!=0)
        {

            Log.d("apunkiid",""+myDatabase.dao().getCartId(list.get(position).getId()));

            if (myDatabase.dao().cartList().get(position).getQuantity()>0&&myDatabase.dao().getCartId(list.get(position).getId())!=null)
            {

                holder.txt_count.setText(""+myDatabase.dao().getQuantity(list.get(position).getId()));

                holder.li_cart.setVisibility(View.VISIBLE);
                holder.txt_addItem.setVisibility(View.GONE);

            }

        }


        holder.txt_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.li_cart.setVisibility(View.VISIBLE);
                holder.txt_addItem.setVisibility(View.GONE);

                updateData(position,1,list.get(position).getSize());



            }
        });

        holder.li_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int count = myDatabase.dao().getQuantity(list.get(position).getId());;
                count++;
                holder.txt_count.setText(String.valueOf(count));


                updateData(position, count,list.get(position).getSize());


            }
        });

        holder.li_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = myDatabase.dao().getQuantity(list.get(position).getId());
                if (count > 1) {
                    count--;
                    holder.txt_count.setText(String.valueOf(count));

                    updateData(position, count,list.get(position).getSize());
                } else {
                    holder.txt_addItem.setVisibility(View.VISIBLE);
                    holder.li_cart.setVisibility(View.GONE);

                    myDatabase.dao().deleteItem(list.get(position).getId());
                    onUpdateData.getOnUpdate(position);
                }
            }
        });
















        holder.cart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.dao().deleteItem(list.get(position).getId());


                onUpdateData.getOnUpdate(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }





    private void updateData(int position, int count,String size) {
        Gson gson = new Gson();

        String json = gson.toJson(list.get(position));

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy", Locale.getDefault());
//        String id = df.format(c)+mobile_no.getText().toString();
//        Log.d("apunkadatar", "" + myDatabase.dao().cartList());




        Log.d("apunkiid",""+list.get(position).getId());
        CartTable cartTable = new CartTable(list.get(position).getId(), json, count, list.get(position).getPrice(),size);

        myDatabase.dao().updateCartQuantity(count,list.get(position).getId());
        onUpdateData.getOnUpdate(position);

    }


    public interface OnUpdateData
    {
        public void getOnUpdate(int position);
    }
}

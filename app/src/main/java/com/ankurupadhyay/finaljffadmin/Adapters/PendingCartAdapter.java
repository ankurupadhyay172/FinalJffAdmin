package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingCartAdapter extends RecyclerView.Adapter<ItemDelete>  {

    Context context;
    List<CartTable> list;
    OnUpdateQuan onUpdateQuan;
    Gson gson = new Gson();

    public PendingCartAdapter(Context context, List<CartTable> list,OnUpdateQuan onUpdateQuan) {
        this.context = context;
        this.list = list;
        this.onUpdateQuan = onUpdateQuan;
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
    public void onBindViewHolder(@NonNull ItemDelete holder, int position) {


        SubCategoryModel model = gson.fromJson(list.get(position).getJson(), SubCategoryModel.class);


        int total_price = list.get(position).getPrice()+list.get(position).getExtra();
        holder.txt_price.setText("₹ "+total_price);
        holder.txt_title.setText(model.getDesc());
        holder.subtitle.setText(model.getName());

        holder.txt_size.setText("Size : "+list.get(position).getSize());





        try {

            Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.img_product);
        }catch (IllegalArgumentException e)
        {

        }






        holder.txt_count.setText(""+list.get(position).getQuantity());

        holder.li_cart.setVisibility(View.VISIBLE);
        holder.txt_addItem.setVisibility(View.GONE);


        holder.li_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int total_quan = list.get(position).getQuantity();
                total_quan++;
                list.get(position).setQuantity(total_quan);


                holder.txt_count.setText(""+list.get(position).getQuantity());


                onUpdateQuan.onGetUpdate(list);

            }
        });


        holder.li_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total_quan = list.get(position).getQuantity();


                if (total_quan>1)
                {
                    total_quan--;
                    list.get(position).setQuantity(total_quan);


                    holder.txt_count.setText(""+list.get(position).getQuantity());

                    onUpdateQuan.onGetUpdate(list);
                }
                else
                {

                    Toast.makeText(context, "can't select less then 1", Toast.LENGTH_SHORT).show();

                }

            }
        });







        holder.cart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                onUpdateQuan.onGetUpdate(list);
                Toast.makeText(context, "Item Delted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnUpdateQuan
    {
        public void onGetUpdate(List<CartTable> updatelist);
    }
}

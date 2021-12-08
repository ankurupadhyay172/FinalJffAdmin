package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LatestCartAdapter extends RecyclerView.Adapter<LatestCartAdapter.MyViewHolder> {

    Context context;
    List<CartTable> list;
    OnUpdateQuantity onUpdateQuantity;

    Gson gson = new Gson();
    public LatestCartAdapter(Context context, List<CartTable> list, OnUpdateQuantity onUpdateQuantity) {
        this.context = context;
        this.list = list;
        this.onUpdateQuantity = onUpdateQuantity;
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

        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CartTable cartTable_model = list.get(position);



        try {
            SubCategoryModel model = gson.fromJson(cartTable_model.getJson(), SubCategoryModel.class);


            holder.title.setText(model.getName());
            holder.size.setText("Size : "+list.get(position).getSize());
            holder.price.setText("₹ "+model.getPrice());
            holder.tvNumber.setText(""+list.get(position).getQuantity());

            if (cartTable_model.getQuantity()>0)
            {
                holder.liCounter.setVisibility(View.VISIBLE);
                holder.tvAdd.setVisibility(View.GONE);


            }
            else
            {

                holder.liCounter.setVisibility(View.GONE);
                holder.tvAdd.setVisibility(View.VISIBLE);
            }


            Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.image);



        }catch (Exception e)
        {

        }


        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total_quan = list.get(position).getQuantity();
                total_quan++;
                list.get(position).setQuantity(total_quan);


                holder.tvNumber.setText(""+list.get(position).getQuantity());


                onUpdateQuantity.onUpdateQuantity(list);
            }
        });




        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = list.get(position).getQuantity();


                if (count > 1) {
                    count--;
                    cartTable_model.setQuantity(count);
                    holder.tvNumber.setText(String.valueOf(count));
                    onUpdateQuantity.onUpdateQuantity(list);

                }else
                {
                    list.remove(position);

                    Toast.makeText(context, "Item Remove Successfully", Toast.LENGTH_SHORT).show();
                    holder.tvAdd.setVisibility(View.VISIBLE);
                    holder.liCounter.setVisibility(View.GONE);
                    onUpdateQuantity.onUpdateQuantity(list);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image,imgPlus,imgMinus;
        TextView title,size,price,tvAdd,tvNumber;
        LinearLayout liCounter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            size = itemView.findViewById(R.id.size);
            price = itemView.findViewById(R.id.price);
            title = itemView.findViewById(R.id.title);

            tvAdd = itemView.findViewById(R.id.tvAdd);
            liCounter = itemView.findViewById(R.id.liCounter);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            tvNumber = itemView.findViewById(R.id.tvNumber);


        }
    }


    public interface OnUpdateQuantity
    {

        public void onUpdateQuantity(List<CartTable> list);
    }

}

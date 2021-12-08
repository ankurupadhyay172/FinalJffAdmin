package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.CategoryActivity;
import com.ankurupadhyay.finaljffadmin.Model.CategoryModel;
import com.ankurupadhyay.finaljffadmin.Products.ProductDetailActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ViewHolders.CategoryViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductCategory extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<CategoryModel> list;

    public ProductCategory(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appliance_repair_list, parent, false);


        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {





        holder.title.setText(list.get(position).getId());

        try {
            Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.image);
        }catch (IllegalArgumentException e)
        {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("category",list.get(position).getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}

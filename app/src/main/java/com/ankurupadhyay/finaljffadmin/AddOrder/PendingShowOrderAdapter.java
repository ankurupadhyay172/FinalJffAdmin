package com.ankurupadhyay.finaljffadmin.AddOrder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Model.HomeMenuModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.SubCategoryActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingShowOrderAdapter extends RecyclerView.Adapter<PendingShowOrderAdapter.MyViewHolder> {




    Context context;

    List<HomeMenuModel> list;

    public PendingShowOrderAdapter(Context context, List<HomeMenuModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.title.setText(list.get(position).getId());

        //holder.subtitle.setText(list.get(position).getDesc());
        try {
            Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.image);
        }catch (IllegalArgumentException e)
        {

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PendingSubCategoryActivity.class);
                intent.putExtra("category",list.get(position).getCategory());
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



        ImageView image;
        TextView title,subtitle;
        LinearLayout linear;
        public MyViewHolder(@NonNull View view) {
            super(view);


            image = (ImageView) view.findViewById(R.id.ivMain);
            title = (TextView) view.findViewById(R.id.tvTitle);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            subtitle = view.findViewById(R.id.subtitle);


        }
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

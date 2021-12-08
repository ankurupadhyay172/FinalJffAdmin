package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Model.HomeMenuModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.SubCategoryActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeCleaningRecycleAdapter extends RecyclerView.Adapter<HomeCleaningRecycleAdapter.MyViewHolder> {

    Context context;

    List<HomeMenuModel> list;

    public HomeCleaningRecycleAdapter(Context context, List<HomeMenuModel> list) {
        this.context = context;
        this.list = list;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView title,subtitle;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.ivMain);
            title = (TextView) view.findViewById(R.id.tvTitle);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            subtitle = view.findViewById(R.id.subtitle);

        }

    }




    @Override
    public HomeCleaningRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_appliance_repair_list, parent, false);


//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_offer, parent, false);



        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_list, parent, false);
        return new HomeCleaningRecycleAdapter.MyViewHolder(itemView);


    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {


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
                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("category",list.get(position).getCategory());
                context.startActivity(intent);
            }
        });


//        if (position==4){
//
//            holder.linear.setVisibility(View.GONE);
//        }else {
//
//            holder.linear.setVisibility(View.VISIBLE);
//
//        }



    }


    @Override
    public int getItemCount() {
        return list.size();

    }

}


package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.SubCategoryActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SeachRecycleAdapter extends RecyclerView.Adapter<SeachRecycleAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<NewMealModel> list;
    List<NewMealModel> templist;


    public SeachRecycleAdapter(Context context, List<NewMealModel> list) {
        this.context = context;
        this.list = list;
        templist = new ArrayList<>();
        templist.addAll(list);

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<NewMealModel> filterList = new ArrayList<>();
                String data = constraint.toString();

                if (data==null||data.length()==0)
                {
                    filterList.addAll(templist);
                }
                else
                {
                    for (NewMealModel model:templist)
                    {
                        Log.d("apunkadata",""+model);

                        if (model.getName().toLowerCase().contains(data.toLowerCase()))
                        {
                            filterList.add(model);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filterList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list.clear();
                list.addAll((Collection<? extends NewMealModel>) results.values);
                notifyDataSetChanged();



            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        ImageView image;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView)view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);


        }

    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_list, parent, false);


        return new MyViewHolder(itemView);


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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        //   holder.image.setImageResource(lists.getImage());
        holder.title.setText(list.get(position).getName());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("category",list.get(position).getCategory());
                context.startActivity(intent);
            }
        });

        try {
            Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.image);
        }catch (IllegalArgumentException e)
        {

        }





    }


    @Override
    public int getItemCount() {
        return list.size();

    }

}




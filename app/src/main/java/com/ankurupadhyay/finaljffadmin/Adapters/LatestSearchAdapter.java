package com.ankurupadhyay.finaljffadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.LocalDatabase.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;

import com.ankurupadhyay.finaljffadmin.Model.SpinnerModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.SubCategoryActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LatestSearchAdapter extends RecyclerView.Adapter<LatestSearchAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<NewMealModel> list;
    Gson gson = new Gson();
    List<NewMealModel> templist;
    OnSearchUpdated onSearchUpdated;
    public LatestSearchAdapter(Context context, List<NewMealModel> list,OnSearchUpdated onSearchUpdated) {
        this.context = context;
        this.list = list;
        templist = new ArrayList<>();
        templist.addAll(list);
        this.onSearchUpdated = onSearchUpdated;
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





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dishes_rate,parent,false);


        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NewMealModel model = list.get(position);
        holder.title.setText(model.getName());
        try {


            Type listType = new TypeToken<List<CartTable>>(){}.getType();
            List<CartTable> cartTableList = gson.fromJson(model.getJsize(),listType);



            final List<SpinnerModel> sizes = gson.fromJson(model.getJsize(), new com.google.common.reflect.TypeToken<List<SpinnerModel>>(){}.getType());
            final SpinnerAdapter adapter = new SpinnerAdapter(context,R.layout.spinner_item,sizes);

            holder.size.setText(sizes.get(0).getName());
            holder.total.setText(""+sizes.get(0).getPrice());

            holder.size.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setTitle("Select Size")
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    holder.size.setText(sizes.get(which).getName());
                                    holder.total.setText(""+sizes.get(which).getPrice());




                                }
                            }).create().show();
                }
            });




        }catch (Exception e)
        {

        }

        holder.add_item_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int count =1;




                holder.add_item_txt.setVisibility(View.GONE);
                holder.add_cart_linear.setVisibility(View.VISIBLE);
                holder.tvNumber.setText(""+count);
                onSearchUpdated.onGetUpdated(list.get(position),holder.size.getText().toString().trim(),holder.total.getText().toString().trim(),1);

            }
        });


        holder.plus_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.tvNumber.getText().toString());
                count++;
                holder.tvNumber.setText(""+count);

                onSearchUpdated.onGetUpdated(list.get(position),holder.size.getText().toString().trim(),holder.total.getText().toString().trim(),count);
            }
        });


        holder.minus_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.tvNumber.getText().toString());
                if (count>1)
                {
                    count--;
                    holder.tvNumber.setText(""+count);
                    onSearchUpdated.onGetUpdated(list.get(position),holder.size.getText().toString().trim(),holder.total.getText().toString().trim(),count);
                }
               else
                {
                    holder.add_item_txt.setVisibility(View.VISIBLE);
                    holder.add_cart_linear.setVisibility(View.GONE);
                    onSearchUpdated.onGetUpdated(list.get(position),holder.size.getText().toString().trim(),holder.total.getText().toString().trim(),0);

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
        TextView title,total,size,add_item_txt,minus_txt,tvNumber,plus_text;

        LinearLayout add_cart_linear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            total = itemView.findViewById(R.id.total);
            size = itemView.findViewById(R.id.size);
            add_item_txt = itemView.findViewById(R.id.add_item_txt);
            minus_txt = itemView.findViewById(R.id.minus_txt);
            plus_text = itemView.findViewById(R.id.plus_text);
            add_cart_linear = itemView.findViewById(R.id.add_cart_linear);
            tvNumber = itemView.findViewById(R.id.tvNumber);

        }
    }

    public interface OnSearchUpdated
    {
        public void onGetUpdated(NewMealModel model,String size,String price,int quantity);
    }
}

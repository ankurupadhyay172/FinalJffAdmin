package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankurupadhyay.finaljffadmin.Model.GridModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ShowImageActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {


    Context context;
    private List<GridModel> list;


    public GridAdapter(Context context, List<GridModel> list) {
        this.context = context;
        this.list = list;
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

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


            Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.fieldImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowImageActivity.class);

                intent.putExtra("key",list.get(position).getImage());
                context.startActivity(intent);








//
//                    Intent intent = new Intent(context, ImageViewActivity.class);
//                    intent.putExtra("key",list.get(position).getImage());
//                    context.startActivity(intent);

            }
        });


//        if (position == 8) {
//
//            holder.fieldName.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.fieldName.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView fieldImage,tick;
        TextView fieldName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fieldName=(TextView)itemView.findViewById(R.id.fieldName);

            fieldImage=(ImageView)itemView.findViewById(R.id.fieldImage);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Choose an action");
            contextMenu.add(getAdapterPosition(),getAdapterPosition(),getAdapterPosition(),"DELETE");

        }
    }
}

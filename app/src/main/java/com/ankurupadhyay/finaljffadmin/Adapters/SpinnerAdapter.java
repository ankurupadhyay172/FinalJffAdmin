package com.ankurupadhyay.finaljffadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.ankurupadhyay.finaljffadmin.Model.SpinnerModel;
import com.ankurupadhyay.finaljffadmin.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerModel> {
    LayoutInflater inflater;
    List<SpinnerModel> list;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<SpinnerModel> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = objects;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view==null)
        {
            view = inflater.inflate(R.layout.spinner_item,parent,false);
        }

                TextView desc = view.findViewById(R.id.description);
                TextView price = view.findViewById(R.id.price);
                TextView title = view.findViewById(R.id.text1);

                title.setText(list.get(position).getName());
                price.setText("₹ "+list.get(position).getPrice());
                desc.setText(list.get(position).getDesc());

        return view;
    }
}

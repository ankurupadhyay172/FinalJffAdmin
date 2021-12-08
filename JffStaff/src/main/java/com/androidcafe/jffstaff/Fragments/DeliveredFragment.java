package com.androidcafe.jffstaff.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidcafe.jffstaff.Adapters.OrdersAdapter;
import com.androidcafe.jffstaff.Common;
import com.androidcafe.jffstaff.Model.OrderModel;
import com.androidcafe.jffstaff.R;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeliveredFragment extends Fragment {


    public DeliveredFragment() {
        // Required empty public constructor
    }



    RecyclerView recyclerView;

    OrdersAdapter adapter;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_delivered, container, false);
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.rvCancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        db.collection(Common.Purchased_Db).whereEqualTo("status","Delivered").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null)
                {
                    Toast.makeText(getActivity(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }else
                {
                    List<OrderModel> list = new ArrayList<>();
                    for (DocumentSnapshot document:value)
                    {
                        OrderModel model = document.toObject(OrderModel.class);
                        model.setId(document.getId());
                        list.add(model);
                    }


                    adapter = new OrdersAdapter(getActivity(),list);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        return view;
    }
}
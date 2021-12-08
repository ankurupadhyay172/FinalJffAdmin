package com.ankurupadhyay.finaljffadmin.FinalStaffData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.OrdersAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FinalDeliveredFragment extends Fragment {

    public FinalDeliveredFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    OrdersAdapter adapter;
    FirebaseFirestore db;


    LinearLayout li_empty;

    TextView status;

    List<OrderModel> order_list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_final_delivered, container, false);

        db = FirebaseFirestore.getInstance();

        status = view.findViewById(com.androidcafe.jffstaff.R.id.status);
        li_empty = view.findViewById(com.androidcafe.jffstaff.R.id.li_empty);
        recyclerView = view.findViewById(com.androidcafe.jffstaff.R.id.rvCancel);
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

                    if (list.size()<=0)
                    {
                        li_empty.setVisibility(View.VISIBLE);
                        status.setText("No Delivered Orders");
                    }

                    else
                        li_empty.setVisibility(View.GONE);


//                    adapter = new OrdersAdapter(getActivity(),list);

                    order_list = list;
                    OrdersAdapter adapter = new OrdersAdapter(getActivity(),list);
                    recyclerView.setAdapter(adapter);

                    recyclerView.setAdapter(adapter);
                }
            }
        });


        return view;
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        try {
            db.collection("Purchased_Order").document(order_list.get(item.getGroupId()).getId()).update("status",item.getTitle()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(getActivity(), "Order Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }catch (Exception e)
        {

        }


        return true;
    }

}
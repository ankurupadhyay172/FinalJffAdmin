package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.PendingUserAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PendingOrdersActivity extends AppCompatActivity {

    PendingUserAdapter adapter;
    RecyclerView recyclerView;
    MyDatabase myDatabase;

    ImageView imgBack;
    TextView tvTitle;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        Paper.init(this);
        db = FirebaseFirestore.getInstance();
        myDatabase = Room.databaseBuilder(this, MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        imgBack = findViewById(R.id.imgBack);
        tvTitle = findViewById(R.id.tvTitle);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        db.collection(Common.Purchased_Db).whereNotEqualTo("user_model",null).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                try {
                    if (!value.isEmpty())
                    {

                        List<OrderModel> list = new ArrayList<>();

                        for (DocumentSnapshot document:value.getDocuments())
                        {

                            OrderModel orderModel = document.toObject(OrderModel.class);
                            orderModel.setId(document.getId());

                            Log.d("mylogdata",""+document.getData());


                            if (orderModel.getStatus().equals("Pending")||orderModel.getStatus().equals("Cooking")||orderModel.getStatus().equals("Out for delivery"))
                            {
                                MyOrders myOrders =  new MyOrders(orderModel.getId(),orderModel.getUser_model(),orderModel.getOrder_json(),orderModel.getDate_format());
                                myDatabase.dao().InsertMyOrder(myOrders);
                                list.add(orderModel);
                            }
                            else
                            {
                                myDatabase.dao().deleteSignlerder(orderModel.getId());
                            }

                        }
                        adapter = new PendingUserAdapter(PendingOrdersActivity.this,myDatabase.dao().ordersList(),list);

                        recyclerView.setAdapter(adapter);


                    }
                    else
                    {
                        Toast.makeText(PendingOrdersActivity.this, "value is empty", Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e)
                {
                    Toast.makeText(PendingOrdersActivity.this, "exception"+e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });









        tvTitle.setText("Pending Orders");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
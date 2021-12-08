package com.ankurupadhyay.finaljffadmin.Orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankurupadhyay.finaljffadmin.Adapters.OrdersAdapter;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.R;

import java.util.List;

public class TotalOrdersActivity extends AppCompatActivity implements MyFirebase.OngetPurchased {

    ImageView back;
    RecyclerView recyclerView;


    MyFirebase myFirebase;
    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_orders);

        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        total = findViewById(R.id.total);

        myFirebase = new MyFirebase(this);


        myFirebase.getSuccessOrders(this);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    @Override
    public void onGetPurchased(List<OrderModel> list) {

        total.setText("Total : "+list.size());

        OrdersAdapter adapter = new OrdersAdapter(this,list);
        recyclerView.setAdapter(adapter);

    }
}
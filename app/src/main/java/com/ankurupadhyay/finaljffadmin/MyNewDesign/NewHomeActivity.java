package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ankurupadhyay.finaljffadmin.R;

public class NewHomeActivity extends AppCompatActivity {


    LinearLayout take_order,pending_orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);

        pending_orders = findViewById(R.id.pending_orders);
        take_order = findViewById(R.id.take_order);



        take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewHomeActivity.this,TakeOrderActivity.class));
            }
        });

        pending_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewHomeActivity.this,PendingOrdersActivity.class));
            }
        });


    }
}
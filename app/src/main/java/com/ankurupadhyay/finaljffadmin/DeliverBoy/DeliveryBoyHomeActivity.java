package com.ankurupadhyay.finaljffadmin.DeliverBoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.CategoryActivity;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Fragments.HomeFragment;
import com.ankurupadhyay.finaljffadmin.MainActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.PendingOrdersActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.ShowCompleteOrdersActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.TakeOrderActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ShowOrderHistroyActivity;
import com.ankurupadhyay.finaljffadmin.SplashScreenActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import io.paperdb.Paper;

public class DeliveryBoyHomeActivity extends AppCompatActivity {


    Toolbar toolbar;
    LinearLayout take_order,pending_orders,complete_orders,history;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_home);

        Paper.init(this);
        toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logout);



        logout.setVisibility(View.VISIBLE);
        Paper.book().write("type","delivery");
        pending_orders = findViewById(R.id.pending_orders);
        take_order = findViewById(R.id.take_order);
        history = findViewById(R.id.history);
        complete_orders = findViewById(R.id.complete_orders);

        sharedPreferences = getSharedPreferences(Common.SP_LOGIN,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        getSupportFragmentManager().beginTransaction().add(R.id.container,new HomeFragment()).commit();




        complete_orders.setVisibility(View.GONE);
        history.setVisibility(View.GONE);



        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveryBoyHomeActivity.this, ShowOrderHistroyActivity.class));
            }
        });



        complete_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveryBoyHomeActivity.this, ShowCompleteOrdersActivity.class));
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.clear();
                editor.commit();

                Intent intent = new Intent(DeliveryBoyHomeActivity.this, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                Toast.makeText(DeliveryBoyHomeActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();

            }
        });




        pending_orders = findViewById(R.id.pending_orders);
        take_order = findViewById(R.id.take_order);



        take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveryBoyHomeActivity.this, TakeOrderActivity.class));
            }
        });

        pending_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveryBoyHomeActivity.this, PendingOrdersActivity.class));
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout)
        {





            FirebaseMessaging.getInstance().unsubscribeFromTopic("order").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(DeliveryBoyHomeActivity.this, SplashScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                    Toast.makeText(DeliveryBoyHomeActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();

                }
            });

        }


        return super.onOptionsItemSelected(item);
    }

}
package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.DeliverBoy.DeliveryBoyHomeActivity;
import com.ankurupadhyay.finaljffadmin.Fragments.CompleteOrderFragment;
import com.ankurupadhyay.finaljffadmin.Fragments.LatestCompleteOrderFragment;
import com.ankurupadhyay.finaljffadmin.MainActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.SplashScreenActivity;

public class CompleteOrderActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        sharedPreferences = getSharedPreferences(Common.SP_LOGIN,MODE_PRIVATE);



        Intent intent = getIntent();



        //getSupportFragmentManager().beginTransaction().add(R.id.container,CompleteOrderFragment.newInstance(intent.getStringExtra("order_json"))).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container,new LatestCompleteOrderFragment()).commit();

    }



}
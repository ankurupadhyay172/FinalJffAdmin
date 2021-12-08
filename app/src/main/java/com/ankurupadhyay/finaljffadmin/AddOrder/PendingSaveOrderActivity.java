package com.ankurupadhyay.finaljffadmin.AddOrder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ankurupadhyay.finaljffadmin.Fragments.SaveOrdersFragment;
import com.ankurupadhyay.finaljffadmin.R;

public class PendingSaveOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_save_order);


       // getSupportFragmentManager().beginTransaction().add(R.id.container,new SaveOrdersFragment()).commit();

        getSupportFragmentManager().beginTransaction().add(R.id.container,new PendingSaveOrderFragment()).commit();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data!=null)
        {

            getSupportFragmentManager().beginTransaction().add(R.id.container,new PendingSaveOrderFragment()).commit();

            //note.setText(data.getStringExtra("data"));

        }


    }

}
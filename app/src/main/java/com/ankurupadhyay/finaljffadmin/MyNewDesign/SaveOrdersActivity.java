package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Fragments.SaveOrdersFragment;
import com.ankurupadhyay.finaljffadmin.R;

public class SaveOrdersActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_orders);


        getSupportFragmentManager().beginTransaction().add(R.id.container,new SaveOrdersFragment()).commit();

    }


}
package com.ankurupadhyay.finaljffadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ankurupadhyay.finaljffadmin.Fragments.GalleryFragment;

public class ManageSliderImageActivity extends AppCompatActivity {


    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_slider_image);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        getSupportFragmentManager().beginTransaction().add(R.id.container,new GalleryFragment()).commit();



    }
}
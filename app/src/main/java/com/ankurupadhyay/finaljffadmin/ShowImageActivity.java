package com.ankurupadhyay.finaljffadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ShowImageActivity extends AppCompatActivity {

    ImageView mainImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        mainImage = (ImageView)findViewById(R.id.mainimage);


        Intent intent = getIntent();


        if (intent.hasExtra("key"))
        {
            Picasso.get().load(intent.getStringExtra("key")).placeholder(R.drawable.slogo).fit().into(mainImage);
        }


    }
}
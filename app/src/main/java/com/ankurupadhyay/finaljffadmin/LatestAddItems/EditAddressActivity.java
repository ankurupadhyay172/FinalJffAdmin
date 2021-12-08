package com.ankurupadhyay.finaljffadmin.LatestAddItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.R;

import io.paperdb.Paper;

public class EditAddressActivity extends AppCompatActivity implements MyFirebase.OnUpdate {

    TextView tvTitle;
    ImageView imgBack;
    EditText discount;
    Button submit;
    OrderModel orderModel;
    MyFirebase myFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        Paper.init(this);
        handleToolbar();
        initViews();


        try {
            orderModel = Paper.book().read("complete_order_model");
            myFirebase = new MyFirebase(this);
            setData();
            handleClick();

        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void handleClick() {

    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Common.isEmpty(discount))
            {
                discount.setError("Please enter delivery address");
            }
            else
            {
                myFirebase.updateAddress(orderModel.getId(),discount.getText().toString(),EditAddressActivity.this);
            }
        }
    });

    }

    private void setData() {

        discount.setText(orderModel.getAddress());


    }


    private void handleToolbar() {

        tvTitle = findViewById(R.id.tvTitle);
        imgBack = findViewById(R.id.imgBack);
        tvTitle.setText("Manage Address");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {

        discount = findViewById(R.id.discount);
        submit = findViewById(R.id.submit);


    }

    @Override
    public void onUpdate(boolean isSuccessfull) {
        if (isSuccessfull)
        {
            Toast.makeText(this, "Address Added Successfully", Toast.LENGTH_SHORT).show();

            orderModel.setAddress(discount.getText().toString());
            Paper.book().write("complete_order_model",orderModel);
            finish();
        }

    }
}
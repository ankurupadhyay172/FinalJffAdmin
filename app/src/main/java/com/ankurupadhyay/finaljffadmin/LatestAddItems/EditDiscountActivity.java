package com.ankurupadhyay.finaljffadmin.LatestAddItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.R;

import io.paperdb.Paper;

public class EditDiscountActivity extends AppCompatActivity implements MyFirebase.OnUpdate {

    EditText discount;
    Button submit;
    OrderModel orderModel;

    MyFirebase myFirebase;

    TextView tvTitle;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discount);
        Paper.init(this);

        myFirebase = new MyFirebase(this);
        initViews();
        handleClicks();
        handleToolbar();

        orderModel = Paper.book().read("complete_order_model");

        discount.setText(orderModel.getDiscount());
    }

    private void initViews() {

        discount = findViewById(R.id.discount);
        submit = findViewById(R.id.submit);


    }

    private void handleClicks() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isEmpty(discount))
                {
                    discount.setError("Enter Discount");
                }else
                {

                myFirebase.updateDiscount(orderModel.getId(),discount.getText().toString(),EditDiscountActivity.this);
                }
            }
        });


    }

    @Override
    public void onUpdate(boolean isSuccessfull) {
        if (isSuccessfull)
        {
            Paper.init(this);
            orderModel.setDiscount(discount.getText().toString());
             Paper.book().write("complete_order_model",orderModel);
            Toast.makeText(this, "Discount added Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            Toast.makeText(this, "Somthing went wrong please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleToolbar() {

        tvTitle = findViewById(R.id.tvTitle);
        imgBack = findViewById(R.id.imgBack);
        tvTitle.setText("Discount");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
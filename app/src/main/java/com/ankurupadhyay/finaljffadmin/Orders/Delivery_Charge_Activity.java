package com.ankurupadhyay.finaljffadmin.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Delivery_Charge_Activity extends AppCompatActivity {

    TextView tv_title,submit;
    ImageView close;

    EditText delivery_charge,min_order;


    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__charge_);

        db = FirebaseFirestore.getInstance();
        delivery_charge = findViewById(R.id.delivery_charge);
        min_order = findViewById(R.id.min_order);
        tv_title = findViewById(R.id.tvTitle);
        submit = findViewById(R.id.submit);
        close = findViewById(R.id.close);


        db.collection(Common.db_Chrages).document("Jff").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful())
                {
                    delivery_charge.setText(task.getResult().get("delivery_charge").toString());
                    min_order.setText(task.getResult().get("min_order").toString());

                }


            }
        });






        tv_title.setText("Delivery Charge");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isEmpty(min_order)||Common.isEmpty(delivery_charge))
                {
                    if (Common.isEmpty(min_order))
                    min_order.setError("Should not be null");
                    if (Common.isEmpty(delivery_charge))
                        delivery_charge.setError("Should not be null");


                }
                else
                {
                    db.collection(Common.db_Chrages).document("Jff").update("delivery_charge",delivery_charge.getText().toString(),"min_order",min_order.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful())
                            {
                                Toast.makeText(Delivery_Charge_Activity.this, "Update Successfully", Toast.LENGTH_SHORT).show();

                                finish();

                            }
                            else
                            {
                                Toast.makeText(Delivery_Charge_Activity.this, "Somthing went wrong please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });








    }
}
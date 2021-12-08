package com.ankurupadhyay.finaljffadmin.Staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Model.StaffModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AddStaffActivity extends AppCompatActivity {

    TextView title,submit;
    ImageView close;

    boolean isUpdate;
    EditText name,mobile,type;

    String uid="na";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);


        Intent intent = getIntent();
        close = findViewById(R.id.close);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        type = findViewById(R.id.type);
        submit = findViewById(R.id.submit);

        title = findViewById(R.id.tvTitle);
        title.setText("Add Staff");


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        if (intent.hasExtra("json"))
        {
            isUpdate= true;
            Gson gson = new Gson();

            StaffModel model = gson.fromJson(intent.getStringExtra("json"),StaffModel.class);
            name.setText(model.getName());
            mobile.setFocusable(false);
            type.setText(model.getType());
            uid = model.getId();
            mobile.setText(model.getId());

        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isEmpty(name)||Common.isEmpty(mobile)||Common.isEmpty(type)||mobile.getText().length()!=10)
                {
                    if (Common.isEmpty(name))
                    {
                        name.setError("Please enter the name");
                    }



                    if (Common.isEmpty(mobile))
                    {
                        mobile.setError("Please enter the mobile");
                    }


                    if (Common.isEmpty(type))
                    {
                        type.setError("Please enter the type");
                    }
                    if (mobile.getText().length()!=10)
                    {
                        mobile.setError("Mobile no must be 10 digit");
                    }
                }
                else
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                    Map<String,Object> todo = new HashMap<>();
                    todo.put("name",name.getText().toString());
                    todo.put("type",type.getText().toString());


                    if (isUpdate)
                    {

                        db.collection(Common.db_Staff).document(mobile.getText().toString()).update(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(AddStaffActivity.this, "Staff updated successfully", Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            }
                        });

                    }
                    else
                    {
                        db.collection(Common.db_Staff).document(mobile.getText().toString()).set(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(AddStaffActivity.this, "Staff added successfully", Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            }
                        });

                    }





                }
            }
        });



    }
}
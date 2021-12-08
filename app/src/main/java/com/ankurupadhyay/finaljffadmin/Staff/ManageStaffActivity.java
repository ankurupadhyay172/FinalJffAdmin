package com.ankurupadhyay.finaljffadmin.Staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.StaffAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.StaffModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ManageStaffActivity extends AppCompatActivity implements MyFirebase.OngetStaff {


    ImageView back;
    RecyclerView recyclerView;

    MyFirebase myFirebase;

    List<StaffModel> list;

    TextView add;

    FirebaseFirestore db;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_staff);

        db = FirebaseFirestore.getInstance();
        add = findViewById(R.id.add);
        myFirebase = new MyFirebase(this);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myFirebase.getStaffList(this);



        add.setVisibility(View.VISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageStaffActivity.this,AddStaffActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        myFirebase.getStaffList(this);

    }

    @Override
    public void ongetStaff(List<StaffModel> list) {
        this.list = list;
        StaffAdapter adapter = new StaffAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Delete"))
        {
            db.collection(Common.db_Staff).document(list.get(item.getGroupId()).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful())
                    {
                        Toast.makeText(ManageStaffActivity.this, "Staff Deleted Successfully", Toast.LENGTH_SHORT).show();
                        updateValue();

                    }
                }
            });

        }

        if (item.getTitle().equals("Edit"))
        {
            String json = gson.toJson(list.get(item.getGroupId()));
            Intent intent = new Intent(ManageStaffActivity.this,AddStaffActivity.class);
            intent.putExtra("json",json);
            startActivity(intent);




        }

        return true;
    }

    private void updateValue() {

    myFirebase.getStaffList(this);
    }
}
package com.ankurupadhyay.finaljffadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.ProductCategory;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.CategoryModel;
import com.ankurupadhyay.finaljffadmin.Products.AddProductActivity;
import com.ankurupadhyay.finaljffadmin.Products.AddSubProducts;
import com.ankurupadhyay.finaljffadmin.Products.ProductDetailActivity;
import com.ankurupadhyay.finaljffadmin.ViewHolders.CategoryViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements MyFirebase.GetCategory {

    RecyclerView recyclerView;






    FirebaseFirestore db;
    CategoryModel model;

    FloatingActionButton fab;

    List<CategoryModel> list = new ArrayList<>();

    MyFirebase myFirebase;

    Gson gson= new Gson();

    @Override
    protected void onRestart() {
        super.onRestart();
        updateValue();
    }

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        back = findViewById(R.id.back);
        fab = findViewById(R.id.fab);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myFirebase = new MyFirebase(this);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CategoryActivity.this, AddProductActivity.class));
            }
        });




        myFirebase.getCategory(this);




    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {


        if (item.getTitle().equals("Edit"))
        {

            String json = gson.toJson(list.get(item.getGroupId()));

            Intent intent = new Intent(this,AddProductActivity.class);

            intent.putExtra("type","edit");
            intent.putExtra("json",json);

            startActivity(intent);


        }
        if (item.getTitle().equals("Delete"))
        {

            db.collection(Common.Menues_Db).document(list.get(item.getGroupId()).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful())
                    {

                        Toast.makeText(CategoryActivity.this, "Product delted successfully", Toast.LENGTH_SHORT).show();
                        updateValue();

                    }

                }
            });

        }


        return true;
    }

    private void updateValue() {

    myFirebase.getCategory(this);

    }

    @Override
    public void onGetCategory(List<CategoryModel> list) {

        this.list = list;
        ProductCategory adapter = new ProductCategory(this,list);

        recyclerView.setAdapter(adapter);

    }
}
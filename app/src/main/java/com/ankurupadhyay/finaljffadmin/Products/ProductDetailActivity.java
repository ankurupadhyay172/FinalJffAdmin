package com.ankurupadhyay.finaljffadmin.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.SpinnerAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.SpinnerModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ViewHolders.SubViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private FirestorePagingAdapter<NewMealModel, SubViewHolder> adapter;
    FirebaseFirestore db;
    NewMealModel model;
    Intent intent;
    String id = "na";
    String txt_size;
    List<String> sizelist = new ArrayList<>();

    String category;

    FloatingActionButton fab;


    Gson gson = new Gson();


    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        sizelist.add("Regular");
        sizelist.add("Medium");
        sizelist.add("Large");

        intent = getIntent();


        back = findViewById(R.id.back);

        if (intent.hasExtra("id"))
        {
            id = intent.getStringExtra("id");

            category = intent.getStringExtra("category");
            Common.temp_product_id = id;
        }



        if (id.equals("na"))
        {
            id = Common.temp_product_id;
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab = findViewById(R.id.fab);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        Query query = db.collection(Common.db_menu).document(id).collection(Common.db_menu_items).orderBy("order", Query.Direction.ASCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(2)
                .build();



        FirestorePagingOptions<NewMealModel> options = new FirestorePagingOptions.Builder<NewMealModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, snapshot -> {

                    model = snapshot.toObject(NewMealModel.class);
                    model.setId(snapshot.getId());


                    return model;

                })
                .build();


        adapter = new FirestorePagingAdapter<NewMealModel, SubViewHolder>(options) {


            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
            @Override
            protected void onBindViewHolder(@NonNull SubViewHolder holder, int position, @NonNull NewMealModel model) {

                category = model.getCategory();

                try {
                    Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(holder.image);
                }catch (IllegalArgumentException e)
                {

                }




                Gson gson = new Gson();
                final List<SpinnerModel> sizes = gson.fromJson(model.getJsize(), new TypeToken<List<SpinnerModel>>(){}.getType());
                final SpinnerAdapter adapter1 = new SpinnerAdapter(ProductDetailActivity.this,R.layout.spinner_item,sizes);



                holder.size.setText(sizes.get(0).getName());
                holder.title.setText(model.getName());
                holder.desc.setText(model.getDesc());
                holder.price.setText("₹ "+sizes.get(0).getPrice());
                txt_size = sizelist.get(0).toUpperCase();
                holder.size.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        new AlertDialog.Builder(ProductDetailActivity.this)
                                .setTitle("Select Size")
                                .setAdapter(adapter1, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        holder.size.setText(sizes.get(which).getName());
                                        // holder.title.setText(sizes.get(which).getName());
                                        holder.price.setText("₹ "+sizes.get(which).getPrice());
                                        txt_size = sizelist.get(which).toUpperCase();





                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                });





                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ProductDetailActivity.this,AddSubProducts.class);
                        intent.putExtra("type","edit");
                        intent.putExtra("json",gson.toJson(model));

                        startActivity(intent);



                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.collection(Common.db_menu).document(model.getCategory()).collection("Items").document(model.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    adapter.refresh();
                                    Toast.makeText(ProductDetailActivity.this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });


            }

            @NonNull
            @Override
            public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(ProductDetailActivity.this).inflate(R.layout.new_item_category_grid, parent, false);



                return new SubViewHolder(view);
            }



            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:

                        notifyDataSetChanged();
                        break;

                    case LOADING_MORE:
                        notifyDataSetChanged();
                        break;

                    case LOADED:
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        notifyDataSetChanged();

                        break;

                    case ERROR:

                        Toast.makeText(ProductDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        //logic here
                        break;

                    case FINISHED:
                        //logic here

                        break;
                }
            }

        };




        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductDetailActivity.this,AddSubProducts.class);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        adapter.refresh();
    }
}
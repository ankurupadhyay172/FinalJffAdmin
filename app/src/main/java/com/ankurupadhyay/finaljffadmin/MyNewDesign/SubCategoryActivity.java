package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.SpinnerAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.SpinnerModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.ViewHolders.SubViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {

    private FirestorePagingAdapter<NewMealModel, SubViewHolder> adapter;

    DataViewHolder dataViewHolder;

    FirebaseFirestore db;

    NewMealModel model;

    RecyclerView recyclerView;

    String txt_size;

    List<String> sizelist = new ArrayList<>();


    ProgressBar progressBar;

    MyDatabase myDatabase;

    LinearLayout li_cart;

    TextView cart_title,cart_price,view_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        db = FirebaseFirestore.getInstance();
        myDatabase = Room.databaseBuilder(this, MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        cart_title = findViewById(R.id.cart_title);
        cart_price = findViewById(R.id.cart_price);
        li_cart = findViewById(R.id.li_cart);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        view_cart = findViewById(R.id.view_cart);
        progressBar = findViewById(R.id.progressbar);


        sizelist.add("Regular");
        sizelist.add("Medium");
        sizelist.add("Large");

        Intent intent = getIntent();

        if (intent.hasExtra("category"))
        {

            Query query = db.collection(Common.db_menu).document(intent.getStringExtra("category")).collection(Common.db_menu_items).orderBy("order", Query.Direction.ASCENDING);



            updateData();

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


                    try {
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).into(holder.image);
                    }catch (IllegalArgumentException e)
                    {

                    }


                    Gson gson = new Gson();
                    final List<SpinnerModel> sizes = gson.fromJson(model.getJsize(), new TypeToken<List<SpinnerModel>>(){}.getType());
                    final SpinnerAdapter adapter = new SpinnerAdapter(SubCategoryActivity.this,R.layout.spinner_item,sizes);

                    if (sizes.size()>1)
                    {
                        holder.size.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.size.setVisibility(View.GONE);
                    }



                    holder.size.setText(sizes.get(0).getName());
                    holder.title.setText(model.getName());
                    holder.desc.setText(model.getDesc());
                    holder.price.setText("₹ "+sizes.get(0).getPrice());
                    txt_size = sizelist.get(0).toUpperCase();
                    holder.size.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            new AlertDialog.Builder(SubCategoryActivity.this)
                                    .setTitle("Select Size")
                                    .setAdapter(adapter, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            holder.size.setText(sizes.get(which).getName());
                                            // holder.title.setText(sizes.get(which).getName());
                                            holder.price.setText("₹ "+sizes.get(which).getPrice());
                                            txt_size = sizelist.get(which).toUpperCase();




                                            if (myDatabase.dao().getCartId(model.getId()+txt_size)!=null)
                                            {
                                                holder.txt_count.setText(String.valueOf(myDatabase.dao().getQuantity(model.getId()+txt_size)));
                                                holder.add_cart_linear.setVisibility(View.VISIBLE);
                                                holder.add_item_text.setVisibility(View.GONE);

                                            }
                                            else
                                            {
                                                holder.add_cart_linear.setVisibility(View.GONE);
                                                holder.add_item_text.setVisibility(View.VISIBLE);

                                            }

                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        }
                    });





                    if (myDatabase.dao().getCartId(model.getId()+txt_size)!=null)
                    {
                        holder.txt_count.setText(String.valueOf(myDatabase.dao().getQuantity(model.getId()+txt_size)));
                        holder.add_cart_linear.setVisibility(View.VISIBLE);
                        holder.add_item_text.setVisibility(View.GONE);

                    }


                    holder.add_item_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.add_cart_linear.setVisibility(View.VISIBLE);
                            holder.add_item_text.setVisibility(View.GONE);
                            String sprice[] = holder.price.getText().toString().split("₹ ");
                            Log.d("apunkaprice",""+Integer.parseInt(sprice[1]));

                            Gson gson = new Gson();
                            model.setPrice(""+Integer.parseInt(sprice[1]));
                            String json = gson.toJson(model);
                            CartTable cartTable = new CartTable(model.getId()+txt_size,json,1,Integer.parseInt(sprice[1]),holder.size.getText().toString());
                            Log.d("apunkadataid",""+model.getId()+txt_size);

                            myDatabase.dao().InsertCart(cartTable);
                            updateData();

                        }
                    });




                    holder.plus_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int count = myDatabase.dao().getQuantity(model.getId()+txt_size);
                            count++;
                            holder.txt_count.setText(String.valueOf(count));


                            myDatabase.dao().updateCartQuantity(count,model.getId()+txt_size);
                            updateData();
                        }
                    });



                    holder.minus_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int count = myDatabase.dao().getQuantity(model.getId()+txt_size);


                            if (count > 1) {
                                count--;
                                holder.txt_count.setText(String.valueOf(count));
                                myDatabase.dao().updateCartQuantity(count,model.getId()+txt_size);



                            }
                            else
                            {

                                myDatabase.dao().deleteItem(model.getId()+txt_size);

                                holder.add_item_text.setVisibility(View.VISIBLE);
                                holder.add_cart_linear.setVisibility(View.GONE);


                                if (myDatabase.dao().cartList().size()<1)
                                {
                                    li_cart.setVisibility(View.GONE);
                                }
                            }
                            updateData();
                        }
                    });




                }

                @NonNull
                @Override
                public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(SubCategoryActivity.this).inflate(R.layout.new_item_category_grid2, parent, false);
                    // View view = LayoutInflater.from(SubCategoryActivity.this).inflate(R.layout.item_offer, parent, false);

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
                            progressBar.setVisibility(View.GONE);
                            notifyDataSetChanged();
                            adapter.stopListening();

                            break;

                        case ERROR:

                            Toast.makeText(SubCategoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            //logic here
                            break;

                        case FINISHED:
                            //logic here
                            progressBar.setVisibility(View.GONE);
                            break;
                    }
                }

            };




            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();





            view_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SubCategoryActivity.this,SaveOrdersActivity.class));
                }
            });






        }


    }



    private void updateData() {



        if (myDatabase.dao().cartList().size()!=0) {

            int total = 0;
            for (CartTable data : myDatabase.dao().cartList()) {
                total = total + (data.getPrice() * data.getQuantity());


            }

//            if (totalprice == null) {
//                totalprice = dataViewHolder.getTotalprice(total);
//            } else {
//                totalprice.setValue(total);
//            }
            cart_price.setText("₹ "+total);
            cart_title.setText("Cart " + myDatabase.dao().cartList().size() + " Item");
            //cart_price.setText("₹ "+myDatabase.dao().cartList().get(0).getPrice());
            li_cart.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();


        if (myDatabase.dao().cartList().size()<=0)
        {
        li_cart.setVisibility(View.GONE);
        }
        else
        {
            li_cart.setVisibility(View.VISIBLE);
        }

    }
}
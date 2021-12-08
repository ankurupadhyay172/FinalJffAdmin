package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankurupadhyay.finaljffadmin.Adapters.HomeCleaningRecycleAdapter;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.Model.HomeMenuModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TakeOrderActivity extends AppCompatActivity implements MyFirebase.OnGetCategory {


    private RecyclerView recyclerView;
    private HomeCleaningRecycleAdapter bAdapter;

    MyFirebase myFirebase;

    Gson gson = new Gson();

    CircleImageView profile_image;

    TextView name,mobile;

    public static UserModel userModel;

    LinearLayout li_cart;

    MyDatabase myDatabase;


    EditText search;
    TextView view_cart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);

        myDatabase = Room.databaseBuilder(this, MyDatabase.class,"MyDb").allowMainThreadQueries().build();


        search = findViewById(R.id.search);
        li_cart = findViewById(R.id.li_cart);
        profile_image = findViewById(R.id.profile_image);
        view_cart = findViewById(R.id.view_cart);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        /*category recyclerview code is here*/

        myFirebase = new MyFirebase(this);
        recyclerView = findViewById(R.id.recyclerview);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myFirebase.getCategoryProducts(this);

        Intent intent = getIntent();
        if (intent.hasExtra("user_json"))
        {

            UserModel  model = gson.fromJson(intent.getStringExtra("user_json"),UserModel.class);

            userModel = gson.fromJson(intent.getStringExtra("user_json"),UserModel.class);
            name.setText(model.getName());
            mobile.setText(model.getMobile_no());


            try {
                Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(profile_image);
            }catch (IllegalArgumentException e)
            {

            }



        }




        view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TakeOrderActivity.this,SaveOrdersActivity.class));
            }
        });


        updateData();



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TakeOrderActivity.this,SearchProductActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateData();
    }

    private void updateData() {


        if (myDatabase.dao().cartList().size()<=0)
        {
            li_cart.setVisibility(View.GONE);
        }
        else
        {
            li_cart.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void getCategory(List<HomeMenuModel> categorylist) {
        bAdapter = new HomeCleaningRecycleAdapter(this,categorylist);
        recyclerView.setAdapter(bAdapter);

    }
}
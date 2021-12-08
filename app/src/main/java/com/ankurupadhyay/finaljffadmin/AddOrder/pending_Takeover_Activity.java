package com.ankurupadhyay.finaljffadmin.AddOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankurupadhyay.finaljffadmin.Adapters.HomeCleaningRecycleAdapter;
import com.ankurupadhyay.finaljffadmin.Adapters.PendingShowProductAdapter;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.PendingCart;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.HomeMenuModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.SaveOrdersActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.SearchProductActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.TakeOrderActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class pending_Takeover_Activity extends AppCompatActivity implements MyFirebase.OnGetCategory {
    private RecyclerView recyclerView;
    private PendingShowProductAdapter bAdapter;

    MyFirebase myFirebase;

    Gson gson = new Gson();

    CircleImageView profile_image;

    TextView name, mobile;

    public static UserModel userModel;

    LinearLayout li_cart;

    MyDatabase myDatabase;


    EditText search;
    TextView view_cart,cart_title;


    public static String order_id;

    MyOrders myOrders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__takeover_);

        Paper.init(this);
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, "MyDb").allowMainThreadQueries().build();
        myOrders = Paper.book().read("order_model");


        cart_title = findViewById(R.id.cart_title);

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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myFirebase.getCategoryProducts(this);

        Intent intent = getIntent();
        if (intent.hasExtra("user_json")) {

            UserModel model = gson.fromJson(intent.getStringExtra("user_json"), UserModel.class);


            userModel = gson.fromJson(intent.getStringExtra("user_json"), UserModel.class);
            name.setText(model.getName());
            mobile.setText(model.getMobile_no());


            try {
                Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(profile_image);
            } catch (IllegalArgumentException e) {

            }


        }


        if (intent.hasExtra("order_id"))
        {
            order_id = intent.getStringExtra("order_id");

        }





        Log.d("mypending",""+intent.getStringExtra("order_id"));


        view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(pending_Takeover_Activity.this, PendingSaveOrderActivity.class));




            }
        });


        updateData();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(pending_Takeover_Activity.this, SearchProductActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateData();
    }

    private void updateData() {


        if (myDatabase.dao().pendingcartList().size() <= 0) {
            li_cart.setVisibility(View.GONE);
        } else {
            li_cart.setVisibility(View.VISIBLE);
            cart_title.setText("Cart Has "+myDatabase.dao().pendingcartList().size()+" Items");
        }


    }


    @Override
    public void getCategory(List<HomeMenuModel> categorylist) {
        bAdapter = new PendingShowProductAdapter(this,categorylist);
        recyclerView.setAdapter(bAdapter);

    }





    public void addOrder()
    {

        Type listType = new TypeToken<List<CartTable>>(){}.getType();
        MyOrders model =myDatabase.dao().getSingleOrder(myOrders.getId());

        List<CartTable> cartTables = gson.fromJson(model.getCart_list(),listType);


        List<CartTable> list = new ArrayList<>();
        for (PendingCart cart:myDatabase.dao().pendingcartList())
        {

            CartTable cartTable = new CartTable();
            cartTable.setId(cart.getId());
            cartTable.setJson(cart.getJson());
            cartTable.setPrice(cart.getPrice());
            cartTable.setSize(cart.getSize());

            list.add(cartTable);

            //Log.d("mypending",""+cart.getJson());
        }


        cartTables.addAll(list);


        myDatabase.dao().updateCart(myOrders.getId(),gson.toJson(cartTables));
        Log.d("mypending",""+cartTables.size());

    }
}
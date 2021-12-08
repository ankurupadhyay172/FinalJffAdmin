package com.ankurupadhyay.finaljffadmin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Fragments.AnalysisFragment;
import com.ankurupadhyay.finaljffadmin.Fragments.CompleteOrderFragment;
import com.ankurupadhyay.finaljffadmin.Fragments.HomeFragment;
import com.ankurupadhyay.finaljffadmin.Fragments.ProfileFragment;
import com.ankurupadhyay.finaljffadmin.Fragments.UsersFragment;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.NewHomeActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.PendingOrdersActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.ShowCompleteOrdersActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.TakeOrderActivity;
import com.ankurupadhyay.finaljffadmin.Products.AddSubProducts;
import com.ankurupadhyay.finaljffadmin.ViewHolders.OrderViewholder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements MyFirebase.OngetPurchased, Common.OnComplete {
    ImageView add;


    BottomBar bottomBar;




    SharedPreferences sharedPreferences;

    CircleImageView profile_image;
    TextView name;

    FrameLayout sync;

    MyFirebase myFirebase;

    public static String USER_TYPE = "Admin";
    LinearLayout take_order,pending_orders,complete_orders,history;
    int i=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);

        sharedPreferences = getSharedPreferences(Common.SP_LOGIN,MODE_PRIVATE);

        myFirebase = new MyFirebase(this);

        Paper.init(this);
        profile_image = findViewById(R.id.profile_image);

        history = findViewById(R.id.history);
        complete_orders = findViewById(R.id.complete_orders);
        name = findViewById(R.id.name);
        sync = findViewById(R.id.sync);

        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        getSupportFragmentManager().beginTransaction().add(R.id.container,new HomeFragment()).commit();



//        gettotalOrders();
//



























        Paper.book().write("type","admin");
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ShowOrderHistroyActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(MainActivity.this, AddSubProducts.class));


                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });


        complete_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowCompleteOrdersActivity.class));
            }
        });




        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId==R.id.tab_mytask) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();


                }
                if (tabId==R.id.tab_smsblack)
                {


                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new UsersFragment()).commit();


                }

                if (tabId==R.id.tab_floating)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new AnalysisFragment()).commit();
                }
                if (tabId==R.id.tab_bell)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();

                }







                }
        });



        if (checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);

        }




        name.setText("Hi, "+sharedPreferences.getString(Common.SP_NAME,"NA"));

        pending_orders = findViewById(R.id.pending_orders);
        take_order = findViewById(R.id.take_order);



        take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TakeOrderActivity.class));
            }
        });

        pending_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PendingOrdersActivity.class));
            }
        });



        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettotalOrders();
            }
        });


    }

    private void gettotalOrders() {

        //myFirebase.getPurchasedOrders(this);


    }


    @Override
    public void onGetPurchased(List<OrderModel> list) {


        for (OrderModel model:list)
        {




        }





    }

    @Override
    public void onComplete(String output) {
        Toast.makeText(this, ""+output, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (i==0)
        {

            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
            i=1;
        }
        else
        super.onBackPressed();
    }
}
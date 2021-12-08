package com.ankurupadhyay.finaljffadmin.LatestAddItems;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.LatestSearchAdapter;
import com.ankurupadhyay.finaljffadmin.Adapters.SeachRecycleAdapter;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SearchProductActivity extends AppCompatActivity implements MyFirebase.OnGetSubCategory, LatestSearchAdapter.OnSearchUpdated {

    private RecyclerView recyclerView;

    LatestSearchAdapter adapter;
    MyFirebase myFirebase;

    EditText search;
    ImageView back,cross;

    MyDatabase myDatabase;

    OrderModel orderModel;

    Gson gson = new Gson();
    List<CartTable> cartTableList;
    List<com.ankurupadhyay.finaljffadmin.LocalDatabase.NewMealModel> tempt_cart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product2);
        Paper.init(this);
        tempt_cart = new ArrayList<>();

        initViews();
        getData();
        handleClick();
        cartTableList = new ArrayList<>();

        orderModel = Paper.book().read("complete_order_model");



    }

    private void handleClick() {


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0)
                {
                    cross.setVisibility(View.VISIBLE);
                }
                else
                    cross.setVisibility(View.GONE);

                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                cross.setVisibility(View.GONE);
            }
        });







        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (event.getRawX()>=(search.getRight()-search.getCompoundDrawables()[2].getBounds().width()))
                    {

                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                        if (intent.resolveActivity(getPackageManager())!=null)
                            startActivityForResult(intent,101);
                        else
                            Toast.makeText(SearchProductActivity.this, "Your device Don't Support speech input", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }



                return false;
            }
        });

    }

    private void initViews() {

        myDatabase = Room.databaseBuilder(this, MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        orderModel = Paper.book().read("complete_order_model");

        myFirebase = new MyFirebase(this);
        back = findViewById(R.id.back);
        search = findViewById(R.id.input);
        cross = findViewById(R.id.cross);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

    }

    private void getData() {

        myFirebase.getSearchData(myDatabase,this);



        adapter = new LatestSearchAdapter(this,myDatabase.dao().getSearchData(),this);

        recyclerView.setAdapter(adapter);


    }

    @Override
    public void getSubCategorylist(List<NewMealModel> list) {
        adapter = new LatestSearchAdapter(this,myDatabase.dao().getSearchData(),this);

        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101&&data!=null)
        {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            search.setText(result.get(0));
        }



    }

    @Override
    public void onGetUpdated(com.ankurupadhyay.finaljffadmin.LocalDatabase.NewMealModel model,String size,String price, int quantity) {

        Paper.init(this);
        SubCategoryModel subCategoryModel = new SubCategoryModel(model.getId(),model.getCategory(),model.getDesc(),model.getImage(),model.getName(),price,model.getJsize());



        if (quantity>0)
        {
            tempt_cart.add(model);
        }
        else
        {
            tempt_cart.remove(model);

        }
        CartTable cartTable = new CartTable(model.getId()+size,gson.toJson(subCategoryModel),quantity,Integer.parseInt(price),size);


        if (quantity>0) {

            myDatabase.dao().InsertCart(cartTable);

        }
        else
        {
            myDatabase.dao().deleteItem(model.getId()+size);

        }






    }
}
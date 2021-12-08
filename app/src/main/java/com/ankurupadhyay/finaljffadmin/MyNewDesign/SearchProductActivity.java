package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ankurupadhyay.finaljffadmin.Adapters.SeachRecycleAdapter;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.HomeMenuModel;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.R;

import java.util.ArrayList;
import java.util.List;

public class SearchProductActivity extends AppCompatActivity implements MyFirebase.OnGetCategory, MyFirebase.OnGetSubCategory {


    private RecyclerView recyclerView;
    private SeachRecycleAdapter bAdapter;

    EditText input;

    ImageView cross,back;



    MyFirebase myFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);








        myFirebase = new MyFirebase(this);

        cross = findViewById(R.id.cross);
        back = findViewById(R.id.back);
        input = findViewById(R.id.input);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchProductActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        myFirebase.getCategoryProducts(this);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
            }
        });

        input.addTextChangedListener(new TextWatcher() {
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

                bAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }

    @Override
    public void getCategory(List<HomeMenuModel> categorylist) {

        List<NewMealModel> list = new ArrayList<>();
        for (HomeMenuModel model : categorylist)
        {
            myFirebase.getAllData(model.getId(),list,this);
        }


    }


    @Override
    public void getSubCategorylist(List<NewMealModel> list) {


        bAdapter = new SeachRecycleAdapter(SearchProductActivity.this,list);
        recyclerView.setAdapter(bAdapter);
        Log.d("myallitems",""+list.size());
    }
}

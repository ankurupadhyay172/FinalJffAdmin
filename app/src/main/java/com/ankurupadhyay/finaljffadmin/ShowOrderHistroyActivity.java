package com.ankurupadhyay.finaljffadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.HistoryAdapter;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.HistoryModel;
import com.ankurupadhyay.finaljffadmin.Model.NewOrderModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.ViewHolders.HistoryViewHolder;
import com.ankurupadhyay.finaljffadmin.ViewHolders.OrderViewholder;
import com.ankurupadhyay.finaljffadmin.ViewHolders.UserViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ShowOrderHistroyActivity extends AppCompatActivity implements MyFirebase.OngetPurchased {

    private HistoryAdapter historyAdapter;
    private ArrayList<HistoryModel> historyModelArrayList;
    private RecyclerView recyclerView;

    String[] txtcanceled = {"Canceled", "Pending", "Approved"};

    TextView tv_title;

    MyFirebase myFirebase;

    ImageView imgOptions;

    LinearLayout mainlayout;

    private FirestorePagingAdapter<OrderModel, HistoryViewHolder> adapter;

    FirebaseFirestore db;

    OrderModel model;
    ProgressBar progressBar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Gson gson = new Gson();

    TextView total_amount;


    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_histroy);





        sharedPreferences = getSharedPreferences(Common.SP_LOGIN, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        myFirebase = new MyFirebase(this);

        db = FirebaseFirestore.getInstance();


        img_back = findViewById(R.id.imgBack);
        tv_title = findViewById(R.id.tvTitle);
        imgOptions = findViewById(R.id.imgOptions);
        mainlayout = findViewById(R.id.mainlayout);

        total_amount = findViewById(R.id.total_amount);
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.rv_timer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        historyModelArrayList = new ArrayList<>();




        for (int i = 0; i < txtcanceled.length; i++) {
            HistoryModel view1 = new HistoryModel(txtcanceled[i]);
            historyModelArrayList.add(view1);
        }



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgOptions.setVisibility(View.VISIBLE);

        tv_title.setText("History");


        getHistoryData("All");


        imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ShowOrderHistroyActivity.this);
                builder.setTitle("Select an Option");

                final View view1 = LayoutInflater.from(ShowOrderHistroyActivity.this).inflate(R.layout.select_language, mainlayout,false);

                RadioButton all = (RadioButton)view1.findViewById(R.id.all);

                RadioButton day = view1.findViewById(R.id.day);
                RadioButton week = (RadioButton)view1.findViewById(R.id.week);
                RadioButton month = (RadioButton)view1.findViewById(R.id.month);
                RadioGroup radioGroup = (RadioGroup)view1.findViewById(R.id.choose_language);


                if (sharedPreferences.getString(Common.SP_SELECTED_OPTION,"na").equals("Last 2 Days"))
                {
                    day.setChecked(true);


                }
                else if (sharedPreferences.getString(Common.SP_SELECTED_OPTION,"na").equals("Last 7 Days"))
                {
                    week.setChecked(true);
                }else
                if (sharedPreferences.getString(Common.SP_SELECTED_OPTION,"na").equals("Last 30 Days"))
                {
                    month.setChecked(true);
                }else
                {
                    all.setChecked(true);
                }


                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {




                        RadioButton radioButton = (RadioButton)view1.findViewById(checkedId);

                        editor.putString(Common.SP_SELECTED_OPTION,radioButton.getText().toString());
                        editor.commit();

                        getHistoryData(radioButton.getText().toString());



                        }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });



                builder.setView(view1);




                builder.show();




            }
        });


       // myFirebase.getPurchasedOrders(this);
    }

    @Override
    public void onGetPurchased(List<OrderModel> list) {

        historyAdapter = new HistoryAdapter(this, list);
        recyclerView.setAdapter(historyAdapter);


    }



    public void getHistoryData(String data)
    {


        Query query;



        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());




        if (data.equals("Last 2 Days"))
        {
            cal.add(Calendar.DAY_OF_YEAR, 0);
            Date daysBeforeDate = cal.getTime();
            query = db.collection(Common.Purchased_Db).whereGreaterThanOrEqualTo("timestamp",daysBeforeDate).orderBy("timestamp", Query.Direction.DESCENDING);
            myFirebase.getTotalAmount(total_amount,daysBeforeDate);

        }else if (data.equals("Last 7 Days"))
        {
            cal.add(Calendar.DAY_OF_YEAR, -7);
            Date daysBeforeDate = cal.getTime();
            query = db.collection(Common.Purchased_Db).whereGreaterThanOrEqualTo("timestamp",daysBeforeDate).orderBy("timestamp", Query.Direction.DESCENDING);
            myFirebase.getTotalAmount(total_amount,daysBeforeDate);
        }else if (data.equals("Last 30 Days"))
        {
            cal.add(Calendar.DAY_OF_YEAR, -30);
            Date daysBeforeDate = cal.getTime();
            query = db.collection(Common.Purchased_Db).whereGreaterThanOrEqualTo("timestamp",daysBeforeDate).orderBy("timestamp", Query.Direction.DESCENDING);
            myFirebase.getTotalAmount(total_amount,daysBeforeDate);
        }
        else
        {
            query = db.collection(Common.Purchased_Db).orderBy("timestamp", Query.Direction.DESCENDING);
            myFirebase.getTotalAmount(total_amount,null);
        }











        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(6)
                .setPageSize(6)
                .build();



        FirestorePagingOptions<OrderModel> options = new FirestorePagingOptions.Builder<OrderModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, snapshot -> {

                    model = snapshot.toObject(OrderModel.class);
                    model.setId(snapshot.getId());


                    return model;

                })
                .build();



        adapter = new FirestorePagingAdapter<OrderModel, HistoryViewHolder>(options) {


            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder holder, int position, @NonNull OrderModel model) {


                String order_name = "";
                SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy hh:mm aa", Locale.getDefault());
                holder.date.setText(""+df.format(model.getTimestamp().toDate()));

                holder.user_name.setText(model.getUser_name());
                holder.total_amount.setText("₹ "+model.getTotal_price());


                Type listType = new TypeToken<List<CartTable>>(){}.getType();
                List<CartTable> cartTableList = gson.fromJson(model.getOrder_json(),listType);


                Log.d("mylogvalue236",""+model.getTotal_price());


                for (CartTable list:cartTableList) {

                    NewOrderModel model1 = gson.fromJson(list.getJson(),NewOrderModel.class);
                    order_name = order_name+model1.getName()+"\n";
                }
                holder.order.setText(order_name);

                if (model.getStatus().equals("Delivered"))
                {
                    holder.txtcanceled.setText("Successfull");
                    holder.txtcanceled.setTextColor(Color.parseColor("#ff2e63"));
                    holder.iv_circle.setColorFilter(Color.parseColor("#ff2e63"));

                    holder.status_layout.setBackgroundColor(getResources().getColor(R.color.status_success));

                }else
                if (model.getStatus().equals("Failed"))
                {
                    holder.txtcanceled.setText("Failed");
                    holder.txtcanceled.setTextColor(Color.parseColor("#e84b4b"));
                    holder.iv_circle.setColorFilter(Color.parseColor("#e84b4b"));
                    holder.status_layout.setBackgroundColor(getResources().getColor(R.color.status_failed));
                }
                else
                {
                    holder.txtcanceled.setText("Pending");
                    holder.txtcanceled.setTextColor(Color.parseColor("#fbbd36"));
                    holder.iv_circle.setColorFilter(Color.parseColor("#fbbd36"));
                    holder.status_layout.setBackgroundColor(getResources().getColor(R.color.status_pending));
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(ShowOrderHistroyActivity.this, ShowSingleOrderDetailActivity.class);
                        intent.putExtra("id",model.getId());
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);

                return new HistoryViewHolder(view);
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

                        Toast.makeText(ShowOrderHistroyActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    }
}






package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.CompleteOrdersAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.LatestAddItems.SearchProductActivity;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowCompleteOrdersActivity extends AppCompatActivity {



    /*------Variable Define----*/
    ImageView imgBack;
    TextView tvTitle;

    /*ManageAddress Data is here*/
    private RecyclerView recyclerView;


    private Integer imgOffice[] = {R.drawable.ic_office_building,R.drawable.ic_home};
    private String tvOffices[] = {"Office","Home"};
    private String tvAddress[] = {"601-602 Galav chamber, dairyman,sayajigunj,  Alkapuri, Vadodara, 390011",
            "601-602 Galav chamber, dairyman,sayajigunj,  Alkapuri, Vadodara, 390011"};


    CompleteOrdersAdapter adapter;

    FirebaseFirestore db;
    TextView today_sale,txt_online_payment,txt_cash_payment,txt_total_amount;


    ImageView left_arrow,right_arrow;
    FrameLayout date_filter;

    ProgressBar progressBar;

    int counter =0;
    LinearLayout li_empty;

    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complete_orders);

        db = FirebaseFirestore.getInstance();



        /*-------Status Color Code To Change--------*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.myblue));
        }
        left_arrow = findViewById(R.id.left_arrow);
        right_arrow = findViewById(R.id.right_arrow);
        date_filter = findViewById(R.id.date_filter);
        imgBack = findViewById(R.id.imgBack);
        txt_online_payment = findViewById(R.id.online_payment);
        txt_cash_payment = findViewById(R.id.cash_payment);
        txt_total_amount = findViewById(R.id.total_amount);
        progressBar = findViewById(R.id.progress_bar);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvTitle = findViewById(R.id.tvTitle);
        li_empty = findViewById(R.id.li_empty);
        tvTitle.setText("Today's Orders");
        today_sale = findViewById(R.id.today_sale);

        /*ManageAddress reyclcerview code is here*/

        recyclerView = findViewById(R.id.rvMangeAddress);


        recyclerView.setLayoutManager(new LinearLayoutManager(ShowCompleteOrdersActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());






        handleDate();
        handleDrawableArrow();



        getCompleteOrders();





    }


    private void handleDate() {


        tvTitle.setText(Common.getDateFromFireStore(Timestamp.now()));


        date_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(ShowCompleteOrdersActivity.this, datePickerListener, mYear, mMonth, mDay);
                dateDialog.show();

            }
            private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    Calendar c2 = Calendar.getInstance();
                    c2.set(year,monthOfYear,dayOfMonth);

                    Log.d("mydatecalendar",""+dayOfMonth);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    dateFormat.setCalendar(c2);
                    String dateYouChoosed = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                    tvTitle.setText(dateFormat.format(c2.getTime()));
                    getCompleteOrders();


                }
            };
        });



    }

    private void getCompleteOrders() {


        progressBar.setVisibility(View.VISIBLE);





        try {

            String date = tvTitle.getText().toString();

          //  Date date2 = new SimpleDateFormat("dd MMM yyyy").parse(tvTitle.getText().toString());

            String[] date2 = tvTitle.getText().toString().split("-");
            String date_second = date2[0]+" "+date2[1]+" "+date2[2];

            //Jun 1, 2021

            String date3 = date2[1]+" "+date2[0]+", "+date2[2];


            db.collection(Common.Purchased_Db).whereEqualTo("status","Delivered").
                    whereIn("order_date", Arrays.asList(date_second,date,date3)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful())
                    {
                        List<OrderModel> list = new ArrayList<>();

                        for (QueryDocumentSnapshot document:task.getResult())
                        {
                            SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy", Locale.getDefault());





                            try {
                                OrderModel model = document.toObject(OrderModel.class);
                                Log.d("mydatecompare",""+model.getTimestamp().toDate());


                                model.setId(document.getId());
                                if (Common.getDateFromFireStore(model.getTimestamp()).equals(tvTitle.getText().toString()))
                                    list.add(model);

                                Log.d("mylogdata235",""+df.format(model.getTimestamp().toDate())+"\ncurrent date"+df.format(Timestamp.now().toDate()));


                            }catch (Exception e)
                            {


                            }




                        }


                        WriteBatch batch = db.batch();
                        int cart_total =0;
                        int online_payment=0;
                        int offline_payment =0;
                        for (OrderModel model:list)
                        {

                            Type listType = new TypeToken<List<CartTable>>(){}.getType();
                            List<CartTable> cartTables = gson.fromJson(model.getOrder_json(),listType);
                            int total_price =0;
                            for (CartTable cartTable:cartTables)
                            {
                                total_price =total_price+(cartTable.getPrice()*cartTable.getQuantity());


                            }
                            total_price = total_price+Integer.parseInt(model.getDelivery_charge());
                            try {
                                total_price = total_price-Integer.parseInt(model.getDiscount());

                            }catch (Exception e)
                            {

                            }
                            Log.d("mydiscount",""+total_price);


                            cart_total =cart_total+total_price;



                            if (cart_total!=Integer.parseInt(model.getTotal_price()))
                            {
                                DocumentReference document = db.collection(Common.Purchased_Db).document(model.getId());
                                batch.update(document,"total_price",""+total_price);

                            }




                            if (model.getPayment_method().equals("Online")||model.getPayment_method().equals("Online On Delivery"))
                            {

                                online_payment = online_payment+Integer.parseInt(model.getTotal_price());

                            }
                            else
                            {
                                offline_payment = offline_payment+Integer.parseInt(model.getTotal_price());


                            }

                        }

                        txt_total_amount.setText(""+cart_total);
                        txt_online_payment.setText("Rec. Online = ₹ "+online_payment);
                        txt_cash_payment.setText("Rec. Cash =  ₹ "+offline_payment);



                        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });





                        //sales_total = price_total-delivery_total;
                        // sales_total = price_total-total_discount;




                        adapter = new CompleteOrdersAdapter(ShowCompleteOrdersActivity.this,list);
                        recyclerView.setAdapter(adapter);



                        if (list.size()==0)
                        {
                            li_empty.setVisibility(View.VISIBLE);
                        }
                        else
                            li_empty.setVisibility(View.GONE);


                        progressBar.setVisibility(View.GONE);

                    }
                    else
                    {
                        Toast.makeText(ShowCompleteOrdersActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d("myerrorlog23",""+task.getException().getMessage());
                    }


                }
            });

        }catch (Exception e)
        {

        }





    }

    private void handleDrawableArrow() {

        Date date = Timestamp.now().toDate();

      left_arrow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              counter--;
              Calendar calendar = Calendar.getInstance();
              calendar.setTime(date);
              calendar.add(Calendar.DATE,counter);

              SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
              dateFormat.setCalendar(calendar);

             tvTitle.setText(dateFormat.format(calendar.getTime()));

             getCompleteOrders();
          }
      });

      right_arrow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              counter++;
              Calendar calendar = Calendar.getInstance();
              calendar.setTime(date);


              calendar.add(Calendar.DATE,counter);

              SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
              dateFormat.setCalendar(calendar);
              tvTitle.setText(dateFormat.format(calendar.getTime()));
              getCompleteOrders();
          }
      });


    }
}
package com.ankurupadhyay.finaljffadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.NewOrderModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.List;

public class ShowSingleOrderDetailActivity extends AppCompatActivity implements MyFirebase.OnGetSingleOrder {



    public ImageView img_bg,imgBack;
    public TextView txt_orderNo,txt_date_time,txt_way,txt_item_count,txt_dollar,delivery_charge,total_price,uname,ucontact,uaddress,payment_status,payment_method,date;

    TextView tvTitle;
    MyFirebase myFirebase;

    Gson gson = new Gson();

    TextView total_discount,after_discount;
    int discount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_order_detail);

        myFirebase = new MyFirebase(this);

        after_discount = findViewById(R.id.after_discount);
        total_discount = findViewById(R.id.total_discount);
        imgBack = findViewById(R.id.imgBack);
        tvTitle = findViewById(R.id.tvTitle);
        uname = findViewById(R.id.uname);
        ucontact = findViewById(R.id.ucontact);
        uaddress = findViewById(R.id.uaddress);
        img_bg = findViewById(R.id.img_bg);
        txt_orderNo = findViewById(R.id.txt_orderNo);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_way = findViewById(R.id.txt_way);
        txt_item_count = findViewById(R.id.txt_item_count);
        txt_dollar = findViewById(R.id.txt_dollar);
        delivery_charge = findViewById(R.id.delivery_charge);
        total_price = findViewById(R.id.total_price);
        payment_status = findViewById(R.id.payment_status);
        payment_method = findViewById(R.id.payment_method);
        date = findViewById(R.id.date);









        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Intent intent = getIntent();

        if (intent.hasExtra("id"))
        {
            Log.d("mylogvalues",""+intent.getStringExtra("id"));


            myFirebase.getSingleOrderData(intent.getStringExtra("id"),this);


        }



    }

    @Override
    public void getSingleOrder(OrderModel model) {








        Type listType = new TypeToken<List<CartTable>>(){}.getType();



        List<CartTable> cartTableList = gson.fromJson(model.getOrder_json(),listType);

        int pos=0,mytotal=0;

        String final_order = "";

        for (CartTable list:cartTableList)
        {
            pos = pos+1;


            NewOrderModel model1 = gson.fromJson(list.getJson(),NewOrderModel.class);
            Log.d("apunkilist",""+cartTableList.size()+"price"+model1.getPrice());

            final_order = final_order+pos+". Category : "+model1.getCategory()+ "\nOrder : ("+list.getQuantity()+" "+list.getSize()+" )"+model1.getName()+" (₹ "+list.getPrice()+")\n\n";




            try {
                mytotal = mytotal+list.getPrice()*list.getQuantity();


                Log.d("myprice",""+list.getPrice());

            }
            catch (Exception e)
            {
                Log.d("apunkivalue",e.getMessage());

            }
        }



        tvTitle.setText(""+model.getUser_name());
        String mydate = DateFormat.getDateInstance().format(model.getTimestamp().toDate());


        String ordertime = DateFormat.getTimeInstance(DateFormat.SHORT).format(model.getTimestamp().toDate());



        date.setText(""+mydate+" , "+ordertime);
        txt_orderNo.setText(final_order);
        uname.setText(model.getUser_name().toUpperCase());
        ucontact.setText(model.getUser_id());
        uaddress.setText(model.getAddress());
        txt_item_count.setText("Items X "+cartTableList.size());
        txt_dollar.setText("₹ "+mytotal);
        txt_date_time.setText(Common.gethours(model.getTimestamp().getSeconds()));
        txt_way.setText("Delivery Status : "+model.getStatus());


        if (model.isIspayment())
        {
            payment_status.setText("PAID");
            payment_method.setText(model.getPayment_method());
        }
        else
        {
            payment_status.setText("NOT PAID");
            payment_method.setText(model.getPayment_method());
        }






        delivery_charge.setText("₹ "+model.getDelivery_charge());

            try {
                if (model.getDiscount()!=null||!model.getDiscount().equals("")) {
                    total_discount.setText("Total Discount = - ₹ "+model.getDiscount());
                    discount = Integer.parseInt(model.getDiscount());

                }

            }catch (Exception e)
            {
                total_discount.setText("Total Discount = - ₹ 0");
                discount =0;
            }


        total_price.setText(" = ₹ "+(Integer.parseInt(model.getTotal_price())));


        after_discount.setText("After Discount = ₹"+(Integer.parseInt(model.getTotal_price())-discount));



        if (model.getStatus().equals("Successfull"))
        {
            img_bg.setImageResource(R.drawable.rectangle_left_cure_green);
        }
        if (model.getStatus().equals("Cancelled"))
        {
            img_bg.setImageResource(R.drawable.rectangle_left_cure_red);
        }





        ucontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+model.getUser_id()));

                startActivity(intent);
            }
        });
















    }
}
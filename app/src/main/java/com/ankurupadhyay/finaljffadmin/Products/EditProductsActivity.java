package com.ankurupadhyay.finaljffadmin.Products;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ankurupadhyay.finaljffadmin.Adapters.LatestCartAdapter;

import com.ankurupadhyay.finaljffadmin.Adapters.PendingUserAdapter;
import com.ankurupadhyay.finaljffadmin.AddInstructionsActivity;
import com.ankurupadhyay.finaljffadmin.AddOrder.PendingSaveOrderActivity;
import com.ankurupadhyay.finaljffadmin.AddOrder.pending_Takeover_Activity;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.DeliverBoy.DeliveryBoyHomeActivity;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.LatestAddItems.EditAddressActivity;
import com.ankurupadhyay.finaljffadmin.LatestAddItems.EditDiscountActivity;
import com.ankurupadhyay.finaljffadmin.LatestAddItems.SearchProductActivity;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.NewMealModel;
import com.ankurupadhyay.finaljffadmin.MainActivity;
import com.ankurupadhyay.finaljffadmin.Model.DeliveryModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.PendingOrdersActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.ShowCompleteOrdersActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;

public class EditProductsActivity extends AppCompatActivity implements LatestCartAdapter.OnUpdateQuantity, MyFirebase.OnUpdate, View.OnClickListener, MyFirebase.OnGetDelivery, MyFirebase.OnGetCompleted {

    OrderModel orderModel;
    public static String TAG = "Editproducrtag";
    TextView delivery_address;

    RecyclerView recyclerView;

    LatestCartAdapter adapter;
    Gson gson = new Gson();

    MyFirebase myFirebase;
    LinearLayout liCash,liPayCash,liPayOnline,litakeaway,lidelivery;
    RadioButton rbCash,rbPayCash,rbPayOnline,rbdelivery,rbtakeaway;
    TextView cart_total,delivery_charge,total,total_item,total_cost,discount,edt_discount,tvTitle;

    ImageView imgBack;
    Button checkout,add_item;
    List<CartTable> cartTableList;

    MyDatabase myDatabase;

    int total_amount=0;

    ImageView edit_address;

    UserModel userModel;

    ProgressBar progress_bar2;

    boolean payment_selected;
    SharedPreferences sharedPreferences;

    TextView edt_note1,edt_table_name;

    ImageView img_note,img_table_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products);

        sharedPreferences = getSharedPreferences(Common.SP_LOGIN,MODE_PRIVATE);

        myDatabase = Room.databaseBuilder(this, MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        initViews();
        setData();
        getTotalPrice();
        handleDelivery();
        handleToolbar();
        handleCheckout();


        liCash.setOnClickListener(this);
        liPayCash.setOnClickListener(this);
        liPayOnline.setOnClickListener(this);

        lidelivery.setOnClickListener(this);
        litakeaway.setOnClickListener(this);



        rbtakeaway.setOnClickListener(this);
        rbdelivery.setOnClickListener(this);

    }

    private void setData() {

        Paper.init(this);
        orderModel = Paper.book().read("complete_order_model");


        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(EditProductsActivity.this, pending_Takeover_Activity.class);
                Intent intent = new Intent(EditProductsActivity.this, SearchProductActivity.class);
                intent.putExtra("order_id",orderModel.getId());
                startActivity(intent);
            }
        });


        try {
            delivery_address.setText(orderModel.getAddress());

            Type listType = new TypeToken<List<CartTable>>(){}.getType();
            cartTableList = gson.fromJson(orderModel.getOrder_json(),listType);
            adapter = new LatestCartAdapter(this,cartTableList,this);
            recyclerView.setAdapter(adapter);


        }catch (Exception e)
        {

        }


    }

    private void initViews() {

        edt_note1 = findViewById(R.id.edt_note1);
        img_note = findViewById(R.id.img_note);
        edt_table_name = findViewById(R.id.edt_table_name);
        img_table_no = findViewById(R.id.img_table_no);



        Paper.init(this);
        myFirebase = new MyFirebase(this);
        orderModel = Paper.book().read("complete_order_model");

        try {
            userModel = gson.fromJson(orderModel.getUser_model(),UserModel.class);
        }catch (Exception e)
        {

        }





        Log.d(TAG, "initViews: "+orderModel.getOrder_json());

        delivery_address = findViewById(R.id.txt_addressCheck);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        progress_bar2 = findViewById(R.id.progress_bar2);
        edt_discount = findViewById(R.id.edt_discount);

        add_item = findViewById(R.id.add_item);
        imgBack = findViewById(R.id.imgBack);
        cart_total = findViewById(R.id.cart_total);
        delivery_charge = findViewById(R.id.delivery_charge);
        liCash = findViewById(R.id.liCash);
        liPayCash = findViewById(R.id.liPayCash);
        liPayOnline = findViewById(R.id.liPayOnline);

        total = findViewById(R.id.total);
        rbCash = findViewById(R.id.rbCash);
        rbPayCash = findViewById(R.id.rbPayCash);
        rbPayOnline = findViewById(R.id.rbPayOnline);
        total_item = findViewById(R.id.total_item);
        total_cost = findViewById(R.id.total_cost);
        checkout = findViewById(R.id.checkout);

        litakeaway = findViewById(R.id.litakeaway);
        lidelivery = findViewById(R.id.lidelivery);
        rbdelivery = findViewById(R.id.rbdelivery);
        rbtakeaway = findViewById(R.id.rbtakeaway);
        discount = findViewById(R.id.discount);
        edit_address = findViewById(R.id.edit_address);




        edt_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProductsActivity.this, EditDiscountActivity.class));
            }
        });
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EditProductsActivity.this, EditAddressActivity.class));
            }
        });



        img_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductsActivity.this, AddInstructionsActivity.class);

                startActivityForResult(intent,102);
            }
        });
        edt_note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductsActivity.this, AddInstructionsActivity.class);

                startActivityForResult(intent,102);
            }
        });





        img_table_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductsActivity.this, AddInstructionsActivity.class);
                intent.putExtra("type","table_no");
                intent.putExtra("table_no",orderModel.getTable_no());
                startActivityForResult(intent,104);
            }
        });
        edt_table_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductsActivity.this, AddInstructionsActivity.class);
                intent.putExtra("type","table_no");
                intent.putExtra("table_no",orderModel.getTable_no());
                startActivityForResult(intent,104);
            }
        });





        if (orderModel.getMessage()!=null)
        edt_note1.setText(orderModel.getMessage());



        if (orderModel.getTable_no()!=null)
            edt_table_name.setText(orderModel.getTable_no());

        edt_note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductsActivity.this, AddInstructionsActivity.class);

                startActivityForResult(intent,102);
            }
        });

        img_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductsActivity.this, AddInstructionsActivity.class);

                startActivityForResult(intent,102);
            }
        });
    }

    @Override
    public void onUpdateQuantity(List<CartTable> list) {

        myFirebase.updateCartList(orderModel.getId(),gson.toJson(list),this);

    }

    @Override
    public void onUpdate(boolean isSuccessfull) {
        myDatabase.dao().clearCart();
        getTotalPrice();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.liCash:
                payment_selected = true;
                rbCash.setChecked(true);
                rbPayCash.setChecked(false);
                rbPayOnline.setChecked(false);
                orderModel.setPayment_method("Online");
                Paper.book().write("complete_order_model",orderModel);
                Paper.book().write("payment_method","Online");
                getTotalPrice();
                break;
            case R.id.liPayCash:
                payment_selected = true;
                rbCash.setChecked(false);
                rbPayCash.setChecked(true);
                rbPayOnline.setChecked(false);
                orderModel.setPayment_method("COD");
                Paper.book().write("complete_order_model",orderModel);
                Paper.book().write("payment_method","COD");
                getTotalPrice();
                break;
            case R.id.liPayOnline:
                payment_selected = true;
                rbCash.setChecked(false);
                rbPayCash.setChecked(false);
                rbPayOnline.setChecked(true);
                orderModel.setPayment_method("Online On Delivery");
                Paper.book().write("complete_order_model",orderModel);
                Paper.book().write("payment_method","Online On Delivery");
                getTotalPrice();
                break;

            case R.id.lidelivery:
            case R.id.rbdelivery:

                rbdelivery.setChecked(true);
                rbtakeaway.setChecked(false);
                orderModel.setIstakeaway(false);
                Paper.book().write("complete_order_model",orderModel);
                myFirebase.getDelivery_charge(this);
                break;

            case R.id.litakeaway:

            case R.id.rbtakeaway:
                orderModel.setDelivery_charge("0");
                orderModel.setIstakeaway(true);
                Paper.book().write("complete_order_model",orderModel);
                getTotalPrice();
                rbtakeaway.setChecked(true);
                rbdelivery.setChecked(false);
                break;


        }
    }

    private void getTotalPrice() {

        Paper.init(this);
        orderModel = Paper.book().read("complete_order_model");

        int tot_price=0;
        for (CartTable table:cartTableList)
        {
            tot_price = tot_price+(table.getPrice()*table.getQuantity());


        }

        total_item.setText(""+cartTableList.size());

        int final_toal = Integer.parseInt(orderModel.getDelivery_charge())+tot_price;
        total_amount = (int)final_toal;


        cart_total.setText("₹ "+tot_price);
        delivery_charge.setText("₹ "+orderModel.getDelivery_charge());


        if (orderModel.getDiscount()==null||orderModel.getDiscount().equals(""))
        {
            discount.setText("₹ 0");

        }else
        {
            try {
                final_toal = final_toal-Integer.parseInt(orderModel.getDiscount());
                discount.setText("₹ "+orderModel.getDiscount());

            }catch (Exception e)
            {

            }

        }

        total.setText("₹ "+final_toal);
        total_cost.setText("₹ "+final_toal);


        if (orderModel.getPayment_method().toLowerCase().trim().equals("cod"))
        {
            payment_selected = true;
            rbCash.setChecked(false);
            rbPayCash.setChecked(true);
            rbPayOnline.setChecked(false);
        }
        if (orderModel.getPayment_method().equals("Online On Delivery"))
        {
            payment_selected = true;
            rbCash.setChecked(false);
            rbPayCash.setChecked(false);
            rbPayOnline.setChecked(true);
        }
        if (orderModel.getPayment_method().toLowerCase().trim().equals("online"))
        {
            payment_selected = true;
            rbCash.setChecked(true);
            rbPayCash.setChecked(false);
            rbPayOnline.setChecked(false);

        }



        try {
            delivery_address.setText(orderModel.getAddress());
        }catch (Exception e)
        {

        }
        myFirebase.updateTotalPrice(orderModel.getId(),""+final_toal,orderModel.getDelivery_charge(),orderModel.isIstakeaway(),orderModel.getPayment_method());
    }

    private void handleDelivery() {


        if (orderModel.isIstakeaway())
        {

            rbdelivery.setChecked(false);
            rbtakeaway.setChecked(true);
        }
        else
        {
            rbdelivery.setChecked(true);
            rbtakeaway.setChecked(false);
        }



    }

    @Override
    protected void onRestart() {
        super.onRestart();


            try {

            if (myDatabase.dao().cartList().size()>0)
            {


                cartTableList.addAll(myDatabase.dao().cartList());
                myFirebase.updateCartList(orderModel.getId(),gson.toJson(cartTableList),this);
                adapter = new LatestCartAdapter(this,cartTableList,this);
                recyclerView.setAdapter(adapter);

            }

            }catch (Exception e)
            {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            getTotalPrice();
            //setData();




    }

    @Override
    public void onGetDeliveryCharge(DeliveryModel model) {

        int min_order = Integer.parseInt(model.getMin_order());

        if (total_amount>=min_order)
        {
            orderModel.setDelivery_charge("0");
        }
        else
        {
            orderModel.setDelivery_charge(model.getDelivery_charge());


        }
        Paper.book().write("complete_order_model",orderModel);
        getTotalPrice();

    }

    private void handleToolbar() {

        tvTitle = findViewById(R.id.tvTitle);
        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void handleCheckout() {

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payment_selected)
                {
                    checkout.setVisibility(View.GONE);
                    progress_bar2.setVisibility(View.VISIBLE);
                    orderModel.setIspayment(true);

                    String date = Common.getDateFromFireStore(orderModel.getTimestamp());

                    myFirebase.updateStatus(orderModel.getId(),"Delivered",date,EditProductsActivity.this);

                }
                    else
                {
                    Toast.makeText(EditProductsActivity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onGetCompleted(boolean status) {
        if (status)
        {
            ArrayList<String> data = new ArrayList<>();
            String full_message = "Hi, "+userModel.getName()+"%0aWelcome to JAIN FAST FOOD%0a Your order is :-\n";
            data.add(full_message);
            for (CartTable table:cartTableList)
            {
                SubCategoryModel model = gson.fromJson(table.getJson(), SubCategoryModel.class);


                Log.d("mysize",""+table.getQuantity());

                int total_amount = Integer.parseInt(model.getPrice())+table.getExtra();

                data.add(model.getName()+" ( "+total_amount+" X "+table.getQuantity() +") = "+(Integer.parseInt(model.getPrice())*table.getQuantity())+"\n");

            }
            data.add("Total Amount = Rs. "+total_amount);
            data.add("\nHope you enjoy the food !\nHave a good day !%0a");

            String last_message="";
            for (String message:data)
            {
                last_message += message+"%0a";
            }




            CompositeDisposable compositeDisposable = new CompositeDisposable();

            Common.sendnotificationmethod("Congratulation","Your order is completed. Hope you enjoy the food",compositeDisposable,orderModel.getToken());
            Toast.makeText(this, "Order Completed Successfully", Toast.LENGTH_SHORT).show();
            sendSmsToUser(userModel.getMobile_no(),last_message);

            Intent intent;
            if (sharedPreferences.getString(Common.SP_TYPE,"na").equals("Admin"))
            {
                intent = new Intent(this, MainActivity.class);
            }else
            {
                intent = new Intent(this, DeliveryBoyHomeActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else
        {
            Toast.makeText(this, "Somthing went wrong please try again", Toast.LENGTH_SHORT).show();
            checkout.setVisibility(View.VISIBLE);
            progress_bar2.setVisibility(View.GONE);

        }
    }




    private void sendSmsToUser(String mobile,String message){
        //Showing the progress dialog
        // final ProgressDialog loading = ProgressDialog.show(Student_entry.this,"Uploading...","Please wait...",false,false);

        String url = "http://fastsms.fastsmsindia.com/api/sendhttp.php?authkey=37124AeevEybx60544db6P30&mobiles="+mobile+"&message="+message+"&sender=NEWMSG&route=6&country=0";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        Log.d("mylogresponse",s);





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {




                        //Showing toast

                        try {

                            Toast.makeText(EditProductsActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }catch (NullPointerException e)
                        {

                        }

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                //Getting Image Name
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(EditProductsActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null&&resultCode==102)
        {

            //Toast.makeText(getActivity(), ""+data.getStringExtra("data"), Toast.LENGTH_SHORT).show();


            edt_note1.setText(data.getStringExtra("data"));


            myFirebase.updateMessage(orderModel.getId(),data.getStringExtra("data"));

        }

        if (data!=null&&resultCode==104)
        {

            //Toast.makeText(getActivity(), ""+data.getStringExtra("data"), Toast.LENGTH_SHORT).show();


            edt_table_name.setText(data.getStringExtra("data"));



            myFirebase.updateTableNo(orderModel.getId(),data.getStringExtra("data"));

        }
    }

}
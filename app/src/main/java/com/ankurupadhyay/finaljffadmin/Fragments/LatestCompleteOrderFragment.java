package com.ankurupadhyay.finaljffadmin.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ankurupadhyay.finaljffadmin.Adapters.PendingCartAdapter;
import com.ankurupadhyay.finaljffadmin.AddOrder.pending_Takeover_Activity;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.DeliverBoy.DeliveryBoyHomeActivity;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.MainActivity;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class LatestCompleteOrderFragment extends Fragment implements PendingCartAdapter.OnUpdateQuan {


    public LatestCompleteOrderFragment() {
        // Required empty public constructor
    }


    View view;
    MyOrders myOrders;
    Button add_item;
    RecyclerView recyclerView;

    MyFirebase myFirebase;
    PendingCartAdapter adapter;
    List<CartTable> cartTableList;
    MyDatabase myDatabase;
    Gson gson = new Gson();

    OrderModel orderModel;
    TextView delivery_charge,name,mobile,total_price,final_amount;
    EditText discount;

    LinearLayout li_delivery, li_takeaway;
    ImageView img_delivery, img_takeaway;

    boolean isclicked;
    boolean ispay_online;


    int total_final;

    FirebaseFirestore db;


    CheckBox check_whatsapp;


    Activity activity;

    Button checkout;

    ProgressBar progress_checkout;
    @Override
    public void onAttach(@NonNull Activity context) {

        this.activity = context;

        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_latest_complete_order, container, false);
        view = inflater.inflate(R.layout.fragment_complete_order, container, false);


        initViews();
        handleClick();
        getData();
        refreshPage();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isclicked)
                {
                    checkout.setVisibility(View.GONE);
                    progress_checkout.setVisibility(View.VISIBLE);
                    uploadOnFirestore();
                }else
                    Toast.makeText(activity, "Please select a payment method", Toast.LENGTH_SHORT).show();

            }
        });



        return view;
    }


    private void getData() {

        Type list_type = new TypeToken<List<CartTable>>(){}.getType();

        cartTableList = gson.fromJson(myOrders.getCart_list(), list_type);


        delivery_charge.setText("₹ "+orderModel.getDelivery_charge());


        name.setText(orderModel.getUser_name());
        mobile.setText(orderModel.getUser_id());
        total_price.setText("₹ "+getTotal());

        int final_total = (getTotal()+Integer.parseInt(orderModel.getDelivery_charge()));

        total_final = final_total;
        final_amount.setText("₹ "+final_total);

    }


    private void handleClick() {




        li_takeaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isclicked = true;

                ispay_online =true;
                img_delivery.setImageResource(R.drawable.circle_radio);
                img_takeaway.setImageResource(R.drawable.ic_radio);
                refreshPage();
            }
        });

        li_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ispay_online = false;
                isclicked = true;
                img_delivery.setImageResource(R.drawable.ic_radio);
                img_takeaway.setImageResource(R.drawable.circle_radio);

                refreshPage();
            }
        });



        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), pending_Takeover_Activity.class);
                intent.putExtra("order_id",myOrders.getId());
                startActivity(intent);

            }
        });

        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                getTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initViews() {

        db = FirebaseFirestore.getInstance();

        progress_checkout = view.findViewById(R.id.progress_checkout);
        name = view.findViewById(R.id.name);
        mobile = view.findViewById(R.id.mobile);
        discount = view.findViewById(R.id.total_discount);
        total_price = view.findViewById(R.id.total_price);
        final_amount = view.findViewById(R.id.final_amount);

        checkout = view.findViewById(R.id.checkout);


        li_takeaway = view.findViewById(R.id.li_takeaway);
        li_delivery = view.findViewById(R.id.li_delivery);
        img_delivery = view.findViewById(R.id.img_delivery);
        img_takeaway = view.findViewById(R.id.img_takeaway);

        check_whatsapp = view.findViewById(R.id.check_whatsapp);


        delivery_charge = view.findViewById(R.id.delivery_charge);
        myFirebase = new MyFirebase(getActivity());
        Paper.init(getActivity());
        myOrders = Paper.book().read("order_model");
        orderModel = Paper.book().read("complete_order_model");
        add_item = view.findViewById(R.id.add_item);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myDatabase = Room.databaseBuilder(getActivity(), MyDatabase.class,"MyDb").allowMainThreadQueries().build();


    }



    private void refreshPage() {
        adapter = new PendingCartAdapter(getActivity(),cartTableList,this);
        recyclerView.setAdapter(adapter);



        myFirebase.updatePrice(myOrders.getId(),""+getTotal(),gson.toJson(cartTableList));

    }



    @Override
    public void onGetUpdate(List<CartTable> updatelist) {
        myDatabase.dao().updateCart(myOrders.getId(),gson.toJson(updatelist));
        refreshPage();

        cartTableList = updatelist;
        //Toast.makeText(getActivity(), ""+getTotal(), Toast.LENGTH_SHORT).show();
        Log.d("mycartdata",""+getTotal());

        myOrders.setCart_list(gson.toJson(cartTableList));
        Paper.book().write("order_model",myOrders);
        getData();

    }

    public Integer getTotal()
    {

        int total =0;
        for (CartTable data:cartTableList)
        {
            total = total+((data.getExtra()+data.getPrice())*data.getQuantity());

        }

        try {
            total = total-Integer.parseInt(discount.getText().toString());
            total = total+Integer.parseInt(orderModel.getDelivery_charge());
            final_amount.setText("₹ "+total);


        }catch (Exception e)
        {

        }


        return total;
    }




    private void uploadOnFirestore() {

        HashMap<String,Object> todo = new HashMap<>();
        todo.put("order_json",myOrders.getCart_list());
        todo.put("address",orderModel.getAddress());
        todo.put("payment_id","NA");
        todo.put("delivery_charge",orderModel.getDelivery_charge());
        todo.put("user_id",orderModel.getUser_id());
        todo.put("timestamp", Timestamp.now());
        todo.put("istakeaway",orderModel.isIspayment());
        todo.put("total_price",""+total_final);
        todo.put("user_name",orderModel.getUser_name());
        todo.put("status","Delivered");
        todo.put("ispayment",true);
        todo.put("discount",discount.getText().toString());
        if (!ispay_online)
            todo.put("payment_method","Online");
        else
            todo.put("payment_method","Pay On Cash");

        todo.put("token", orderModel.getToken());
        todo.put("shop_json",""+gson.toJson(myOrders));

        if (orderModel.getAddress().equals("Jff Table "))
        todo.put("order_type","From Shop");
        else
            todo.put("order_type","Online Order");


        db.collection("Purchased_Order").document(myOrders.getId()).update(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {







                String full_message = "Hi, "+orderModel.getUser_name()+"%0aWelcome to JAIN FAST FOOD%0a Your order is :-\n";
                String order="";
                ArrayList<String> data = new ArrayList<>();
                //data.add(last_message);




                data.add(full_message);

                for (CartTable table:cartTableList)
                {
                    SubCategoryModel model = gson.fromJson(table.getJson(), SubCategoryModel.class);


                    Log.d("mysize",""+table.getQuantity());

                    int total_amount = Integer.parseInt(model.getPrice())+table.getExtra();

                    order = model.getName()+" X "+table.getQuantity() +" = "+model.getPrice()+"\n";
                    data.add(model.getName()+" ( "+total_amount+" X "+table.getQuantity() +") = "+(Integer.parseInt(model.getPrice())*table.getQuantity())+"\n");
                }
                Log.d("mylogvalue",""+order);
                data.add("Total Amount = Rs. "+getTotal());
                data.add("\nHope you enjoy the food !\nHave a good day !%0a");

                // data.add("Download  JFF Android App From %0a"+"https://play.google.com/store/apps/details?id=com.ankurupadhyay.finalljffuser");
//                String last_message = full_message+order+"Total : "+getTotal()+".\nHope you enjoy the food !\nThanks For Visit JAIN FAST FOOD";
                String last_message="";
                for (String message:data)
                {
                    last_message += message+"%0a";
                }





                Intent intent;
                if (!Paper.book().read("type").equals("delivery"))
                {
                    intent=new Intent(getActivity(), MainActivity.class);
                }
                else
                {
                    intent=new Intent(getActivity(), DeliveryBoyHomeActivity.class);
                }



                //PendingIntent pi=PendingIntent.getActivity(getActivity(), 0, intent,0);
                //Get the SmsManager instance and call the sendTextMessage method to send message






                sendSmsToUser(mobile.getText().toString(),last_message);
                myDatabase.dao().deleteSignlerder(myOrders.getId());
                Toast.makeText(getActivity(), "Order Completed Successfully", Toast.LENGTH_SHORT).show();


                if (check_whatsapp.isChecked())
                {

                    Common.whatsAppContact(getActivity(),mobile.getText().toString(),last_message);



                }else
                {

                    if (!Paper.book().read("type").equals("delivery"))
                    {
                        Intent intent1 = new Intent(getActivity(), MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);

                    }
                    else
                    {
                        Intent intent1 = new Intent(getActivity(), DeliveryBoyHomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);

                    }
                }

















            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                checkout.setVisibility(View.VISIBLE);
                progress_checkout.setVisibility(View.GONE);
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void sendSmsToUser(String mobile,String message){
        //Showing the progress dialog
        // final ProgressDialog loading = ProgressDialog.show(Student_entry.this,"Uploading...","Please wait...",false,false);

        String url = "http://fastsms.fastsmsindia.com/api/sendhttp.php?authkey=37124AeevEybx60544db6P30&mobiles="+mobile+"&message="+message+"&sender=NEWMSG&route=6&country=0";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {




                        //Showing toast

                        try {

                            Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

        backToHome();

    }



    private void backToHome() {


        if (!Paper.book().read("type").equals("delivery"))
        {
            Intent intent1 = new Intent(activity, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);

        }
        else
        {
            Intent intent1 = new Intent(activity, DeliveryBoyHomeActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);

        }



    }
}
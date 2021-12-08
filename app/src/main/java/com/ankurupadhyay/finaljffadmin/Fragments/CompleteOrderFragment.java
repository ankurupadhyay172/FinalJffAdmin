package com.ankurupadhyay.finaljffadmin.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.ankurupadhyay.finaljffadmin.Model.CategoryModel;
import com.ankurupadhyay.finaljffadmin.Model.NewOrderModel;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.NukeSSLCerts;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.SendSmsActivity;
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
import java.util.UUID;

import io.paperdb.Paper;

public class CompleteOrderFragment extends Fragment implements PendingCartAdapter.OnUpdateQuan {


    public CompleteOrderFragment() {
        // Required empty public constructor
    }

    Activity activity;
    @Override
    public void onAttach(@NonNull Activity context) {

        this.activity = context;

        super.onAttach(context);
    }

    public static CompleteOrderFragment newInstance(String order_json) {

        Bundle args = new Bundle();
        args.putString("order_json",order_json);
        CompleteOrderFragment fragment = new CompleteOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }





    Gson gson = new Gson();

    //CartAdapter adapter;

    PendingCartAdapter adapter;
    MyDatabase myDatabase;
    List<CartTable> cartTableList;

    MyOrders myOrders;
    TextView total_price,name,mobile,grand_total,final_amount;
    RecyclerView recyclerView;
    UserModel userModel;

    Button checkout;
    LinearLayout li_delivery, li_takeaway;
    ImageView img_delivery, img_takeaway;



    LinearLayout li_ac, li_nonac;
    ImageView img_ac, img_nonac;

    ProgressBar progressBar;
    FirebaseFirestore db;
    boolean isclicked;
    boolean ispay_online;

    CheckBox check_whatsapp;


    Button add_item;

    MyFirebase myFirebase;
    Button cancel_order;
    String ac;
    boolean isAc;


    TextView total_discount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete_order, container, false);

        new NukeSSLCerts().nuke();

        Paper.init(getActivity());

        myFirebase = new MyFirebase(getActivity());
        db = FirebaseFirestore.getInstance();
        li_takeaway = view.findViewById(R.id.li_takeaway);
        li_delivery = view.findViewById(R.id.li_delivery);
        img_delivery = view.findViewById(R.id.img_delivery);
        img_takeaway = view.findViewById(R.id.img_takeaway);
        total_discount = view.findViewById(R.id.total_discount);
        final_amount = view.findViewById(R.id.final_amount);


        li_ac = view.findViewById(R.id.li_ac);
        li_nonac = view.findViewById(R.id.li_nonac);
        img_ac = view.findViewById(R.id.img_ac);
        img_nonac = view.findViewById(R.id.img_nonac);

        cancel_order = view.findViewById(R.id.cancel_order);

        add_item = view.findViewById(R.id.add_item);
        check_whatsapp = view.findViewById(R.id.check_whatsapp);
        progressBar = view.findViewById(R.id.progress_checkout);
        checkout = view.findViewById(R.id.checkout);
        name = view.findViewById(R.id.name);
        mobile = view.findViewById(R.id.mobile);
        grand_total = view.findViewById(R.id.grand_total);
        total_price = view.findViewById(R.id.total_price);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myDatabase = Room.databaseBuilder(getActivity(), MyDatabase.class,"MyDb").allowMainThreadQueries().build();


        Bundle bundle = getArguments();


        myOrders = gson.fromJson(bundle.getString("order_json"),MyOrders.class);




        total_discount.addTextChangedListener(new TextWatcher() {
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

        Log.d("mylogiddata",""+myOrders.getId());

        userModel = gson.fromJson(myOrders.getUser_json(),UserModel.class);


        Type list_type = new TypeToken<List<CartTable>>(){}.getType();

        cartTableList = gson.fromJson(myOrders.getCart_list(), list_type);



        name.setText(userModel.getName());
        mobile.setText(userModel.getMobile_no());

        //adapter = new CartAdapter(getActivity(),cartTableList,myDatabase,this);



        refreshPage();

        if (Paper.book().read("type").equals("delivery"))
        {
            check_whatsapp.setVisibility(View.GONE);
        }
        else
        {
            check_whatsapp.setVisibility(View.VISIBLE);
        }


        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               
                if (Paper.book().read("type").equals("Delivery"))
                {

                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else
                {

                    Intent intent = new Intent(getActivity(), DeliveryBoyHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }




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









        li_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac = "ac";

                isAc = true;
                img_ac.setImageResource(R.drawable.ic_radio);
                img_nonac.setImageResource(R.drawable.circle_radio);




                for (CartTable table:cartTableList)
                {


                    NewOrderModel model1 = gson.fromJson(table.getJson(),NewOrderModel.class);

                    if (model1.getId().equals("dkWmxR01dd4lH47kVpUX")||model1.getId().equals("cf257f4c-4066-4e58-b6a5-c9f37809541b"))
                    {

                    }else
                    {
                        table.setExtra(10);
                        table.setAc(true);
                    }


                }



                refreshPage();
            }
        });

        li_nonac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ac = "nonac";
                isAc = false;
                img_ac.setImageResource(R.drawable.circle_radio);
                img_nonac.setImageResource(R.drawable.ic_radio);



                for (CartTable table:cartTableList)
                {
                    table.setExtra(0);
                    table.setAc(false);
                }




                refreshPage();
            }
        });


















        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Common.whatsAppContact(getActivity(),userModel.getMobile_no(),"This is test message");



                String food;


                if (getActivity().checkCallingOrSelfPermission(Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},100);
                }
                else
                {




                    if (!isclicked||ac==null)
                    {
                        if (!isclicked)
                        {
                            Toast.makeText(getActivity(), "Please select an payment method", Toast.LENGTH_SHORT).show();
                        }
                        if (ac==null)
                        {
                            Toast.makeText(activity, "Please Select Hall Type", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else
                    completeOrder();


                }

//                String message = userModel.getName()+" \nThanks to visit us.We hope you enjoy the food.\nYour billing statement is "+total_price.getText().toString()+" For ";
//
//                String contact = "+91"+userModel.getMobile_no(); // use country code with your phone number
//                String url = "https://api.whatsapp.com/send?phone=" + contact+"&text="+message;
//                try {
//                    PackageManager pm = getActivity().getPackageManager();
//                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//
//                    i.putExtra(Intent.EXTRA_TEXT,message);
//                    startActivity(i);
//                } catch (PackageManager.NameNotFoundException e) {
//                    Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }


            }
        });





        return view;
    }

    private void completeOrder() {


        checkout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String id = UUID.randomUUID().toString();

        HashMap<String,Object> todo = new HashMap<>();
        todo.put("order_json",myOrders.getCart_list());
        todo.put("address","Jain Fast Food New Colony");
        todo.put("payment_id","NA");
        todo.put("delivery_charge","0");
        todo.put("user_id",userModel.getMobile_no());
        todo.put("timestamp", Timestamp.now());
        todo.put("istakeaway",false);
        todo.put("total_price",getTotal().toString());
        todo.put("user_name",userModel.getName());
        todo.put("status","Delivered");
        todo.put("ispayment",true);
        todo.put("discount",total_discount.getText().toString());

        if (!ispay_online)
        todo.put("payment_method","Online");
        else
            todo.put("payment_method","Pay On Cash");

        todo.put("token", userModel.getToken_no());
        todo.put("shop_json",""+gson.toJson(myOrders));
        todo.put("order_type","From Shop");


        Log.d("myorderid123",""+myOrders.getId());
        db.collection("Purchased_Order").document(myOrders.getId()).update(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                String full_message = "Hi, "+userModel.getName()+"%0aWelcome to JAIN FAST FOOD%0a Your order is :-\n";
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
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }








    public void refreshPage() {



        adapter = new PendingCartAdapter(getActivity(),cartTableList,this);
        recyclerView.setAdapter(adapter);



        total_price.setText("₹ "+getTotal());

        grand_total.setText("₹ "+getTotal());


        myFirebase.updatePrice(myOrders.getId(),""+getTotal(),gson.toJson(cartTableList));





















    }

    public Integer getTotal()
    {

        int total =0;
        for (CartTable data:cartTableList)
        {
            total = total+((data.getExtra()+data.getPrice())*data.getQuantity());

        }

        try {
            total = total-Integer.parseInt(total_discount.getText().toString());

            final_amount.setText("₹ "+total);
        }catch (Exception e)
        {

        }

        return total;
    }


    @Override
    public void onGetUpdate(List<CartTable> updatelist) {



        myDatabase.dao().updateCart(myOrders.getId(),gson.toJson(updatelist));
        refreshPage();
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
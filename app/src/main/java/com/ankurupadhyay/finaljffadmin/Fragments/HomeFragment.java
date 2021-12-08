package com.ankurupadhyay.finaljffadmin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.ankurupadhyay.finaljffadmin.Adapters.OrdersAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.SubCategoryModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.Products.EditProductsActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class HomeFragment extends Fragment implements View.OnClickListener, MyFirebase.OngetPurchased {


    public HomeFragment() {
        // Required empty public constructor
    }






    FirebaseFirestore db;

    RecyclerView recyclerView;



    List<OrderModel> mylist;

    LinearLayout li_empty;


    ProgressBar progressBar;


    TextView pending,ongoing,delivered,failed,cooking;


    String filterValue="Pending";

    MyFirebase myFirebase;


    TextView status;

    Gson gson = new Gson();

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        myFirebase = new MyFirebase(getActivity());

        status = view.findViewById(R.id.status);
        li_empty = view.findViewById(R.id.li_empty);
        delivered = view.findViewById(R.id.txt_deliverd);
        failed = view.findViewById(R.id.txt_failed);
        cooking = view.findViewById(R.id.txt_cooking);


        progressBar = view.findViewById(R.id.progressbar);

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        pending = view.findViewById(R.id.txt_pending);
        ongoing = view.findViewById(R.id.txt_ongoing);


        pending.setOnClickListener(this);
        ongoing.setOnClickListener(this);
        delivered.setOnClickListener(this);
        failed.setOnClickListener(this);
        cooking.setOnClickListener(this);


        myFirebase.getPurchasedOrders("Pending",this);






        FirebaseMessaging.getInstance().subscribeToTopic("order").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        });






        return view;
    }








    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        Log.d("apunkilist",""+mylist.get(item.getGroupId()).getUser_id());

        try {
            ArrayList<String> data = new ArrayList<>();
            String full_message = "Hi, "+mylist.get(item.getGroupId()).getUser_name()+"%0aWelcome to JAIN FAST FOOD%0a Your order is :-\n";
            data.add(full_message);
            Type listType = new TypeToken<List<com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable>>(){}.getType();
            List<CartTable> cartTableList = gson.fromJson(mylist.get(item.getGroupId()).getOrder_json(),listType);

            for (com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable table:cartTableList)
            {
                SubCategoryModel model = gson.fromJson(table.getJson(), SubCategoryModel.class);


                Log.d("mysize",""+table.getQuantity());

                int total_amount = Integer.parseInt(model.getPrice())+table.getExtra();

                data.add(model.getName()+" ( "+total_amount+" X "+table.getQuantity() +") = "+(Integer.parseInt(model.getPrice())*table.getQuantity())+"\n");

            }
            data.add("Total Amount = Rs. "+mylist.get(item.getGroupId()).getTotal_price());
            data.add("\nHope you enjoy the food !\nHave a good day !%0a");

            String last_message="";
            for (String message:data)
            {
                last_message += message+"%0a";
            }


            Log.d("mytempmsg",last_message);

            if (item.getTitle().equals("Delivered"))
            {
                sendSmsToUser(mylist.get(item.getGroupId()).getUser_id(),last_message);

            }

        }catch (Exception e)
        {

        }

        if (mylist!=null)
        {

            String date = Common.getDateFromFireStore(mylist.get(item.getGroupId()).getTimestamp());
        db.collection("Purchased_Order").document(mylist.get(item.getGroupId()).getId()).update("status",item.getTitle(),"order_date",date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {

                  //  Common.sendnotificationmethod("Order Update ","Your order is "+item.getTitle(),compositeDisposable,mylist.get(item.getGroupId()).getToken());
                    Toast.makeText(getActivity(), "successfully status updated", Toast.LENGTH_SHORT).show();
                    updatedata();
                }

            }
        });

        }
        else
        {
            Toast.makeText(getActivity(), "Nothing in list", Toast.LENGTH_SHORT).show();
        }



        return true;
    }

    private void updatedata() {




    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        pending.setBackground(getResources().getDrawable(R.drawable.rectangle_gray_search));
        ongoing.setBackground(getResources().getDrawable(R.drawable.rectangle_gray_search));

        pending.setTextColor(getResources().getColor(R.color.black));
        ongoing.setTextColor(getResources().getColor(R.color.black));



        delivered.setBackground(getResources().getDrawable(R.drawable.rectangle_gray_search));
        failed.setBackground(getResources().getDrawable(R.drawable.rectangle_gray_search));

        delivered.setTextColor(getResources().getColor(R.color.black));
        failed.setTextColor(getResources().getColor(R.color.black));



        cooking.setBackground(getResources().getDrawable(R.drawable.rectangle_gray_search));

        cooking.setTextColor(getResources().getColor(R.color.black));
        switch (id)
        {
            case R.id.txt_pending:
                pending.setBackground(getResources().getDrawable(R.drawable.rectangle_green_search));
                pending.setTextColor(getResources().getColor(R.color.white));

                filterValue = "Pending";
                break;



            case R.id.txt_cooking:
                cooking.setBackground(getResources().getDrawable(R.drawable.rectangle_green_search));
                cooking.setTextColor(getResources().getColor(R.color.white));
                filterValue = "Cooking";
                break;

            case R.id.txt_ongoing:
                ongoing.setBackground(getResources().getDrawable(R.drawable.rectangle_green_search));
                ongoing.setTextColor(getResources().getColor(R.color.white));
                filterValue = "Out for delivery";
                break;


            case R.id.txt_deliverd:
                delivered.setBackground(getResources().getDrawable(R.drawable.rectangle_green_search));
                delivered.setTextColor(getResources().getColor(R.color.white));
                filterValue ="Delivered";
                break;



            case R.id.txt_failed:
                failed.setBackground(getResources().getDrawable(R.drawable.rectangle_green_search));
                failed.setTextColor(getResources().getColor(R.color.white));
                filterValue ="Failed";
                break;

        }

        progressBar.setVisibility(View.VISIBLE);
        myFirebase.getPurchasedOrders(filterValue,this);

    }




    @Override
    public void onGetPurchased(List<OrderModel> list) {

        progressBar.setVisibility(View.GONE);

        mylist = list;



        if (list.size()==0)
        {
            li_empty.setVisibility(View.VISIBLE);
            status.setText("No "+filterValue+" orders");
        }
        else
        {
            li_empty.setVisibility(View.GONE);
        }
        OrdersAdapter adapter = new OrdersAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);



        WriteBatch batch = db.batch();
        for (OrderModel orderModel:list)
        {

            if (orderModel.getOrder_date()==null)
            {
                DocumentReference document = db.collection(Common.Purchased_Db).document(orderModel.getId());
                batch.update(document,"order_date",Common.getDateFromFireStore(orderModel.getTimestamp()));
            }


        }
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onResume() {

        super.onResume();
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


    }

}
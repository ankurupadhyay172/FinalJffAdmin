package com.ankurupadhyay.finaljffadmin.AddOrder;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.CartAdapter;
import com.ankurupadhyay.finaljffadmin.Adapters.PendingCartAdapter;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.PendingCart;

import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.AddUserMobileActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.CompleteOrderActivity;
import com.ankurupadhyay.finaljffadmin.Products.EditProductsActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class PendingSaveOrderFragment extends Fragment implements TempPendingCartAdapter.OnUpdateQuan {


    public PendingSaveOrderFragment() {
        // Required empty public constructor
    }




    RecyclerView recyclerView;

    TempPendingCartAdapter cartAdapter;
    MyDatabase myDatabase;

    Button checkout;

    Gson gson = new Gson();


    MyFirebase myFirebase;

    MyOrders myOrders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_save_order, container, false);

        Paper.init(getActivity());
        myDatabase = Room.databaseBuilder(getActivity(), MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        myOrders = Paper.book().read("order_model");
        myFirebase = new MyFirebase(getActivity());
        checkout = view.findViewById(R.id.checkout);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        Log.d("myunique","Order id "+pending_Takeover_Activity.order_id);





        cartAdapter = new TempPendingCartAdapter(getActivity(),myDatabase.dao().pendingcartList(),myDatabase,this);
        recyclerView.setAdapter(cartAdapter);


        MyOrders myOrders = myDatabase.dao().getSingleOrder(pending_Takeover_Activity.order_id);

        Type listType = new TypeToken<List<CartTable>>(){}.getType();

        List<CartTable> cartTableList = gson.fromJson(myOrders.getCart_list(),listType);

        List<CartTable> temp_cart_list = new ArrayList<>();


        for (PendingCart model:myDatabase.dao().pendingcartList())
        {
            CartTable cartTable = new CartTable(model.getId(),model.getJson(),model.getQuantity(),model.getPrice(),model.getSize());
            temp_cart_list.add(cartTable);

        }
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cartTableList.addAll(temp_cart_list);




               myFirebase.updateCartList(pending_Takeover_Activity.order_id, gson.toJson(cartTableList), new MyFirebase.OnUpdate() {
                   @Override
                   public void onUpdate(boolean isSuccessfull) {

                       if (isSuccessfull)
                       {
                           Toast.makeText(getActivity(), "Successfully order updated", Toast.LENGTH_SHORT).show();


                           myDatabase.dao().updateCart(pending_Takeover_Activity.order_id,gson.toJson(cartTableList));
                           myDatabase.dao().clearPendingCart();

                           MyOrders orders = myDatabase.dao().getSingleOrder(pending_Takeover_Activity.order_id);

                           Paper.book().write("order_model",orders);
                           //Intent intent = new Intent(getActivity(), CompleteOrderActivity.class);
                           Intent intent = new Intent(getActivity(), EditProductsActivity.class);
                           intent.putExtra("order_json",gson.toJson(orders));
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                           Toast.makeText(getActivity(), "Item Update Successfully", Toast.LENGTH_SHORT).show();


                       }
                       else
                       {
                           Toast.makeText(getActivity(), "Somthing went wrong please reopen the app", Toast.LENGTH_SHORT).show();
                       }
                   }
               });














            }
        });





        return view;
    }


    @Override
    public void onGetUpdate(List<PendingCart> updatelist) {



        cartAdapter = new TempPendingCartAdapter(getActivity(),updatelist,myDatabase,this);
        recyclerView.setAdapter(cartAdapter);


    }
}
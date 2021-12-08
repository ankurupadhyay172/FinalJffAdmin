package com.ankurupadhyay.finaljffadmin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.DeliverBoy.DeliveryBoyHomeActivity;
import com.ankurupadhyay.finaljffadmin.ManageSliderImageActivity;
import com.ankurupadhyay.finaljffadmin.Orders.Delivery_Charge_Activity;
import com.ankurupadhyay.finaljffadmin.Orders.TotalOrdersActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.SplashScreenActivity;
import com.ankurupadhyay.finaljffadmin.Staff.ManageStaffActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    Button logout;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    TextView name,mobile,success_orders;

    LinearLayout li_delivery_charge,li_staff,li_sliding_image;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Common.SP_LOGIN, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        firebaseAuth = FirebaseAuth.getInstance();
        li_sliding_image = view.findViewById(R.id.li_sliding_image);
        li_delivery_charge = view.findViewById(R.id.li_delivery_charge);
        name = view.findViewById(R.id.name);
        mobile = view.findViewById(R.id.mobile);
        success_orders = view.findViewById(R.id.success_orders);

        li_staff = view.findViewById(R.id.li_staff);

        name.setText(sharedPreferences.getString(Common.SP_NAME,"NA"));
        mobile.setText(sharedPreferences.getString(Common.SP_STAFF_ID,"NA"));


        li_sliding_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ManageSliderImageActivity.class));
            }
        });

        success_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TotalOrdersActivity.class));
            }
        });


        logout = view.findViewById(R.id.logout);



        li_delivery_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Delivery_Charge_Activity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseAuth.signOut();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("order").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();

                        Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });


        li_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageStaffActivity.class));
            }
        });



        return view;
    }
}
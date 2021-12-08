package com.ankurupadhyay.finaljffadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.CartAdapter;
import com.ankurupadhyay.finaljffadmin.AddInstructionsActivity;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.AddUserMobileActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.NewHomeActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.SaveOrdersActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.TakeOrderActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SaveOrdersFragment extends Fragment implements CartAdapter.OnUpdateData{


    public SaveOrdersFragment() {
        // Required empty public constructor
    }



    RecyclerView recyclerView;

    CartAdapter cartAdapter;
    MyDatabase myDatabase;

    Button checkout;



    LinearLayout li_delivery, li_takeaway;
    ImageView img_delivery, img_takeaway;

    String selected_hall;
    TextView edit_note,edt_table_name;

    public static String USER_INSTRUCTION;

    public static String TABLE_NO;


    ImageView img_note,img_table_no;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_orders, container, false);


        myDatabase = Room.databaseBuilder(getActivity(), MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        img_note = view.findViewById(R.id.img_note);
        edit_note = view.findViewById(R.id.edt_note1);
        img_note = view.findViewById(R.id.img_note);
        li_takeaway = view.findViewById(R.id.li_takeaway);
        li_delivery = view.findViewById(R.id.li_delivery);
        img_delivery = view.findViewById(R.id.img_delivery);
        img_takeaway = view.findViewById(R.id.img_takeaway);
        edt_table_name = view.findViewById(R.id.edt_table_name);
        img_table_no = view.findViewById(R.id.img_table_no);

        checkout = view.findViewById(R.id.checkout);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartAdapter = new CartAdapter(getActivity(),myDatabase.dao().cartList(),myDatabase,this);
        recyclerView.setAdapter(cartAdapter);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    TABLE_NO = edt_table_name.getText().toString();
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy", Locale.getDefault());
                    SimpleDateFormat df2 = new SimpleDateFormat("dd_MMM_yyyy mm:ss", Locale.getDefault());


                    Gson gson = new Gson();


                    Intent intent = new Intent(getActivity(), AddUserMobileActivity.class);


                    startActivity(intent);

            }
        });


        
        img_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddInstructionsActivity.class);

                startActivityForResult(intent,102);
            }
        });
        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddInstructionsActivity.class);

                startActivityForResult(intent,102);
            }
        });


        img_table_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddInstructionsActivity.class);
                intent.putExtra("type","table_no");
                startActivityForResult(intent,104);

            }
        });

        edt_table_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddInstructionsActivity.class);
                intent.putExtra("type","table_no");

                startActivityForResult(intent,104);


            }
        });



        li_takeaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

  //              Toast.makeText(getActivity(), "non ac hall", Toast.LENGTH_SHORT).show();

                selected_hall = "non_ac";
                for (CartTable table:myDatabase.dao().cartList())
                {
                    myDatabase.dao().updateExtra(0,false,table.getId());
                }

                updateAdapter();
                img_delivery.setImageResource(R.drawable.circle_radio);
                img_takeaway.setImageResource(R.drawable.ic_radio);
            }
        });

        li_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "ac hall", Toast.LENGTH_SHORT).show();

                selected_hall ="ac";
                img_delivery.setImageResource(R.drawable.ic_radio);
                img_takeaway.setImageResource(R.drawable.circle_radio);


                for (CartTable table:myDatabase.dao().cartList())
                {
                    myDatabase.dao().updateExtra(10,true,table.getId());
                }

                updateAdapter();

//
            }
        });





        return view;
    }

    private void updateAdapter() {

        cartAdapter = new CartAdapter(getActivity(),myDatabase.dao().cartList(),myDatabase,this);
                recyclerView.setAdapter(cartAdapter);

    }

    @Override
    public void getOnUpdate(int position) {
        Toast.makeText(getActivity(), "Item Update Successfully", Toast.LENGTH_SHORT).show();
        cartAdapter = new CartAdapter(getActivity(),myDatabase.dao().cartList(),myDatabase,this);
        recyclerView.setAdapter(cartAdapter);

        if (myDatabase.dao().cartList().size()<=0)
            checkout.setVisibility(View.GONE);
        else
            checkout.setVisibility(View.VISIBLE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null&&resultCode==102)
        {

            //Toast.makeText(getActivity(), ""+data.getStringExtra("data"), Toast.LENGTH_SHORT).show();

            USER_INSTRUCTION = data.getStringExtra("data");

            edit_note.setText(data.getStringExtra("data"));

        }




        if (data!=null&&resultCode==104)
        {

            //Toast.makeText(getActivity(), ""+data.getStringExtra("data"), Toast.LENGTH_SHORT).show();

            TABLE_NO = data.getStringExtra("data");

            edt_table_name.setText(data.getStringExtra("data"));

        }


    }
}
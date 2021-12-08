package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.DeliverBoy.DeliveryBoyHomeActivity;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Fragments.SaveOrdersFragment;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.CartTable;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyOrders;
import com.ankurupadhyay.finaljffadmin.MainActivity;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;

public class AddUserActivity extends AppCompatActivity {

    EditText name,email,mobile_no;

    Gson gson = new Gson();
    MyDatabase myDatabase;

    Button btn_next;
    FirebaseFirestore db;
    ProgressBar btn_loading;

    CompositeDisposable compositeDisposable;
    MyFirebase myFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Paper.init(this);
        db = FirebaseFirestore.getInstance();



        compositeDisposable = new CompositeDisposable();
        myFirebase = new MyFirebase(this);
        myDatabase = Room.databaseBuilder(this, MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile_no = findViewById(R.id.mobile_no);
        btn_next = findViewById(R.id.btnNext);
        btn_loading = findViewById(R.id.btn_loading);

        Intent intent = getIntent();

        if (intent.hasExtra("mobile_no"))
        {
            mobile_no.setText(intent.getStringExtra("mobile_no"));
        }



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Common.isEmpty(name)||Common.isEmpty(mobile_no))
                {
                    if (Common.isEmpty(name))
                        name.setError("Please enter customer name");
                    if (Common.isEmpty(mobile_no))
                        mobile_no.setError("Please enter 10 digits mobile no");
                }
                else
                {

                    uploadUser();
                }


            }
        });
    }

    private void uploadUser() {

        btn_next.setVisibility(View.GONE);
        btn_loading.setVisibility(View.VISIBLE);
        Map<String,Object> todo =new HashMap<>();
        todo.put("mobile_no",mobile_no.getText().toString());
        todo.put("name",name.getText().toString());
        todo.put("email",email.getText().toString());
        todo.put("token_no","na");
        todo.put("timestamp", Timestamp.now());
        todo.put("from","Cafe");
        todo.put("image","na");




        db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();


        DocumentReference user_reference =db.collection(Common.db_user).document(mobile_no.getText().toString());
        batch.set(user_reference,todo);





        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                db.collection(Common.db_user).document(mobile_no.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {

                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy", Locale.getDefault());
                            SimpleDateFormat df2 = new SimpleDateFormat("dd_MMM_yyyy mm:ss", Locale.getDefault());
                            UserModel model = documentSnapshot.toObject(UserModel.class);

                            String id = df2.format(c)+mobile_no.getText().toString();

                            Log.d("AddUserId",""+id);


                            Log.d("mydata12345",""+getTotal(myDatabase.dao().cartList()));

                            model.setId(documentSnapshot.getId());
                            MyOrders myOrders = new MyOrders(id,gson.toJson(model),gson.toJson(myDatabase.dao().cartList()), df2.format(c));


                            if (SaveOrdersFragment.USER_INSTRUCTION!=null)
                            {
                                myOrders.setMessage(SaveOrdersFragment.USER_INSTRUCTION);
                            }



                            myFirebase.updateOrder(gson.toJson(model),df2.format(c),myOrders.getId(), gson.toJson(myDatabase.dao().cartList()), "Jff Table ", "na", "0", model.getId(), "" + getTotal(myDatabase.dao().cartList()), model.getName(), false, false, "na", model.getToken_no(),myOrders.getMessage(),SaveOrdersFragment.TABLE_NO, new MyFirebase.OnUpdate() {
                                @Override
                                public void onUpdate(boolean isSuccessfull) {

                                    Common.sendnotificationmethodTopic("Order Update ","New Order from Jff Table ",compositeDisposable,"order");
                                    Common.sendnotificationmethod("Order Update ","Successfully Placed Order ",compositeDisposable,model.getToken_no());
                                    myDatabase.dao().InsertMyOrder(myOrders);
                                    myDatabase.dao().clearCart();


                                    Toast.makeText(AddUserActivity.this, "Orders Save Successfully", Toast.LENGTH_SHORT).show();

                                    if (Paper.book().read("type").equals("delivery"))
                                    {

                                        Intent intent = new Intent(AddUserActivity.this, DeliveryBoyHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    }else
                                    {
                                    Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    }


                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });






//        db.collection(Common.db_user).document(mobile_no.getText().toString()).set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                db.collection(Common.db_user).document(mobile_no.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Date c = Calendar.getInstance().getTime();
//                        SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy", Locale.getDefault());
//                        SimpleDateFormat df2 = new SimpleDateFormat("dd_MMM_yyyy mm:ss", Locale.getDefault());
//                        UserModel model = documentSnapshot.toObject(UserModel.class);
//
//                        String id = df2.format(c)+mobile_no.getText().toString();
//
//                        Log.d("AddUserId",""+id);
//
//
//                        Log.d("mydata12345",""+getTotal(myDatabase.dao().cartList()));
//
//                        model.setId(documentSnapshot.getId());
//                        MyOrders myOrders = new MyOrders(id,gson.toJson(model),gson.toJson(myDatabase.dao().cartList()), df2.format(c));
//
//                        myFirebase.updateOrder(myOrders.getId(),gson.toJson(myDatabase.dao().cartList()), "Jff Table ", "na", "Jff Table ", model.getId(), ""+getTotal(myDatabase.dao().cartList()), model.getName(), false, false, "na",model.getToken_no(), new MyFirebase.OnUpdate() {
//                            @Override
//                            public void onUpdate(boolean isSuccessfull) {
//
//                                Common.sendnotificationmethodTopic("Order Update ","New Order from Jff Table ",compositeDisposable,"order");
//                                Common.sendnotificationmethod("Order Update ","Successfully Placed Order ",compositeDisposable,model.getToken_no());
//                                myDatabase.dao().InsertMyOrder(myOrders);
//                                myDatabase.dao().clearCart();
//
//
//                                Toast.makeText(AddUserActivity.this, "Orders Save Successfully", Toast.LENGTH_SHORT).show();
//
//
//                                //Log.d("mydata12345",""+myOrders.getCart_list());
////                    myFirebase.updateOrder(gson.toJson(myOrders),"Jff Table ","na","Jff Table ",model.getId(),myOrders.g);
//
//                                if (Paper.book().read("type")==null)
//                                {
//                                    Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//
//                                }
//                                else
//                                {
//                                    Intent intent = new Intent(AddUserActivity.this, DeliveryBoyHomeActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//
//                                }
//                            }
//                        });
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//
//                        btn_next.setVisibility(View.VISIBLE);
//                        btn_loading.setVisibility(View.GONE);
//                        Toast.makeText(AddUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//
//                btn_next.setVisibility(View.VISIBLE);
//                btn_loading.setVisibility(View.GONE);
//                Toast.makeText(AddUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    public Integer getTotal(List<CartTable> cartTableList)
    {

        int total =0;
        for (CartTable data:cartTableList)
        {
            total = total+((data.getExtra()+data.getPrice())*data.getQuantity());


        }
        return total;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }
}
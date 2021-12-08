package com.ankurupadhyay.finaljffadmin.MyNewDesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import io.reactivex.disposables.CompositeDisposable;

public class AddUserMobileActivity extends AppCompatActivity {


    EditText mobile_no;
    Button login;

    ProgressBar progressBar;


    FirebaseFirestore db;

    Gson gson = new Gson();
    MyDatabase myDatabase;
    TextView error;

    CompositeDisposable compositeDisposable;

    MyFirebase myFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_mobile);

        compositeDisposable = new CompositeDisposable();
        db = FirebaseFirestore.getInstance();
        myDatabase = Room.databaseBuilder(this, MyDatabase.class,"MyDb").allowMainThreadQueries().build();

        Paper.init(this);
        mobile_no = findViewById(R.id.mobile_no);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressbar);
        error = findViewById(R.id.error);

        myFirebase = new MyFirebase(this);

        mobile_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()==10)
                {

                    InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);


                    //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                if (mobile_no.getText().toString().length()!=10)
                {
                    mobile_no.setError("Please enter 10 digits mobile no");
                }
                else
                {
                    getUserDetails();
                }

            }
        });




    }


    public void getUserDetails()
    {
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


                    myFirebase.updateOrder(gson.toJson(model),df2.format(c),myOrders.getId(),gson.toJson(myDatabase.dao().cartList()), "Jff Table ", "na", "0", model.getId(), ""+getTotal(myDatabase.dao().cartList()), model.getName(), false, false, "na",model.getToken_no(),myOrders.getMessage(),SaveOrdersFragment.TABLE_NO, new MyFirebase.OnUpdate() {
                        @Override
                        public void onUpdate(boolean isSuccessfull) {

                            Common.sendnotificationmethodTopic("Order Update ","New Order from Jff Table ",compositeDisposable,"order");
                            Common.sendnotificationmethod("Order Update ","Successfully Placed Order ",compositeDisposable,model.getToken_no());



                                myOrders.setTable_no(SaveOrdersFragment.TABLE_NO);


                            myDatabase.dao().InsertMyOrder(myOrders);
                            myDatabase.dao().clearCart();




                            Toast.makeText(AddUserMobileActivity.this, "Orders Save Successfully", Toast.LENGTH_SHORT).show();


                            //Log.d("mydata12345",""+myOrders.getCart_list());
//                    myFirebase.updateOrder(gson.toJson(myOrders),"Jff Table ","na","Jff Table ",model.getId(),myOrders.g);

                            if (Paper.book().read("type")==null)
                            {
                                Intent intent = new Intent(AddUserMobileActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                            else
                            {
                                Intent intent = new Intent(AddUserMobileActivity.this, DeliveryBoyHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        }
                    });




                }
                else
                {
                    Intent intent = new Intent(AddUserMobileActivity.this,AddUserActivity.class);
                    intent.putExtra("mobile_no",mobile_no.getText().toString());

                    startActivity(intent);


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                error.setText(e.getMessage());

                Log.d("mylogerror",""+e.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
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
}
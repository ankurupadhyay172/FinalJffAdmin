package com.ankurupadhyay.finaljffadmin.Ragistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.DeliverBoy.DeliveryBoyHomeActivity;
import com.ankurupadhyay.finaljffadmin.MainActivity;
import com.ankurupadhyay.finaljffadmin.Model.StaffModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MobileVerificationActivity extends AppCompatActivity implements OtpVerification.OnVerficationComplete{



    Button login;

    EditText mobileno,otp;
    ProgressBar progressBar,progressBar2;

    LinearLayout li_mobile,li_otp;
    TextView error1,error2;
    Button submit_otp;

    FirebaseFirestore db;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);





        sharedPreferences = getSharedPreferences(Common.SP_LOGIN,MODE_PRIVATE);
        editor = sharedPreferences.edit();



        login = findViewById(R.id.login);
        mobileno = findViewById(R.id.mobile_no);
        progressBar = findViewById(R.id.progressbar);
        li_mobile = findViewById(R.id.li_mobile);
        li_otp = findViewById(R.id.li_otp);
        otp = findViewById(R.id.otp);
        submit_otp = findViewById(R.id.submit_otp);
        error1 = findViewById(R.id.error);
        error2 = findViewById(R.id.error2);
        progressBar2 = findViewById(R.id.progressbar2);


        db = FirebaseFirestore.getInstance();
        OtpVerification otpVerification = new OtpVerification(this,this);





        mobileno.addTextChangedListener(new TextWatcher() {
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




        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()==6)
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






        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar2.setVisibility(View.VISIBLE);
                otpVerification.verifyCode(otp.getText().toString(), new OtpVerification.OnVerficationComplete() {
                    @Override
                    public void getVerficationStatus(String status) {
                        progressBar2.setVisibility(View.GONE);
                        error2.setVisibility(View.VISIBLE);

                        if (status.equals("Successfull"))
                        {

                            db.collection(Common.db_Staff).document(mobileno.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful())
                                    {

                                        if (task.getResult().exists())
                                        {

                                            error2.setText(status);

                                            DocumentSnapshot document = task.getResult();

                                            StaffModel model = document.toObject(StaffModel.class);
                                            model.setId(document.getId());

                                            editor.putString(Common.SP_NAME,model.getName());
                                            editor.putString(Common.SP_TYPE,model.getType());
                                            editor.putString(Common.SP_STAFF_ID,model.getId());
                                            editor.putBoolean(Common.SP_ISLOGIN,true);
                                            editor.commit();

                                            if (model.getType().equals("Admin"))
                                            {
                                                Intent intent = new Intent(MobileVerificationActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                Intent intent = new Intent(MobileVerificationActivity.this, DeliveryBoyHomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                        else
                                        {
                                            error2.setText("you are not ragistred for this app");

                                            Toast.makeText(MobileVerificationActivity.this, "you are not ragistred", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }
                            });




                        }




                    }
                });
            }
        });





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isEmpty(mobileno)||mobileno.getText().toString().trim().length() != 10)
                {
                    mobileno.setError("Enter valid mobile no");
                }else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    otpVerification.sendOtp(MobileVerificationActivity.this,mobileno.getText().toString(),progressBar,li_mobile,li_otp,otp,error1);


//                    Intent intent =new Intent(RagistrationHomeActivity.this,OtpVerificationActivity.class);
//                    intent.putExtra("mobile",mobileno.getText().toString());
//                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void getVerficationStatus(String status) {
        if (status.equals("Successfull"))
        {

            db.collection(Common.db_Staff).document(mobileno.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {

                        if (task.getResult().exists())
                        {



                            DocumentSnapshot document = task.getResult();

                            StaffModel model = document.toObject(StaffModel.class);
                            model.setId(document.getId());


                            editor.putString(Common.SP_NAME,model.getName());
                            editor.putString(Common.SP_TYPE,model.getType());
                            editor.putString(Common.SP_STAFF_ID,model.getId());
                            editor.putBoolean(Common.SP_ISLOGIN,true);
                            editor.commit();

                            if (model.getType().equals("Admin"))
                            {
                                Intent intent = new Intent(MobileVerificationActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent = new Intent(MobileVerificationActivity.this, DeliveryBoyHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }

                        else
                        {
                            error2.setText("you are not ragistred for this app");

                            Toast.makeText(MobileVerificationActivity.this, "you are not ragistred", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            });



        }
    }
}
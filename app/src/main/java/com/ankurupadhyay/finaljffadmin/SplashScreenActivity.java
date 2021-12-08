package com.ankurupadhyay.finaljffadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.androidcafe.jffstaff.StaffHomeActivity;
import com.ankurupadhyay.finaljffadmin.DeliverBoy.DeliveryBoyHomeActivity;
import com.ankurupadhyay.finaljffadmin.FinalStaffData.FinalStaffHomeActivity;
import com.ankurupadhyay.finaljffadmin.MyNewDesign.NewHomeActivity;
import com.ankurupadhyay.finaljffadmin.Ragistration.MobileVerificationActivity;
import com.ankurupadhyay.finaljffadmin.Test.BarChartActivity;
import com.ankurupadhyay.finaljffadmin.Test.SalesChartActivity;
import com.ankurupadhyay.finaljffadmin.Test.ShowGraphActivity;

public class SplashScreenActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences(Common.SP_LOGIN,MODE_PRIVATE);



        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);

                    if (sharedPreferences.getBoolean(Common.SP_ISLOGIN,false))
                    {

                        if (sharedPreferences.getString(Common.SP_TYPE,"na").equals("Admin"))
                        {
                            //Intent intent = new Intent(SplashScreenActivity.this, DeliveryBoyHomeActivity.class);
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            //Intent intent = new Intent(SplashScreenActivity.this, SalesChartActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {


                            Intent intent = new Intent(SplashScreenActivity.this, DeliveryBoyHomeActivity.class);
                            //Intent intent = new Intent(SplashScreenActivity.this, StaffHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }

                    }
                    else
                    {
                        Intent intent = new Intent(SplashScreenActivity.this, MobileVerificationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }
    }

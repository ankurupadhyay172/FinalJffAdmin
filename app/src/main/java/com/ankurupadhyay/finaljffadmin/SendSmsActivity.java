package com.ankurupadhyay.finaljffadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;

public class SendSmsActivity extends AppCompatActivity {


    Button sendsms;
    EditText mobile_no,message;

    TextView txt_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        txt_response = findViewById(R.id.txt_response);
        sendsms = findViewById(R.id.sendsms);
        mobile_no = findViewById(R.id.mobile_no);
        message = findViewById(R.id.message);
        new NukeSSLCerts().nuke();

        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://fastsms.fastsmsindia.com/api/sendhttp.php?authkey=37124AeevEybx60544db6P30&mobiles=8769746066&message="+message+"&sender=ABCDEF&route=6&country=0";

                getDatafromurl();

            }
        });

    }

    public InputStream download(String url) {
        URL myFileURL = null;
        InputStream is = null;
        try {
            myFileURL = new URL(url);
            is = myFileURL.openStream();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Log.d("mylogerror156",""+e.getMessage());
        }
        return is;
    }

    public void mysendMessage() throws IOException {



        String url = "http://fastsms.fastsmsindia.com/api/sendhttp.php?authkey=37124AeevEybx60544db6P30&mobiles=8769746066&message=Welcome to jain fast food &sender=ABCDEF&route=6&country=0";


        new URL(url).openStream();

    }


    private void getDatafromurl(){
        //Showing the progress dialog
        // final ProgressDialog loading = ProgressDialog.show(Student_entry.this,"Uploading...","Please wait...",false,false);

        String url = "http://fastsms.fastsmsindia.com/api/sendhttp.php?authkey=37124AeevEybx60544db6P30&mobiles=8769746066&message=Welcome to jain fast food &sender=ABCDEF&route=6&country=0";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        Log.d("mylogvalue",""+s);



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {




                        //Showing toast

                        try {

                            Toast.makeText(SendSmsActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

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

                params.put("action","read");
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(SendSmsActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
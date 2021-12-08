package com.ankurupadhyay.finaljffadmin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ankurupadhyay.finaljffadmin.Notifications.FCMResponse;
import com.ankurupadhyay.finaljffadmin.Notifications.FCMSendData;
import com.ankurupadhyay.finaljffadmin.Notifications.IFCMApi;
import com.ankurupadhyay.finaljffadmin.Notifications.RetrofitClient;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Common {



    public static String SP_SELECTED_OPTION = "na";
    public static String SP_LOGIN = "login";
    public static int Choose_Gallery = 201;

    public static int CAMERA_PERMISSION_REQUEST = 102;

    public static String db_user ="Users";
    public static String db_Chrages = "Charges";
    public static String db_Staff ="Staff";
    public static String SP_ISLOGIN = "islogin";


    public static String SP_TYPE="type";
    public static String SP_NAME = "name";
    public static String SP_STAFF_ID = "id";

    public static String db_home="Home";

    public static String db_menu ="Menus";
    public static String db_menu_items = "Items";

    public static String Purchased_Db = "Purchased_Order";

    public static String Menues_Db = "Menus";
    public static String temp_product_id;

    public static boolean Is_home;

    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnected();

    }

    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    public static CharSequence gethours(long timestamp)
    {
        long time = timestamp*1000L;
        long now = System.currentTimeMillis();

        CharSequence relativetime = DateUtils.getRelativeTimeSpanString(time,now, DateUtils.SECOND_IN_MILLIS,DateUtils.FORMAT_ABBREV_RELATIVE);


        return relativetime;
    }



    public static void sendnotificationmethod(String title, String body, CompositeDisposable compositeDisposable, String category) {






        IFCMApi ifcmApi = RetrofitClient.getInstance().create(IFCMApi.class);
        Map<String, String> notiData = new HashMap<>();
        notiData.put("title", title);
        notiData.put("body", body);

        FCMSendData sendData = new FCMSendData();
//        sendData.setTo("/topics/"+school_code+category);

        sendData.setTo("/"+category);
        sendData.setPriority("high");

        sendData.setData(notiData);

        Log.d("mytest","send category");

        compositeDisposable.add(ifcmApi.sendNotification(sendData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FCMResponse>() {
                    @Override
                    public void accept(FCMResponse fcmResponse) throws Exception {

                        if (fcmResponse.getSuccess() == 1) {
                        } else {
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));


    }








    public static void sendnotificationmethodTopic(String title, String body, CompositeDisposable compositeDisposable, String category) {






        IFCMApi ifcmApi = RetrofitClient.getInstance().create(IFCMApi.class);
        Map<String, String> notiData = new HashMap<>();
        notiData.put("title", title);
        notiData.put("body", body);



        Map<String,String> mynotify = new HashMap<>();
        mynotify.put("title",title);
        mynotify.put("body",body);



        FCMSendData sendData = new FCMSendData();
//        sendData.setTo("/topics/"+school_code+category);

        sendData.setTo("/topics/"+category);
        sendData.setPriority("high");


        sendData.setData(notiData);

        sendData.setNotification(mynotify);
        Log.d("mytest","send category");

        compositeDisposable.add(ifcmApi.sendNotification(sendData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FCMResponse>() {
                    @Override
                    public void accept(FCMResponse fcmResponse) throws Exception {

                        if (fcmResponse.getSuccess() == 1) {
                        } else {
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));


    }





    public static void sendSms(Context context,String mobileno,String message)
    {
        String contact = "+91"+mobileno; // use country code with your phone number
        String url = "http://fastsms.fastsmsindia.com/api/sendhttp.php?authkey=37124AeevEybx60544db6P30&mobiles=8769746066&message="+message+"&sender=ABCDEF&route=6&country=0";
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.ankurupadhyay.finaljffadmin", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            i.putExtra(Intent.EXTRA_TEXT,"message");
            context.startActivity(i);



        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void whatsAppContact(Context context,String mobileno,String message)
    {
        String contact = "+91"+mobileno; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact+"&text="+message;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            i.putExtra(Intent.EXTRA_TEXT,"message");
            context.startActivity(i);



        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    public static boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime());
    }


    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }




    public static void getFromExcel(Context context,String order_id,String user_id,
                                    String name,String order_address,String order_date,String total_amount,String delivery_charge,
                                    String discount,String grand_total,String order_status,String payment_method,String order_method
                                    ,String order_from,OnComplete onComplete)
    {

        String excelurl = "https://script.google.com/macros/s/AKfycbyETJKbi-6Y6K_GsDJ7_oF39K3-rJKew_qyWUND0sB-JcI5FTWCPY-6XudZhEW-Sk8ggw/exec";



        StringRequest request =new StringRequest(Request.Method.POST, excelurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("myexcel",response);
                onComplete.onComplete(response);

                Gson gson = new Gson();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("records");



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("myexcel",e.getMessage());


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                NetworkResponse response = error.networkResponse;
                String errorMsg = "";
                if(response != null && response.data != null){
                    String errorString = new String(response.data);
                    Log.d("mylogerror123", errorString);
                    onComplete.onComplete(errorString);
                }
                Log.d("mylogerror123",""+error.getMessage());


            }
        })
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {


            Map<String,String> todo = new Hashtable<>();
            todo.put("action","insert");
            todo.put("order_id",order_id);
            todo.put("user_id",user_id);
            todo.put("name",name);
            todo.put("order_address",order_address);
            todo.put("order_date",order_date);
            todo.put("total_amount",total_amount);
            todo.put("delivery_charge",delivery_charge);
            todo.put("discount",discount);
            todo.put("grand_total",grand_total);
            todo.put("order_status",order_status);
            todo.put("payment_method",payment_method);

            todo.put("order_form",order_from);
            todo.put("order_method",order_method);

            todo.put("id","1");



            return todo;
        }
        };



        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);



    }


    public static  String getDateFromFireStore(Timestamp timestamp)
    {

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        String mydate = DateFormat.getDateInstance().format(timestamp.toDate());

        mydate =format.format(Date.parse(mydate));


        return mydate;


    }


    public static String getTimeFromFireStore(Timestamp timestamp)
    {
        String ordertime = DateFormat.getTimeInstance(DateFormat.SHORT).format(timestamp.toDate());

        return ordertime;

    }

    public interface OnComplete
    {
        public void onComplete(String output);
    }
}

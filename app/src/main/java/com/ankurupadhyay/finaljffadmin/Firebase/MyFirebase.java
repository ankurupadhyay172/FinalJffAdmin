package com.ankurupadhyay.finaljffadmin.Firebase;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.LocalDatabase.MyDatabase;
import com.ankurupadhyay.finaljffadmin.Model.CategoryModel;
import com.ankurupadhyay.finaljffadmin.Model.DeliveryModel;
import com.ankurupadhyay.finaljffadmin.Model.GridModel;
import com.ankurupadhyay.finaljffadmin.Model.HomeMenuModel;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.Model.StaffModel;
import com.ankurupadhyay.finaljffadmin.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MyFirebase {

    Context context;

    FirebaseFirestore db;
    public MyFirebase(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();

    }




    public void getPurchasedOrders(String status,OngetPurchased ongetPurchased)
    {

//        db.collection(Common.Purchased_Db).orderBy("timestamp", Query.Direction.DESCENDING).get().



        db.collection(Common.Purchased_Db).orderBy("timestamp", Query.Direction.DESCENDING).whereEqualTo("status",status).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null)
                {

                    Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    List<OrderModel> list = new ArrayList<>();
                    Gson gson = new Gson();

                    for (QueryDocumentSnapshot document:value)
                    {

                        Log.d("mydocvalue",""+document.getData());



                        OrderModel model = document.toObject(OrderModel.class);
                        model.setId(document.getId());
                        list.add(model);



                        if (status.equals("Pending"))
                        {
                            if (model.getUser_model()==null)
                            {
                                db.collection("Users").document(model.getUser_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        UserModel userModel = document.toObject(UserModel.class);
                                        userModel.setId(documentSnapshot.getId());

                                        Log.d("myusermodel",""+userModel.getName());

                                    }
                                });

                            }

                        }




                    }
                    ongetPurchased.onGetPurchased(list);


                }
            }
        });


    }








    public void getSuccessOrders(OngetPurchased ongetPurchased)
    {


        db.collection(Common.Purchased_Db).whereEqualTo("status","Delivered").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {

                    List<OrderModel> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document:task.getResult())
                    {

                        OrderModel model = document.toObject(OrderModel.class);
                        model.setId(document.getId());
                        list.add(model);





                    }
                    ongetPurchased.onGetPurchased(list);




                }
                else
                {
                    Log.d("apunkierror",task.getException().getMessage());
                }



            }
        });


    }





    public void getCategory(GetCategory getCategory)
    {

        db.collection(Common.Menues_Db).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                    List<CategoryModel> list = new ArrayList<>();


                    for (QueryDocumentSnapshot document:task.getResult())
                    {
                        CategoryModel model = document.toObject(CategoryModel.class);
                        model.setId(document.getId());
                        list.add(model);
                    }
                    getCategory.onGetCategory(list);


                }


            }
        });

    }




    public void getStaffList(OngetStaff ongetStaff)
    {

        db.collection(Common.db_Staff).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {

                    List<StaffModel> list = new ArrayList<>();

                    for (QueryDocumentSnapshot document:task.getResult())
                    {
                        StaffModel model = document.toObject(StaffModel.class);
                        model.setId(document.getId());
                        list.add(model);



                    }
                    ongetStaff.ongetStaff(list);


                }



            }
        });

    }




    public void getSlider(getSliderImage getSliderImage)
    {
        db.collection("SliderImages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<GridModel> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document:task.getResult())
                    {
                        GridModel model = document.toObject(GridModel.class);
                        model.setId(document.getId());
                        list.add(model);

                    }
                    getSliderImage.onGetSliderImage(list);

                }
            }
        });
    }



    public void getCategoryProducts(final OnGetCategory onGetCategory)
    {
        db.collection(Common.db_menu).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<HomeMenuModel> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document:task.getResult())
                    {

                        HomeMenuModel model = document.toObject(HomeMenuModel.class);
                        model.setId(document.getId());
                        list.add(model);
                        onGetCategory.getCategory(list);
                    }


                }
            }
        });
    }


    public void getAllData(String id, List<NewMealModel> list, OnGetSubCategory onGetSubCategory)
    {
        db.collection(Common.db_menu).document(id).collection(Common.db_menu_items).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document:task.getResult())
                    {
                        NewMealModel model = document.toObject(NewMealModel.class);
                        model.setId(document.getId());
                        list.add(model);
                    }
                    onGetSubCategory.getSubCategorylist(list);
                }


            }
        });
    }




    public void getSingleOrderData(String id,OnGetSingleOrder onGetSingleOrder)
    {


        db.collection(Common.Purchased_Db).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    OrderModel model = documentSnapshot.toObject(OrderModel.class);
                    onGetSingleOrder.getSingleOrder(model);

                    Log.d("mylogdata",""+model.getUser_name());

                }
                else
                    Toast.makeText(context, "data not available", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void updateOrder(String user_model,String date_format,String order_id,String json,String address,String paymentid,String delivery,String user_id,String total_price,String user_name,boolean istakeaway,boolean isPayment,String payment_method,String token,String message,String table_no,OnUpdate onUpdate)
    {

        String id = UUID.randomUUID().toString();

        HashMap<String,Object> todo = new HashMap<>();
        todo.put("order_json",json);
        todo.put("address",address);
        todo.put("payment_id",paymentid);
        todo.put("delivery_charge",delivery);
        todo.put("user_id",user_id);
        todo.put("timestamp", Timestamp.now());
        todo.put("istakeaway",istakeaway);
        todo.put("total_price",total_price);
        todo.put("user_name",user_name);
        todo.put("status","Pending");
        todo.put("ispayment",isPayment);
        todo.put("payment_method",payment_method);
        todo.put("token", token);
        todo.put("user_model",user_model);
        todo.put("date_format",date_format);
        todo.put("message",message);
        todo.put("table_no",table_no);


        db.collection("Purchased_Order").document(order_id).set(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    onUpdate.onUpdate(true);
                }
                else
                {
                    onUpdate.onUpdate(false);
                }


            }
        });

    }


    public void getTotalAmount(TextView total_amount, Date daysBeforeDate )
    {

        if (daysBeforeDate==null)
        {
            db.collection(Common.Purchased_Db).whereEqualTo("status","Delivered")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty())
                    {
                        int total=0;
                        for (DocumentSnapshot document:queryDocumentSnapshots)
                        {
                            OrderModel orderModel = document.toObject(OrderModel.class);
                            orderModel.setId(document.getId());

                            total = total+Integer.parseInt(orderModel.getTotal_price());
                        }

                        total_amount.setText("Total Sale ₹"+total);
                        Log.d("mylogdata159",""+total);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("mylogerror",""+e.getMessage());
                }
            });

        }
        else
        {
            db.collection(Common.Purchased_Db).whereEqualTo("status","Delivered")
                    .whereGreaterThanOrEqualTo("timestamp",daysBeforeDate).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty())
                    {
                        int total=0;
                        for (DocumentSnapshot document:queryDocumentSnapshots)
                        {
                            OrderModel orderModel = document.toObject(OrderModel.class);
                            orderModel.setId(document.getId());

                            total = total+Integer.parseInt(orderModel.getTotal_price());
                        }

                        total_amount.setText("Total Sale ₹"+total);
                        Log.d("mylogdata159",""+total);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("mylogerror",""+e.getMessage());
                }
            });
        }


    }


    public void updateDiscount(String order_id,String discount,OnUpdate onUpdate)
    {
        db.collection(Common.Purchased_Db).document(order_id).update("discount",discount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onUpdate.onUpdate(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
           onUpdate.onUpdate(false);
            }
        });
    }


    public void updateCartList(String order_id,String order_json,OnUpdate onUpdate)
    {

        db.collection(Common.Purchased_Db).document(order_id).update("order_json",order_json).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onUpdate.onUpdate(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                onUpdate.onUpdate(false);
            }
        });


    }


    public void updateTableNo(String order_id,String tableno)
    {
        db.collection(Common.Purchased_Db).document(order_id).update("table_no",tableno
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }




    public void updateMessage(String order_id,String message)
    {
        db.collection(Common.Purchased_Db).document(order_id).update("message",message
               ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void updateTotalPrice(String order_id,String total_price,String delivery_charge,boolean isTakeaway,String payment_method)
    {
        db.collection(Common.Purchased_Db).document(order_id).update("total_price",total_price
                ,"delivery_charge",delivery_charge,"istakeaway",isTakeaway
        ,"payment_method",payment_method).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void updateAddress(String order_id,String address,OnUpdate onUpdate)
    {
        db.collection(Common.Purchased_Db).document(order_id).update("address",address).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onUpdate.onUpdate(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            onUpdate.onUpdate(false);
            }
        });
    }

    public void updatePrice(String order_id,String total_price,String order_json)
    {
        db.collection(Common.Purchased_Db).document(order_id).update("total_price",total_price,"order_json",order_json).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    public void getSearchData(MyDatabase myDatabase,OnGetSubCategory onGetSubCategory)
    {
        db.collection("SearchData").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                List<NewMealModel> list = new ArrayList<>();
                for (DocumentSnapshot document:queryDocumentSnapshots)
                {
                    NewMealModel model = document.toObject(NewMealModel.class);
                    model.setId(document.getId());

                    com.ankurupadhyay.finaljffadmin.LocalDatabase.NewMealModel mealModel =
                            new com.ankurupadhyay.finaljffadmin.LocalDatabase.NewMealModel(model.getId(),model.getCategory(),model.getDesc(),
                                    model.getImage(),model.getJsize(),model.getName(),model.getPrice(),model.getOrder());
                    myDatabase.dao().InsertSearchItem(mealModel);
                    list.add(model);
                }
                Log.d("mylogdata",""+list.size());

                onGetSubCategory.getSubCategorylist(list);

            }
        });
    }


    public void getDelivery_charge(OnGetDelivery onGetDelivery)
    {
        db.collection(Common.db_Chrages).document("Jff").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    DeliveryModel model = documentSnapshot.toObject(DeliveryModel.class);
                    onGetDelivery.onGetDeliveryCharge(model);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void updateStatus(String order_id,String status,String date,OnGetCompleted onGetCompleted)
    {
        db.collection(Common.Purchased_Db).document(order_id).update("status",status,"ispayment",true,"order_date",date).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                onGetCompleted.onGetCompleted(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                onGetCompleted.onGetCompleted(false);
            }
        });

    }


    public void getAllOrders(OngetPurchased ongetPurchased)
    {
        db.collection(Common.Purchased_Db).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<OrderModel> list = new ArrayList<>();
                for (DocumentSnapshot document:queryDocumentSnapshots)
                {
                    OrderModel orderModel = document.toObject(OrderModel.class);
                    list.add(orderModel);
                }
                ongetPurchased.onGetPurchased(list);

            }
        });
    }


    public interface OnGetCompleted
    {
        public void onGetCompleted(boolean status);
    }

    public interface OnGetDelivery
    {
        public void onGetDeliveryCharge(DeliveryModel model);
    }

    public interface getSliderImage
    {
        public void onGetSliderImage(List<GridModel>list);

    }


    public interface OngetStaff
    {
        public void ongetStaff(List<StaffModel> list);
    }

    public interface GetCategory
    {
        public void onGetCategory(List<CategoryModel> list);
    }

    public interface OngetPurchased
    {
        public void onGetPurchased(List<OrderModel>list);
    }

    public interface OnGetCategory
    {
        public void getCategory(List<HomeMenuModel> categorylist);
    }

    public interface OnGetSubCategory
    {
        public void getSubCategorylist(List<NewMealModel> list);
    }


    public interface OnGetSingleOrder
    {
        public void getSingleOrder(OrderModel orderModel);
    }

    public interface OnUpdate
    {
        public void onUpdate(boolean isSuccessfull);
    }
}

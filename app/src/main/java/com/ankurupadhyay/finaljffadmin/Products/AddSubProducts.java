package com.ankurupadhyay.finaljffadmin.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.UploadFile;
import com.ankurupadhyay.finaljffadmin.Model.NewMealModel;
import com.ankurupadhyay.finaljffadmin.Model.SpinnerModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddSubProducts extends AppCompatActivity implements UploadFile.OnImageUpload {

    EditText title,desc,price,order;



    List<SpinnerModel> list ;
    FirebaseFirestore db;


    UploadFile uploadFile;


    TextView submit;
    String category = "na";

    CircleImageView imageview;
    byte[] imageByte;
    boolean isupdate;

    Gson gson = new Gson();

    ImageView close;
    String uid="na";
    EditText size_title1,size_title2,size_title3,size_desc1,size_desc2,size_desc3,size_price1,size_price2,size_price3,category1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_products);
        db = FirebaseFirestore.getInstance();


        close = findViewById(R.id.close);
        Intent intent = getIntent();
        imageview = findViewById(R.id.imageview);
        category1 = findViewById(R.id.category);
        size_title1 = findViewById(R.id.size_title1);
        size_title2 = findViewById(R.id.size_title2);
        size_title3 = findViewById(R.id.size_title3);


        size_desc1 = findViewById(R.id.size_desc1);
        size_desc2 = findViewById(R.id.size_desc2);
        size_desc3 = findViewById(R.id.size_desc3);


        size_price1 = findViewById(R.id.size_price1);
        size_price2 = findViewById(R.id.size_price2);
        size_price3 = findViewById(R.id.size_price3);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (intent.hasExtra("category"))
        {
            category = intent.getStringExtra("category");
            category1.setText(category);
        }

        title = findViewById(R.id.title);
        desc = findViewById(R.id.description);
        price = findViewById(R.id.price);

        order = findViewById(R.id.order);
        submit = findViewById(R.id.submit);

//        list.add(new SpinnerModel("LARGE","Serves 4","250"));


        uploadFile = new UploadFile(this,this);

        imageview.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {




                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1001);

                }
                else
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
                }

            }
        });




        if (intent.hasExtra("type"))
        {
            NewMealModel model = gson.fromJson(intent.getStringExtra("json"),NewMealModel.class);

            isupdate = true;
            category1.setText(model.getCategory());
            uid = model.getId();
            category = model.getCategory();
            order.setText(""+model.getOrder());
            title.setText(model.getName());
            desc.setText(model.getDesc());




            SpinnerModel[] spinnerModels = gson.fromJson(model.getJsize(),SpinnerModel[].class);

                for (SpinnerModel spinnerModel:spinnerModels)
                {


                    if (spinnerModels.length==1)
                    {
                        size_title1.setText(spinnerModels[0].getName());
                        size_desc1.setText(spinnerModels[0].getDesc());
                        size_price1.setText(spinnerModels[0].getPrice());
                    }
                    if (spinnerModels.length==2)
                    {

                        size_title1.setText(spinnerModels[0].getName());
                        size_desc1.setText(spinnerModels[0].getDesc());
                        size_price1.setText(spinnerModels[0].getPrice());

                        size_title2.setText(spinnerModels[1].getName());
                        size_desc2.setText(spinnerModels[1].getDesc());
                        size_price2.setText(spinnerModels[1].getPrice());

                    }

                    if (spinnerModels.length==3)
                    {


                        size_title1.setText(spinnerModels[0].getName());
                        size_desc1.setText(spinnerModels[0].getDesc());
                        size_price1.setText(spinnerModels[0].getPrice());

                        size_title2.setText(spinnerModels[1].getName());
                        size_desc2.setText(spinnerModels[1].getDesc());
                        size_price2.setText(spinnerModels[1].getPrice());



                        size_title3.setText(spinnerModels[2].getName());
                        size_desc3.setText(spinnerModels[2].getDesc());
                        size_price3.setText(spinnerModels[2].getPrice());
                    }




                }


            try {
                Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(imageview);
            }catch (IllegalArgumentException e)
            {

            }





        }




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                list = new ArrayList<>();




                if (!Common.isEmpty(size_title1)||!size_title1.getText().toString().equals(""))
                {
                    list.add(new SpinnerModel(size_title1.getText().toString(),size_desc1.getText().toString(),size_price1.getText().toString()));
                }


                if (!Common.isEmpty(size_title2)||!size_title2.getText().toString().equals(""))
                {
                    list.add(new SpinnerModel(size_title2.getText().toString(),size_desc2.getText().toString(),size_price2.getText().toString()));
                }


                if (!Common.isEmpty(size_title3)||!size_title3.getText().toString().equals(""))
                {
                    list.add(new SpinnerModel(size_title3.getText().toString(),size_desc3.getText().toString(),size_price3.getText().toString()));
                }


             //   list.add(new SpinnerModel("GRILL","Sandwich","90"));




                if (Common.isEmpty(size_title1)||Common.isEmpty(size_desc1)||Common.isEmpty(size_price1))
                {
                    if (Common.isEmpty(size_title1))
                        size_title1.setError("Please enter atlist one size title");

                    if (Common.isEmpty(size_desc1))
                        size_desc1.setError("Please enter atlist one size description");

                    if (Common.isEmpty(size_price1))
                        size_price1.setError("Please enter atlist one size price");

                }
                else
                {
                    if (isupdate)
                    {

                        if (imageByte==null)
                        {
                            uploadOnFirestore("na");
                        }
                        else
                        {
                            uploadFile.uploadByteonfirestorage("Product_images",title.getText().toString(),imageByte);
                        }


                    }
                    else
                    {

                        if (imageByte==null)
                        {
                            Toast.makeText(AddSubProducts.this, "please select an image first", Toast.LENGTH_SHORT).show();
                        }else
                            uploadFile.uploadByteonfirestorage("Product_images",title.getText().toString(),imageByte);

                    }
                }



            }
        });



    }

    private void uploadOnFirestore(String url) {

        Gson gson = new Gson();
        String json = gson.toJson(list);

        Map<String,Object> todo = new HashMap<>();
        todo.put("category",category);
        todo.put("order",Integer.parseInt(order.getText().toString()));
        todo.put("desc",desc.getText().toString());
        if (!url.equals("na"))
        {
            todo.put("image",url);
        }

        todo.put("name",title.getText().toString());
        todo.put("price","na");

        todo.put("jsize",json);
        todo.put("timestamp", Timestamp.now());



        if (isupdate)
        {



            db.collection("Menus").document(category).collection("Items").document(uid).update(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {


                        finish();
                        Toast.makeText(AddSubProducts.this, "Successfully updated product", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(AddSubProducts.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            String id = UUID.randomUUID().toString();

            db.collection("Menus").document(category).collection("Items").document(id).set(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {


                        finish();
                        Toast.makeText(AddSubProducts.this, "Successfully uploaded product", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1001&&resultCode==RESULT_OK)
        {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,baos);
                imageByte = baos.toByteArray();



                imageview.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void getUrl(String url) {
     //   Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        uploadOnFirestore(url);

    }
}
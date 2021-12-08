package com.ankurupadhyay.finaljffadmin.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.UploadFile;
import com.ankurupadhyay.finaljffadmin.Model.CategoryModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity implements UploadFile.OnImageUpload {


    EditText category,title,description;


    UploadFile uploadFile;


    CircleImageView imageview;
    byte[] imageByte;
    boolean isupdate;

    Gson gson = new Gson();
    TextView submit;

    String pid="na";


    ImageView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        category = findViewById(R.id.category);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        close = findViewById(R.id.close);

        uploadFile = new UploadFile(this,this);
        imageview = findViewById(R.id.imageview);
        submit = findViewById(R.id.submit);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();

        if (intent.hasExtra("json"))
        {
            isupdate= true;

            category.setFocusable(false);
            CategoryModel model = gson.fromJson(intent.getStringExtra("json"),CategoryModel.class);

            title.setText(model.getName());
            description.setText(model.getDesc());
            category.setText(model.getCategory());
            pid = model.getId();


            try {
                Picasso.get().load(model.getImage()).placeholder(R.drawable.slogo).fit().centerCrop().into(imageview);
            }catch (IllegalArgumentException e)
            {

            }

        }





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



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isEmpty(title)||Common.isEmpty(description)||Common.isEmpty(category))
                {
                    if (Common.isEmpty(title))
                    {
                        title.setError("Please enter title");
                    }
                    if (Common.isEmpty(description))
                    {
                        description.setError("Plese enter description");
                    }

                    if (Common.isEmpty(category))
                    {
                        category.setError("Please enter product category");
                    }
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

                            Toast.makeText(AddProductActivity.this, "please select an image first", Toast.LENGTH_SHORT).show();
                        }else
                            uploadFile.uploadByteonfirestorage("Product_images",title.getText().toString(),imageByte);

                    }

                }




            }
        });





    }





    public void uploadOnFirestore(String url)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        String id = UUID.randomUUID().toString();

        Map<String,Object> todo = new HashMap<>();

        if (!url.equals("na"))
            todo.put("image",url);


        todo.put("category",category.getText().toString());
        todo.put("desc",description.getText().toString());

        todo.put("name",title.getText().toString());


        if (isupdate)
        {


            db.collection(Common.Menues_Db).document(pid).update(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(AddProductActivity.this, "Update Product Successfully", Toast.LENGTH_SHORT).show();
                        finish();


                    }


                }
            });



        }
        else
        {
            db.collection(Common.Menues_Db).document(title.getText().toString()).set(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(AddProductActivity.this, "Upload Product Successfully", Toast.LENGTH_SHORT).show();
                        finish();


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
        uploadOnFirestore(url);
    }
}
package com.ankurupadhyay.finaljffadmin.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Adapters.GridAdapter;
import com.ankurupadhyay.finaljffadmin.Common;
import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Firebase.UploadFile;
import com.ankurupadhyay.finaljffadmin.Model.GridModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements UploadFile.OnImageUpload, MyFirebase.getSliderImage {

    public GalleryFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    FloatingActionButton fab;

    UploadFile file;

    MyFirebase myFirebase;


    FirebaseFirestore db;
    List<GridModel> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        db = FirebaseFirestore.getInstance();

        myFirebase = new MyFirebase(getActivity());


        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        file = new UploadFile(getActivity(),this);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myFirebase.getSlider(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {



                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED||
                        getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, Common.CAMERA_PERMISSION_REQUEST);
                    Toast.makeText(getActivity(), "request permission", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.Choose_Gallery);



                }
            }
        });




        return view;
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (Common.Choose_Gallery==requestCode&&resultCode==RESULT_OK)
        {


            String id = UUID.randomUUID().toString();
            file.uploadonfirestorage(data.getData(),"Sliding_Image",id);


        }
    }

    @Override
    public void getUrl(String url) {



        String id  = UUID.randomUUID().toString();


        Map<String,Object> todo = new HashMap<>();
        todo.put("align","top");
        todo.put("image",url);
        todo.put("order",0);
        db.collection("SliderImages").document(id).set(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    updateValue();

                }




            }
        });






    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().toString().toLowerCase().equals("delete"))
        {
            db.collection("SliderImages").document(list.get(item.getGroupId()).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(getActivity(), "Image Delted successfully", Toast.LENGTH_SHORT).show();
                        updateValue();
                    }


                }
            });
        }


        return true;
    }

    private void updateValue() {

    myFirebase.getSlider(this);

    }

    @Override
    public void onGetSliderImage(List<GridModel> list) {
        this.list = list;
        GridAdapter adapter = new GridAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
    }
}
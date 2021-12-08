package com.ankurupadhyay.finaljffadmin.Firebase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UploadFile {

    Context context;
    StorageReference storageReference;
    OnImageUpload onImageUpload;


    public UploadFile(Context context, OnImageUpload onImageUpload) {
        this.context = context;
        this.onImageUpload = onImageUpload;
    }






    public void uploadonfirestorage(Uri saveUri, String folder,String name)
    {
        String id = UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReference(folder).child(name);
        UploadTask uploadTask = storageReference.putFile(saveUri);


        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                //finish.setText(""+(int)progress+"%");
                Toast.makeText(context, ""+progress+" %", Toast.LENGTH_SHORT).show();
                Log.e("kitnahua","onProgress : file "+progress+" % uploaded");



            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete( Task<UploadTask.TaskSnapshot> task) {
                //test.dismiss();
                Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        });




        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {

                    // Toast.makeText(Send_notice.this, "Failed", Toast.LENGTH_SHORT).show();
                    onImageUpload.getUrl(task.getException().getMessage());

                }

                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {


                if (task.isSuccessful()) {
                    String url = task.getResult().toString();
                    Log.d("PROFILE_DIRECTLINK", url);


                    onImageUpload.getUrl(url);


                }

            }
        });


    }


    public void uploadvideofirestorage(Uri saveUri, String folder, String name, final String purl, final OnVideoUpload onVideoUpload)
    {
        String id = UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReference(folder).child(name);
        // Create name for image
        UploadTask uploadTask = storageReference.putFile(saveUri);

        //create Task



//        LoadingClass loadingClass = new LoadingClass();
//        final Dialog test = loadingClass.showLoginDialog(context);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                //finish.setText(""+(int)progress+"%");
                Toast.makeText(context, ""+progress+" %", Toast.LENGTH_SHORT).show();
                Log.e("kitnahua","onProgress : file "+progress+" % uploaded");

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete( Task<UploadTask.TaskSnapshot> task) {
                //test.dismiss();
                Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        });





        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {

                    // Toast.makeText(Send_notice.this, "Failed", Toast.LENGTH_SHORT).show();

                }

                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {


                if (task.isSuccessful()) {
                    String url = task.getResult().toString();
                    Log.d("PROFILE_DIRECTLINK", url);


                    onVideoUpload.getVideoUrl(url,purl);


                }

            }
        });

    }




    //---------------------------reduce the image size



    public void uploadByteonfirestorage(String folder,String name,byte[] imagebyte)
    {

        storageReference = FirebaseStorage.getInstance().getReference(folder).child(name);

        try {

            UploadTask uploadTask = storageReference.putBytes(imagebyte);


            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                    double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    //finish.setText(""+(int)progress+"%");
                    Toast.makeText(context, ""+progress+" %", Toast.LENGTH_SHORT).show();
                    Log.e("kitnahua","onProgress : file "+progress+" % uploaded");



                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete( Task<UploadTask.TaskSnapshot> task) {
                    //test.dismiss();
                    Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                }
            });




            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        // Toast.makeText(Send_notice.this, "Failed", Toast.LENGTH_SHORT).show();
                        onImageUpload.getUrl(task.getException().getMessage());

                    }

                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {


                    if (task.isSuccessful()) {
                        String url = task.getResult().toString();
                        Log.d("PROFILE_DIRECTLINK", url);


                        onImageUpload.getUrl(url);


                    }

                }
            });




        } catch (Exception e)
        {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }



    public interface OnImageUpload
    {
        public void getUrl(String url);
    }

    public interface OnVideoUpload
    {
        public void getVideoUrl(String url,String purl);
    }
}

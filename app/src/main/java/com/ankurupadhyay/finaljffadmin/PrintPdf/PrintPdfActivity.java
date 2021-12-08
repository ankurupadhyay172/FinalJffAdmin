package com.ankurupadhyay.finaljffadmin.PrintPdf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.printservice.PrintDocument;
import android.view.View;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.MainActivity;
import com.ankurupadhyay.finaljffadmin.R;
import com.ankurupadhyay.finaljffadmin.Utils.PDFUtils;
import com.bumptech.glide.Glide;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PrintPdfActivity extends AppCompatActivity {



    private static final String FILE_PRINT = "b.pdf";
    AlertDialog dialog;


    List<SuperHeroModel> superHeroModelList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_pdf);


        dialog = new AlertDialog.Builder(this).setCancelable(false).setMessage("Please Wait").create();




        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }



        addSuperheroes();



        findViewById(R.id.btn_create_pdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ceratePdfFile(new StringBuilder(getAppPath()).append(FILE_PRINT).toString());
            }
        });
    }

    private void ceratePdfFile(String path) {


        if (new File(path).exists())
            new File(path).delete();

        try {

            Document document = new Document();

            //save

            PdfWriter.getInstance(document,new FileOutputStream(path));

            //open
            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Ankur upadhyay");
            document.addCreator("Ankur Upadhyay");



            //font setting

            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            //custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/a.ttf","UTF-8",BaseFont.EMBEDDED);



            //create title of document
            Font titleFont = new Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
            PDFUtils.addNewItem(document,"SUPER HEROES", Element.ALIGN_CENTER,titleFont);


            //Add more information
            Font textFont = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            PDFUtils.addNewItem(document,"Document By : ", Element.ALIGN_LEFT,textFont);
            PDFUtils.addNewItem(document,"Ankur Upadhyay: ", Element.ALIGN_LEFT,textFont);


            PDFUtils.addLineSeperator(document);



            //Add detail
            PDFUtils.addLineSeperator(document);
            PDFUtils.addNewItem(document,"Ankur Upadhyay ",Element.ALIGN_LEFT,textFont);
            PDFUtils.addLineSeperator(document);


            //Use RxJava , fetch Image from URL and add pdf
            Observable.fromIterable(superHeroModelList).flatMap(model->getBitmapFromUrl(this,model,document))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(model->{
                //On Next
                //Each Item,we will add detail
                PDFUtils.addNewItemWithLeftAndRight(document,model.getName(),"",titleFont,textFont);

                PDFUtils.addLineSeperator(document);


                PDFUtils.addNewItem(document,model.getDescription(),Element.ALIGN_LEFT,textFont);


                PDFUtils.addLineSeperator(document);
            },throwable -> {
                dialog.dismiss();
                Toast.makeText(this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            },()->{
                document.close();
                dialog.dismiss();

                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();



                printPdf();

            });





        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dialog.dismiss();

        }


    }

    private void printPdf() {



        try {
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(this,new StringBuilder(getAppPath())
            .append(FILE_PRINT).toString(),FILE_PRINT);
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());

        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private Observable<SuperHeroModel> getBitmapFromUrl(Context context, SuperHeroModel model, Document document) {

        return Observable.fromCallable(()->{

            Bitmap bitmap = Glide.with(context).asBitmap().load(model.getImage()).submit().get();


            Image image = Image.getInstance(bitmapToByteArray(bitmap));
            image.scaleAbsolute(100,100);
            document.add(image);

            return model;
        });

    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();



    }

    private String getAppPath() {

        File dir = new File(Environment.getExternalStorageDirectory()+File.separator+"Ankur"
        +File.separator);

        if (!dir.exists())
        {
            dir.mkdir();

            Toast.makeText(this, "folder created", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "folder exixst", Toast.LENGTH_SHORT).show();
        }



        return dir.getPath()+File.separator;



    }

    private void addSuperheroes() {


        SuperHeroModel superHeroModel = new SuperHeroModel("Justice League","https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
               "The Justice League is a team of fictional superheroes " );

        superHeroModelList.add(superHeroModel);


        superHeroModel = new SuperHeroModel("Avengers","https://homepages.cae.wisc.edu/~ece533/images/fruits.png",
                "The Avengers are a fictional team of suerheroes appearing in Marvel Comics");

        superHeroModelList.add(superHeroModel);






    }
}
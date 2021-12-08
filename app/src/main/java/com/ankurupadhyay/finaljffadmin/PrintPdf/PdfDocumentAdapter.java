package com.ankurupadhyay.finaljffadmin.PrintPdf;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends PrintDocumentAdapter {

    Context context;
    String path,name;


    public PdfDocumentAdapter(Context context, String path, String name) {
        this.context = context;
        this.path = path;
        this.name = name;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

        if (cancellationSignal.isCanceled())
            callback.onLayoutCancelled();
        else
        {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder(name);
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();
          //  callback.onLayoutFinished(builder.build(),!newAttributes.equals(oldAttributes));

        }


    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

        InputStream in = null;
        OutputStream out = null;



        try {
            File file = new File(path);
            in = new FileInputStream(file);
            out = new FileOutputStream(destination.getFileDescriptor());


            byte[] buff = new byte[1634];
            int size;
            while ((size=in.read(buff)) >=0&&cancellationSignal.isCanceled())
                out.write(buff,0,size);
            if (cancellationSignal.isCanceled())
                callback.onWriteCancelled();
            else
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});



        }catch (FileNotFoundException e)
        {

        }catch (IOException e)
        {

        }
        finally {
            try {
                in.close();
                out.close();
            }catch (IOException e)
            {

            }
        }



    }
}

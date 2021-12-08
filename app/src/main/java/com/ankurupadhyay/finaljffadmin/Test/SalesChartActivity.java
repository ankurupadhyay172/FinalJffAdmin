package com.ankurupadhyay.finaljffadmin.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ankurupadhyay.finaljffadmin.Firebase.MyFirebase;
import com.ankurupadhyay.finaljffadmin.Model.OrderModel;
import com.ankurupadhyay.finaljffadmin.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesChartActivity extends AppCompatActivity implements MyFirebase.OngetPurchased {


    MyFirebase myFirebase;
    BarChart barChart;
    BarDataSet barDataSet;
    BarData barData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_chart);
        barChart = findViewById(R.id.barchartss);

        myFirebase = new MyFirebase(this);
        myFirebase.getSuccessOrders(this);




    }

    @Override
    public void onGetPurchased(List<OrderModel> list) {

        List<BarEntry> barEntryList = new ArrayList<>();
        int jan_total =0;
        int fab_total =0;
        int mar_total =0;

        int apr_total =0;
        int may_total =0;

        for (OrderModel model:list)
        {
            Date date = model.getTimestamp().toDate();



            if (date.getMonth()==0)
            {

                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                jan_total = jan_total+(int)totalamount;

            }
            if (date.getMonth()==1)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                fab_total = fab_total+(int)totalamount;

            }

            if (date.getMonth()==2)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                mar_total = mar_total+(int)totalamount;

            }

            if (date.getMonth()==3)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                apr_total = apr_total+(int)totalamount;

            }
            if (date.getMonth()==4)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                may_total = may_total+(int)totalamount;

            }
        }

        barEntryList.add(new BarEntry(0,jan_total));
        barEntryList.add(new BarEntry(1,fab_total));
        barEntryList.add(new BarEntry(2,mar_total));
        barEntryList.add(new BarEntry(3,apr_total));
        barEntryList.add(new BarEntry(4,may_total));
        String[] years = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};


        barDataSet = new BarDataSet(barEntryList,"Sales Chart");

        barData = new BarData(barDataSet);


        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barChart.invalidate();


        XAxis xAxis = barChart.getXAxis();

        barData.setBarWidth(0.9f);

        barDataSet.setValueTextSize(9f);
        
        barChart.setDescription("");

        barChart.setFitBars(true);

        xAxis.setLabelCount(1);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return years[(int)value];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
    }
}
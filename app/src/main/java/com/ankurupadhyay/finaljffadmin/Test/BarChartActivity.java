package com.ankurupadhyay.finaljffadmin.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.ankurupadhyay.finaljffadmin.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.barchart);

        BarDataSet barDataSet = new BarDataSet(dataValues(),"salesvalues");

        BarDataSet barDataSet2 = new BarDataSet(dataValues2(),"salesvalues");


        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();
        barDataSet.setColor(Color.RED);



        barData.setBarWidth(0.9f);



//        //add legend to chart
//        Legend legend = barChart.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);


        barChart.getAxisLeft().setDrawTopYLabelEntry(false);

        barChart.getAxisRight().setDrawLabels(false);


        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setEnabled(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDescription("");

        String[] years = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        XAxis xAxis =barChart.getXAxis();
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

    public ArrayList<BarEntry> dataValues()
    {
        ArrayList<BarEntry> list = new ArrayList<>();
        list.add(new BarEntry(0,3000));
        list.add(new BarEntry(1,4000));
        list.add(new BarEntry(2,6000));
        list.add(new BarEntry(3,1300));
        list.add(new BarEntry(4,1000));

        list.add(new BarEntry(5,300));
        list.add(new BarEntry(6,400));
        list.add(new BarEntry(7,200));
        list.add(new BarEntry(8,130));
        list.add(new BarEntry(9,10));

        list.add(new BarEntry(10,200));
        list.add(new BarEntry(11,130));
        return list;
    }


    public ArrayList<BarEntry> dataValues2()
    {
        ArrayList<BarEntry> list = new ArrayList<>();
        list.add(new BarEntry(0,300));
        list.add(new BarEntry(1,400));
        list.add(new BarEntry(2,200));
        list.add(new BarEntry(3,130));
        list.add(new BarEntry(4,10));

        list.add(new BarEntry(5,300));
        list.add(new BarEntry(6,400));
        list.add(new BarEntry(7,200));
        list.add(new BarEntry(8,130));
        list.add(new BarEntry(9,10));

        list.add(new BarEntry(10,200));
        list.add(new BarEntry(11,130));
        return list;
    }
}
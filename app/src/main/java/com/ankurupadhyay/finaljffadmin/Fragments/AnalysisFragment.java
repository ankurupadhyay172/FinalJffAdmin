package com.ankurupadhyay.finaljffadmin.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

public class AnalysisFragment extends Fragment implements MyFirebase.OngetPurchased {


    public AnalysisFragment() {
        // Required empty public constructor
    }

    MyFirebase myFirebase;
    BarChart barChart;
    BarDataSet barDataSet;
    BarData barData;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);

        barChart = view.findViewById(R.id.barchartss);
        progressBar = view.findViewById(R.id.progress_bar);
        myFirebase = new MyFirebase(getActivity());
        myFirebase.getSuccessOrders(this);




        return view;
    }

    @Override
    public void onGetPurchased(List<OrderModel> list) {

        progressBar.setVisibility(View.GONE);


        List<BarEntry> barEntryList = new ArrayList<>();
        int jan_total =0;
        int fab_total =0;
        int mar_total =0;

        int apr_total =0;
        int may_total =0;






        int jun_total =0;
        int jul_total =0;
        int aug_total =0;

        int sep_total =0;
        int oct_total =0;

        int nov_total =0;
        int dec_total =0;

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





            if (date.getMonth()==5)
            {

                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                jun_total = jun_total+(int)totalamount;

            }
            if (date.getMonth()==6)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                jul_total = jul_total+(int)totalamount;

            }

            if (date.getMonth()==7)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                aug_total = aug_total+(int)totalamount;

            }

            if (date.getMonth()==8)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                sep_total = sep_total+(int)totalamount;

            }
            if (date.getMonth()==9)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                oct_total = oct_total+(int)totalamount;

            }




            if (date.getMonth()==10)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                nov_total = nov_total+(int)totalamount;

            }
            if (date.getMonth()==9)
            {
                double totalamount=Double.parseDouble(model.getTotal_price().trim());

                dec_total = dec_total+(int)totalamount;

            }



        }

        barEntryList.add(new BarEntry(0,jan_total));
        barEntryList.add(new BarEntry(1,fab_total));
        barEntryList.add(new BarEntry(2,mar_total));
        barEntryList.add(new BarEntry(3,apr_total));
        barEntryList.add(new BarEntry(4,may_total));

        if (jun_total!=0)
        {
            barEntryList.add(new BarEntry(5,jun_total));
        }

        if (jul_total!=0)
        {
            barEntryList.add(new BarEntry(6,jul_total));
        }

        if (aug_total!=0)
        {
            barEntryList.add(new BarEntry(7,aug_total));
        }
        if (sep_total!=0)
        {
            barEntryList.add(new BarEntry(8,sep_total));
        }
        if (oct_total!=0)
        {
            barEntryList.add(new BarEntry(9,oct_total));
        }
        if (nov_total!=0)
        {
            barEntryList.add(new BarEntry(10,nov_total));
        }
        if (dec_total!=0)
        {
            barEntryList.add(new BarEntry(11,dec_total));
        }







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
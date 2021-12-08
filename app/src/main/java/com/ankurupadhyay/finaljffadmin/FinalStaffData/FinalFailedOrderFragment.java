package com.ankurupadhyay.finaljffadmin.FinalStaffData;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ankurupadhyay.finaljffadmin.R;

public class FinalFailedOrderFragment extends Fragment {


    public FinalFailedOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_final_failed_order, container, false);



        return view;
    }
}
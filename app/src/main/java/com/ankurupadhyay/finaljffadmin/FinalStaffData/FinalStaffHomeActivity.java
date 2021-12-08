package com.ankurupadhyay.finaljffadmin.FinalStaffData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.androidcafe.jffstaff.Fragments.StaffHomeFragment;
import com.ankurupadhyay.finaljffadmin.R;

public class FinalStaffHomeActivity extends AppCompatActivity {



    Fragment fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_staff_home);



        fragment = new FinalStaffHomeFragment();
        loadFragment(fragment);


    }



    /*load fragment method can be here*/
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(com.androidcafe.jffstaff.R.id.framlayout, fragment);
        transaction.commit();
    }

}
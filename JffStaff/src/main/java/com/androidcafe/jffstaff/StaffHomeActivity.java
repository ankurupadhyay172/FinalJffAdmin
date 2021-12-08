package com.androidcafe.jffstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.androidcafe.jffstaff.Fragments.StaffHomeFragment;

public class StaffHomeActivity extends AppCompatActivity {


    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);




        fragment = new StaffHomeFragment();
        loadFragment(fragment);
    }




    /*load fragment method can be here*/
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framlayout, fragment);
        transaction.commit();
    }

}
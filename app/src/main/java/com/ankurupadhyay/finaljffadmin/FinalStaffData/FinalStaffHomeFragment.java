package com.ankurupadhyay.finaljffadmin.FinalStaffData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.androidcafe.jffstaff.Fragments.CookingFragment;
import com.androidcafe.jffstaff.Fragments.DeliveredFragment;
import com.androidcafe.jffstaff.Fragments.FailedFragment;
import com.androidcafe.jffstaff.Fragments.OnGoingFragment;
import com.androidcafe.jffstaff.Fragments.PendingOrdersFragment;
import com.androidcafe.jffstaff.Fragments.StaffHomeFragment;
import com.ankurupadhyay.finaljffadmin.R;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.HelperUtils;
import com.kekstudio.dachshundtablayout.indicators.DachshundIndicator;
import com.kekstudio.dachshundtablayout.indicators.LineFadeIndicator;
import com.kekstudio.dachshundtablayout.indicators.LineMoveIndicator;
import com.kekstudio.dachshundtablayout.indicators.PointFadeIndicator;
import com.kekstudio.dachshundtablayout.indicators.PointMoveIndicator;

public class FinalStaffHomeFragment extends Fragment {


    public FinalStaffHomeFragment() {
        // Required empty public constructor
    }


    View view;

    public static final String DOG_BREEDS[] = {"Pending", "Cooking", "Ongoing","Delivered","Failed"};

    public ViewPager viewPager;
    public DachshundTabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_final_staff_home, container, false);

        viewPager = view.findViewById(R.id.view_pager1);
        tabLayout = view.findViewById(R.id.tab_layout);

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);




        return view;
    }


    public void onClickDachshund(View view) {
        tabLayout.setAnimatedIndicator(new DachshundIndicator(tabLayout));
    }

    public void onClickPointMove(View view) {
        tabLayout.setAnimatedIndicator(new PointMoveIndicator(tabLayout));
    }

    public void onClickPointMoveAccelerate(View view) {
        PointMoveIndicator pointMoveIndicator = new PointMoveIndicator(tabLayout);
        pointMoveIndicator.setInterpolator(new AccelerateInterpolator());
        tabLayout.setAnimatedIndicator(pointMoveIndicator);
    }

    public void onClickLineMove(View view) {
        tabLayout.setAnimatedIndicator(new LineMoveIndicator(tabLayout));
    }

    public void onClickPointFade(View view) {
        tabLayout.setAnimatedIndicator(new PointFadeIndicator(tabLayout));
    }

    public void onClickLineFade(View view) {
        LineFadeIndicator lineFadeIndicator = new LineFadeIndicator(tabLayout);
        tabLayout.setAnimatedIndicator(lineFadeIndicator);

        lineFadeIndicator.setSelectedTabIndicatorHeight(HelperUtils.dpToPx(2));
        lineFadeIndicator.setEdgeRadius(0);
    }

    public static class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    FinalPendingFragment tab0 = new FinalPendingFragment();
                    return tab0;
                case 1:
                    FinalCookingFragment tab1 = new FinalCookingFragment();
                    return tab1;
                case 2:
                    FinalOutForDeliveryFragment tab2 = new FinalOutForDeliveryFragment();
                    return tab2;

                case 3:
                    FinalDeliveredFragment tab3 = new FinalDeliveredFragment();
                    return tab3;
                case 4:
                    FailedFragment tab4 = new FailedFragment();
                    return tab4;

                default:
                    return null;
            }



        }



        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return DOG_BREEDS[position];
        }
    }

    public static class PageFragment extends Fragment {
        public PageFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(com.androidcafe.jffstaff.R.layout.fragment_staff_home, container, false);
        }
    }


}
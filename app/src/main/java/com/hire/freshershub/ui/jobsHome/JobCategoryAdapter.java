package com.hire.freshershub.ui.jobsHome;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class JobCategoryAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public JobCategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CollegeJobs();
        } else {
            return new ApplicationJobs();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "On Campus";
        } else {
            return "Off Campus";
        }
    }
}

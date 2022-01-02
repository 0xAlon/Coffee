package com.networks.coffee;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static int tempItemPosition = -1;

    public static int getTempItemPosition() {
        return tempItemPosition;
    }

    public static void setTempItemPosition(int tempItemPosition) {
        ViewPagerAdapter.tempItemPosition = tempItemPosition;
    }

    public ViewPagerAdapter(
            @NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new InsideFragment();
        else
            fragment = new OutsideFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Inside";
        else if (position == 1)
            title = "Outside";
        return title;
    }
}
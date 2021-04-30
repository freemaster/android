package com.newkoad.deliver;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private boolean enableSwipe;
    private final List<Fragment> lstFragment = new ArrayList<>();
    private final List<String> lstTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {

        super(fm);
        init();
    }
    private void init(){
        enableSwipe = true;
    }
    @Override
    public Fragment getItem(int i) {

        return lstFragment.get(i);
    }

    @Override
    public int getCount() {

        return lstTitle.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitle.get(position);

    }

    public void AddFragment (Fragment fragment, String title){
        lstFragment.add(fragment);
        lstTitle.add(title);
    }



    public void setEnableSwipe(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
    }


}

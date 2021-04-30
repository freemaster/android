package com.newkoad.deliver.add;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import com.newkoad.deliver.R;
import com.newkoad.deliver.ViewPagerAdapter;
import com.newkoad.deliver.add.Addition;
import com.newkoad.deliver.add.SetAlarmFragment;

public class SettingActivity extends Addition {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(" 환경설정");
        headerCloseBtn =findViewById(R.id.header_closeBtn);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment( new SetAlarmFragment(), "alarm");

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("알람");



    }
}

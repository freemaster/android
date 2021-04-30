package com.newkoad.deliver.add;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import com.newkoad.deliver.R;
import com.newkoad.deliver.ViewPagerAdapter;

public class MoneyActivity extends Addition {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(" 거래 ");
        headerCloseBtn =findViewById(R.id.header_closeBtn);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment( new MoneyGetFragment(),"get");
        adapter.AddFragment( new MoneySendFragment(), "send");
        adapter.AddFragment( new MoneyListFragment(), "list");

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

        tabLayout.getTabAt(0).setText("환급신청");

        tabLayout.getTabAt(1).setText("예치금 거래");

        tabLayout.getTabAt(2).setText("거래내역");

    }
}

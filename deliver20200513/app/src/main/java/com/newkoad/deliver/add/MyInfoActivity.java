package com.newkoad.deliver.add;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import com.newkoad.deliver.R;
import com.newkoad.deliver.ViewPagerAdapter;

public class MyInfoActivity extends Addition {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my_info);



        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(" 내정보");
        headerCloseBtn =findViewById(R.id.header_closeBtn);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment( new MyPageFragment(),"page");
        adapter.AddFragment( new MyCountFragment(), "acount");

        if(mainApp.getAddBranch()){

        }else {
            adapter.AddFragment(new MyBoxFragment(), "box");
            adapter.AddFragment(new MyAttendFragment(), "attend");
        }

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("마이페이지");
        tabLayout.getTabAt(1).setText("내계좌");

        if(mainApp.getAddBranch()) {


        }else{
            tabLayout.getTabAt(2).setText("디디박스");
            tabLayout.getTabAt(3).setText("출 / 퇴근");

        }

    }

}

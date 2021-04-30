package com.test.ex0406;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView nv;

    //2-1) 각각 프래그먼트 객체 생성
    private Fragment_1 fg1;
    private Fragment_2 fg2;
    private Fragment_3 fg3;
    private Fragment_4 fg4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fg1 = new Fragment_1();
        fg2 = new Fragment_2();
        fg3 = new Fragment_3();
        fg4 = new Fragment_4();

        //2-2) 최초 실행될 프로그먼트 고르기!
        //2-2-1) fragment 가 들어갈 공간(frameLayout)의 ID
        //2-2-3) 갈아끼울 Fragment객체
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fg1).commit();

        nv = (BottomNavigationView)findViewById(R.id.bottomNavigationView);

        nv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.tab1 :
                        Toast.makeText(getApplicationContext(), "Web menu selected!", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fg1).commit();
                    break;
                    case R.id.tab2 :
                        Toast.makeText(getApplicationContext(), "stopwatch menu selected!", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fg2).commit();
                    break;
                    case R.id.tab3 :
                        Toast.makeText(getApplicationContext(), "Doduge menu selected!", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fg3).commit();
                        break;
                    case R.id.tab4 :
                        Toast.makeText(getApplicationContext(), "Setting menu selected!", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fg4).commit();
                        break;
                }

                //retrun false => 아직 현재 이벤트가 끝나지 않았다.!
                return true;
            }
        });
    }
}
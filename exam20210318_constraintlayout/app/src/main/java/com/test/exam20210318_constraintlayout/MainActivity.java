package com.test.exam20210318_constraintlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_main 이였던 xml을 kakao_login 으로 변경
        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.kakao_login);
        setContentView(R.layout.kakao_login_form);
    }
}
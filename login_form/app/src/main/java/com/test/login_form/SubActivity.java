package com.test.login_form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SubActivity extends AppCompatActivity {
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        tv_result = (TextView)findViewById(R.id.tv_result);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");
        String userpass = intent.getStringExtra("userpass");
        tv_result.setText(userid+"님 로그인 성공");

        //위문장에서 getIntent()를 변수로 저장하지 않고 바로 대입해도 된다.
        //tv_result.setText(getIntent().getStringExtra("id");
    }
}
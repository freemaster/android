package com.test.exam20210319;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {
    private Button pre;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        tv_result = (TextView)findViewById(R.id.tv_result);
        pre = (Button)findViewById(R.id.pre);

        Intent intent = getIntent();
        String str = intent.getStringExtra("data");
        tv_result.setText(str);

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //아래처럼 다시 mainactivity로 돌아갈 경우 actvity가 task에 쌓여있게된다.
                //그래서 아래처럼 사용하면 안되고 actvity를 task에서 지우기 위해 finish()사용
                //다시 돌아갈 페이지를 task에 쌓아 놓고 싶을때는 아래 intent를 사용,
                //Intent intent = new Intent(SubActivity.this, MainActivity.class);
                //startActivity(intent);
                
                //현재 activity를 task에서 종료
                finish(); 
            }
        });
    }
}
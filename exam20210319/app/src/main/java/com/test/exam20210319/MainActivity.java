package com.test.exam20210319;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Intent.ACTION_CALL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn, btn2, call, next;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=(Button)findViewById(R.id.btn);
        btn2=(Button)findViewById(R.id.btn2);
        call=(Button)findViewById(R.id.call);
        next=(Button)findViewById(R.id.next);
        editText=(EditText)findViewById(R.id.editText);

        //두개 모두 같은 뜻이다.
        //btn.setOnClickListener(this);
        btn.setOnClickListener(MainActivity.this);
        btn2.setOnClickListener(this);
        call.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //1. Intent 객체 생성하기(액션, 데이터)
        if(v.getId() == R.id.btn) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://naver.com"));
            //2. Intent 실행시키기
            startActivity(intent);
        }

        if(v.getId() == R.id.btn2) {
            Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01096031229"));
            startActivity(intent2);
        }

        if(v.getId() == R.id.call) {
            Intent intent3 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:01096031229"));

            //현재 권한이 부여되어 있는지 검사
            //checkSelfPermission에서 권한이 있으면 0 , 권한이 없으면 -1의 값을 전달
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0 );
                return;
            }

            startActivity(intent3);
        }

        if(v.getId() == R.id.next) {
            //보내는 사람 mainactivity.this, 받는사람 subactivity.class
            Intent intent4 = new Intent(MainActivity.this, SubActivity.class);

            //여러개의 데이터 전송시 putExtra를 어러번 반복해서 값을 보내주면 된다.
            intent4.putExtra("data", editText.getText().toString());
            startActivity(intent4);
        }


        Toast.makeText(MainActivity.this, "뭘하고 싶은겨???.", Toast.LENGTH_SHORT).show();
        Log.v("로그인 ::", String.valueOf(btn));
    }
}
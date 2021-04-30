package com.test.exam20210311;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //변수는 상단에 한번만 정의
    ImageView iv;
    int current = 0;
    int[] imgs = {R.drawable.a1, R.drawable.a2, R.drawable.a3};
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = findViewById(R.id.imageView);
        layout = findViewById(R.id.lay1);
    }

    //버튼을 눌렀을 때 실행될 메소드 생성!
    public void btnClick(View v) {
        //이미지뷰의 이미지 속성을 바꾸자
        //1. imageview id값을 통해서 찾아오기
        if(v.getId()==R.id.btn1) {
            //iv.setImageResource(R.drawable.a2);
            current = 0;
            layout.setBackgroundColor(Color.RED);
        } else if(v.getId() == R.id.btn2) {
            //iv.setImageResource(R.drawable.a3);
            current = 1;
            layout.setBackgroundColor(Color.GREEN);
        } else if(v.getId() == R.id.btn3) {
            //iv.setImageResource(R.drawable.a1);
            current = 2;
            layout.setBackgroundColor(Color.GRAY);
        } else if(v.getId() == R.id.next) {

            if(current==imgs.length-1) {
                //Toast.makeText(MainActivity.this, "마지막 사진입니다.", Toast.LENGTH_SHORT).show();
                current = 0;
            } else {
                current++;
            }
        } else if(v.getId() == R.id.pre) {
            if(current==0) {
                //Toast.makeText(MainActivity.this, "첫뻔째 사진입니다.", Toast.LENGTH_SHORT).show();
                current = 2;
            } else {
                current--;
            }
        }
        iv.setImageResource(imgs[current]);
    }

    //버튼 사라지게 하기
    public void btnVisible(View v) {
        if(iv.getVisibility() == View.INVISIBLE) {
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.INVISIBLE);
        }

    }
}
package com.test.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_shake;
    ImageView img1, img2;
    TextView tv_score;
    int[] dice = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    int score1 = 0, score2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_shake = (Button)findViewById(R.id.btn_shake);
        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        tv_score = (TextView)findViewById(R.id.tv_score);

        btn_shake.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //버튼을 누르면 실행될 메소드
        //주사위 이미지 중에서 한개를 랜덤으로 뽑아야 함!(두 번)
        //1. 이미지 리소스들을 (R.drawable.dice1........) 배열에 넣고 배열 인덱스 중에서 랜덤으로 뽑아줌
        //2. 이미지 뷰의 이미지를 방금 뽑은 리소스로 바꿔줌.

        //랜덤을 구해서 넣어준다
        int num1 = new Random().nextInt(6);
        int num2 = new Random().nextInt(6);


        img1.setImageResource(dice[num1]);
        img2.setImageResource(dice[num2]);

        //스코어 표시
        if(num1 > num2) {
            score1++;
        } else if(num2 > num1) {
            score2++;
        } else {
            Toast.makeText(this, "무승부입니다.", Toast.LENGTH_SHORT).show();
        }

        tv_score.setText(score1+" : "+score2);
    }
}
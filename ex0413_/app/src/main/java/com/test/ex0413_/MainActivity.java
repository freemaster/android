package com.test.ex0413_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tv_count;
    private ImageView[] imgArray = new ImageView[9];
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_count = (TextView)findViewById(R.id.tv_count);
        for(int i=0; i<imgArray.length; i++) {
            //i를 상수로 정의
            final int pos = 1;

            //xml에 정의한 화면 View 의 id ( 반복문을 통해서 한번에 정의 )
            int img_id = getResources().getIdentifier("img"+(i+1), "id", getPackageName());
            imgArray[i] = (ImageView)findViewById(img_id);

            //두더지 상태
            imgArray[i].setTag("off");

            //두더지에 대한 Thread 실행
            Thread doThread = new doThread(i);
            doThread.start();

            //클릭이벤트 적용
            imgArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imgArray[pos].getTag().toString().equals("on")) {
                        score++;
                    } else {
                        score--;
                    }
                    tv_count.setText(String.valueOf(score));
                }
            });
        }
    }

    Handler doHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //두더지에 대한 상태 , 이미지 등을 변경
            imgArray[msg.arg1].setImageResource(msg.arg2);
            imgArray[msg.arg1].setTag(msg.obj);
        }
    };

    class doThread extends Thread {
        //몇번째 두더지 인지 초기화 해주는 녀석으로 사용
        int pos = 0; //두더지 번호

        public doThread(int pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            while(true) {
                //두더지가 땅에 아래로 내려가 있는 시간
                int offTime = new Random().nextInt(5000) + 500; //0초부터 4.5초까지
                int onTime = new Random().nextInt(3000) + 500;

                try {
                    Thread.sleep(offTime); //0초부터 4.5초까지
                    Message msg = new Message();

                    //핸들러에게 전달할 값? 상태값( on ), on이라는 이미지의 이름(on.png), 몇번째 두더지인지
                    msg.arg1 = pos; //몇번째 두더지
                    msg.arg2 = R.drawable.on; //어떤 사진으로 바꿀껀지
                    msg.obj = "on"; //상태값
                    doHandler.sendMessage(msg);

                    //두더지를  땅 아래로 숨기는 기능을 구현
                    // - 두더지가 서있는 시간값
                    // - 두더지 번호, 사진과, 상태값을 새로운 Message객체를 생성하여 핸들러에게 전달
                    Thread.sleep(onTime);
                    msg = new Message();
                    msg.arg1 = pos;
                    msg.arg2 = R.drawable.off;
                    msg.obj = "off";
                    doHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
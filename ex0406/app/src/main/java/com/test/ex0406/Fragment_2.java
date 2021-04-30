package com.test.ex0406;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_2 extends Fragment {
    //Three 만들기!!
    //1) Thread 클래스(설계도)를 정의한다. (Thread 상속) (#1번)
    //2) run 메소드(Thread 시작했을 때 호출되는 메소드)를 오버라이딩한다. (#2번)
    //3) setText(UI업데이트)하기 위해 handler 만들기 (#3번)
    //4) Message 전송 (sendMessage)하고 Handler에서 값 전달 받음(HandlerMessage)
    //5) Handler에서 setText처리하고 종료

    private TextView tv_tv1;
    private TextView tv_tv2;
    private Button startBtn1;
    private Button startBtn2;

    //(#31번)
    cntThread thread;
    cntThread thread2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //self 실습
        // - tv1 찾아 온다.
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        tv_tv1 = (TextView)view.findViewById(R.id.tv_tv1);
        tv_tv2 = (TextView)view.findViewById(R.id.tv_tv2);
        startBtn1 = (Button)view.findViewById(R.id.startBtn1);
        startBtn2 = (Button)view.findViewById(R.id.startBtn2);

        startBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thread 실행시키는 법
                // 11) Thread를 상속받아 만들어진 클래스의 객체를 생성한다. (#11번)
                //(#11번)
                //이녀석을 필드변수로 만들어 버린다. ===> 맨위로 올려 (#31번)
                //cntThread thread = new cntThread();

                // 12) start메소드를 호출한다.(#12번)
                //(#12번)
                //thread.start(); //현재상태로 사용하게 되면 startBtn1을 클릭할때마다 새로운 쓰레드가 계속 생겨난다.

                //start 버튼을 누르면 버튼에 적힌 글자가 stop로 바뀌게 해봅시다.
                //(#21)
                //startBtn1.setText("Stop");

                //버튼에 적혀있는 글자가 start! 일때만 쓰레드를 생성하고 시작!
                //아니면 멈춤!
                //(#22)
                if(startBtn1.getText().toString().equals("START")) {
                    //(#32) 쓰레드를 다시 정의한다.
                    thread = new cntThread(tv_tv1); //지금 생성되는 Thread가 필요한 TextView를 보내줘야함.
                    thread.start();
                    startBtn1.setText("STOP");
                } else {
                    thread.interrupt();
                    //cpu 뺏기(가로채기)
                    //thread.interrupt를 하면 thread 내부에서는 interrup Exception 이 발생함
                    //멈추게 하려면??? interrupException을 처리할 수 있는 catch문에서 run메소드를 끝내버리면 됨. (#32번)
                    startBtn1.setText("START");
                }
            }
        });

        startBtn2.setOnClickListener(new View.OnClickListener() { //(#37번)
            @Override
            public void onClick(View v) {
                if(startBtn2.getText().toString().equals("START")) {
                    //(#32) 쓰레드를 다시 정의한다.
                    thread2 = new cntThread(tv_tv2); //지금 생성되는 Thread가 필요한 TextView를 보내줘야함.
                    thread2.start();
                    startBtn2.setText("STOP");
                } else {
                    thread2.interrupt();
                    startBtn2.setText("START");
                }
            }
        });
        return view;
    }

    //(#3번) 핸들러 만들기
    //Handler handler = new Handler() {
    Handler handler = new Handler() {//(#35)
        //alt+insert >> Override Method >> handleMessage
        @Override
        public void handleMessage(Message msg) {
            //super은 주석
            //super.handleMessage(msg);

            //msg에서 arg1에 저장된 값을 setText
            //★★★★ setText할 때 정수형을 넣으면 에러가 납니다.
            //정수형으로 넣으면 정해준 숫자를 참조할 주소값이 돼버린다.
            //그래서 문자로 바꿔주기 위해 +"" 를 해준다.
            //(#5번)
            //tv_tv1.setText(msg.arg1+"");
            ((TextView)msg.obj).setText(msg.arg1+""); //(#36)
        }
    };

    //(#1번) Thread(쓰레드) 정의
    class cntThread extends Thread {
        //(#2번) run메소드 오버라이딩
        //Alt + insert >> Override Method >> run 검색

        //매개변수로 TextView를 전달 받는 생성자 (#33번)
        private TextView textView;
        public cntThread(TextView textView) {
            this.textView = textView;

        }

        @Override
        public void run() {
            //super.run은 주석 처리
            //super.run();

            //Thread 가 할일을 적어주면 됨
            //1~10까지 1초씩 새는 코드
            for(int i=1; i<=10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //(#32)
                    //e.printStackTrace();
                    return;
                    //Thread가 CPU를 사용하다가 양보 => yield
                    //CPU를 뺏어옴 => interrupt
                }

                //i => TextView에 setText만 하면 되는데~~~

                //★★★★ 여기(SubThread => 내가 만든 Thread에서는 setText 못함.
                //많은 Thread가 UI업데이트가 가능하다면 화면이 혼잡해질 가능성이 있다.(동기화를 시키지 않은 경우)
                //Android는 UI업데이트를 위해 Thread들을 동기화 해야 하는 번거로움을 차단.
                //UI업데이트는 Main Thread에서만 가능하다
                //외부 Thread에서 UI업데이트 하려면 Handler를 이용

                //Handler에 메세지를 전송해서 사용 (#4번)
                Message msg = new Message();
                //Message에는 변수가 3개 있음.
                //Object obj, int arg1, int arg2
                //변수에 각각 값을 저장할 수 있음.
                //현재 i 값을 handle에 보내서 textView에  setText 함.
                msg.arg1 = i;
                msg.obj = textView; //(#34번)
                handler.sendMessage(msg);
            }
        }
    }
}
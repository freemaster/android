package com.test.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //View들을 저장할 변수
    //아래와 같은 코드는
    //setContentView 에서 찾을 수가 없다.
    //setContentView 가 없으면 아래 코드를 가져올 수가 없다.
    //꼭 setContentView 아래에 findViewById 가 존재해야 한다.
    //TextView userId = findViewById(R.id.userId);
    TextView userId;
    TextView userPass;
    TextView loginOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //아래에서 가져온 변수를 전역변수로 설정해 준다.
        //TextView userId = findViewById(R.id.userId);
        //TextView userPass = findViewById(R.id.userPass);
        //TextView loginOk = findViewById(R.id.loginOk);

        //최초 한번만 View를 찾아준다.
        //반드시 setContentView 아래에 있어야 한다.
        userId = findViewById(R.id.userId);
        userPass = findViewById(R.id.userPass);
        loginOk = findViewById(R.id.loginOk);
    }

    public void btnSubmit(View v){
        String id = "admin";
        String pw = "1234";

        //버튼을 누를때마다 아래의 findView가 계속 실행이 된다.
        //그래서 이부분을 onCreate 안에 넣어준다.
        //TextView userId = findViewById(R.id.userId);
        //TextView userPass = findViewById(R.id.userPass);
        //TextView loginOk = findViewById(R.id.loginOk);

        String str = "";
        if(userId.getText().toString().equals(id)) {
            if(userPass.getText().toString().equals(pw)) {
                loginOk.setText("ADMIN님 환영합니다.");
                userPass.setText("");
                userId.setText("");
            } else {
                //로그 확인하기
                //System.out.println(pw+"--"+userPass.getText());
                //Log.v("검색할 태그값", 어떤로그를 찍어볼건지);
                Log.v("btnSubmit", userId.getText()+" ::: "+userPass.getText());

                //토스트메시지 보이기
                //MainActivity = 상단의 메인 메소드와 이름이 같아야 한다.
                Toast.makeText(MainActivity.this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                userPass.setText("");
                userPass.setHint("비밀번호를 입력하세요");
            }
        } else {
            //토스트메시지 보이기
            //MainActivity = 상단의 메인 메소드와 이름이 같아야 한다.
            Toast.makeText(MainActivity.this, "아이디를 확인하세요", Toast.LENGTH_SHORT).show();
            //로그확인
            //System.out.println(id+"--"+userId.getText());
            Log.v("btnSubmit", userId.getText()+" ::: "+userPass.getText());
            userId.setText("");
            userId.setHint("아이디를 입력하세요");
        }

    }
}
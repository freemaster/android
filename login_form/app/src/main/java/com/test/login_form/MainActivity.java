package com.test.login_form;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ConstraintLayout layout;
    private Button btnSubmit;
    private TextView textView;
    private EditText userid;
    private EditText userpass;
    private Button btnBackground;
    //요청코드값으로 어디를 갔다왓는지 나타내기 위해서
    private int requestCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (ConstraintLayout)findViewById(R.id.layout);
        textView = (TextView)findViewById(R.id.textView);
        userid = (EditText)findViewById(R.id.userid);
        userpass = (EditText)findViewById(R.id.userpass);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnBackground = (Button)findViewById(R.id.btnBackground);

        //로그인버튼
        btnSubmit.setOnClickListener(this);
        //백그라운드 불러오기
        btnBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit) {
            String id = userid.getText().toString();
            String pass = userpass.getText().toString();

            if(id.equals("test") && pass.equals("1234")) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("userid", id);
                intent.putExtra("userpass", pass);
                startActivity(intent);
            }

            Log.v("로그인 ::", String.valueOf(userid.getText())+" ::: "+String.valueOf(userpass.getText()));
        }

        if(v.getId() == R.id.btnBackground) {
            Intent intent = new Intent(MainActivity.this, ListActivity2.class);

            //값을 보내고 다시 받아 올때 사용하는 메소드
            startActivityForResult(intent, requestCode);
        }
    }

    //갔다가 왔을 때 실행되는 메소드 onActivityResult
    //onActivityResult Override
    // alt + insert  -> overriding 방법
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //1. requestCode => 요청코드값 ( 어떤 버튼을 눌러서 갔다온건지 구분)
        //2. resultCode => 결과코드값 ( 결과가 잘 넘어오는지 )
        //3. data => 넘어온 결과값 ( 생략가능 )

        //변수면이 같을 경우 나와 가져오값을 분간하기 위해 나의 값은 this를 붙여준다.
        if(requestCode == this.requestCode) { //color 버튼을 클릭하고 온건지 여부
            if (resultCode == RESULT_OK) { //결과가 적용됐는지(결과가 정상인지)
                //intent 값 가져오기
                String color = data.getStringExtra("color");

                //배경색 바꾸기
                layout.setBackgroundColor(Color.parseColor(color));
            }
        }
    }
}
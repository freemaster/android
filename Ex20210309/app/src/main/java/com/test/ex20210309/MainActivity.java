package com.test.ex20210309;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //부모클래스의 메소드 재정의(@Override)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //버튼을 누렀을 때 실행될 메소드 정의
    //매개변수로 View 가 필요함
    public void btnClick(View v) {
        //2. TextView의 글사 (text속성) 변경
        //2-1. 속성을 바꾸고 싶은 textview를 찾습니다. => id
        TextView tv_text = findViewById(R.id.tv_text);

        //2-2. 속성을 바꿈
        tv_text.setText("잘가라~~~~~");
        //tv_text.setTextColor();
    }

    public void btnClick2(View v) {
        TextView tv_text = findViewById(R.id.tv_text);
        TextView tvText = findViewById(R.id.inputText);

        //getText() 는 editable로 담겨있어서 바로 변수로 저장하면 오류가 발생
        //.toString()을 통해서 변수로 저장할 수 있게 된다.
        String str = tv_text.getText().toString();

        tvText.setText(tv_text.getText());
        tvText.setTextColor(Color.rgb(255, 0, 0));
    }
}
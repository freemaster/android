package com.test.ex20210312_event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//인터페이스를 상속받아서 사용해야 하므로 인터페이스를 구현해줘야 오류가 발생하지 않는다.
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    //Alt+Insert를 통해서 제너레이터를 불러와서 implements method를 만들어 준다.
    @Override
    public void onClick(View v) {
        Toast.makeText(this, "버튼을 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
    }
}
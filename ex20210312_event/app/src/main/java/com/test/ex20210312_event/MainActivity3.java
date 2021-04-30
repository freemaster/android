package com.test.ex20210312_event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {
    Button btn_ani1, btn_ani2, btn_ani3, btn_ani4, btn_ani5;

    Button[] btns = new Button[5];
    int[] ids = {R.id.btn_ani1, R.id.btn_ani2, R.id.btn_ani3, R.id.btn_ani4, R.id.btn_ani5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        for(int i=0; i<btns.length; i++) {
            btns[i] = (Button)findViewById(ids[i]);
            btns[i].setOnClickListener(this);
        }
        /*
        이구문을 반복문으로 변경해서 짧은 코드로 생성
        btn_ani1=(Button)findViewById(R.id.btn_ani1);
        btn_ani2=(Button)findViewById(R.id.btn_ani2);
        btn_ani3=(Button)findViewById(R.id.btn_ani3);
        btn_ani4=(Button)findViewById(R.id.btn_ani4);
        btn_ani5=(Button)findViewById(R.id.btn_ani5);

        btn_ani1.setOnClickListener(this);
        btn_ani2.setOnClickListener(this);
        btn_ani3.setOnClickListener(this);
        btn_ani4.setOnClickListener(this);
        btn_ani5.setOnClickListener(this);
        */

        /*
        이런식으로 구현을 하게 되면 코드도 길어지고 많이 복잡해 진다.
        btn_ani1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "어흥!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_ani2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "야옹!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_ani3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "멍멍!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_ani4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "꿀꿀!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_ani5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity3.this, "꽥꽥!", Toast.LENGTH_SHORT).show();
            }
        });
        */

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_ani1) {
            Toast.makeText(MainActivity3.this, "어흥!", Toast.LENGTH_SHORT).show();
        } else if(v.getId()==R.id.btn_ani2) {
            Toast.makeText(MainActivity3.this, "야옹!", Toast.LENGTH_SHORT).show();
        } else if(v.getId()==R.id.btn_ani3){
            Toast.makeText(MainActivity3.this, "멍멍!", Toast.LENGTH_SHORT).show();
        } else if(v.getId()==R.id.btn_ani4){
            Toast.makeText(MainActivity3.this, "꿀꿀!", Toast.LENGTH_SHORT).show();
        } else if(v.getId()==R.id.btn_ani5){
            Toast.makeText(MainActivity3.this, "꽥꽥!", Toast.LENGTH_SHORT).show();
        }

    }
}
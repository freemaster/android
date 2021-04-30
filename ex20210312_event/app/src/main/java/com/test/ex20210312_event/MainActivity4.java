package com.test.ex20210312_event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity4 extends AppCompatActivity implements View.OnClickListener{
    private EditText edt_num1;
    private EditText edt_num2;
    private EditText edt_opr;
    private TextView tv_result;
    private Button btn_opr;
    private int num1, num2, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        edt_num1 = (EditText)findViewById(R.id.edt_num1);
        edt_num2 = (EditText)findViewById(R.id.edt_num2);
        edt_opr = (EditText)findViewById(R.id.edt_opr);
        tv_result = (TextView)findViewById(R.id.tv_result);
        btn_opr = (Button)findViewById(R.id.btn_opr);
        btn_opr.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        num1 = Integer.parseInt(edt_num1.getText().toString());
        num2 = Integer.parseInt(edt_num2.getText().toString());
        result = 0;
        if(edt_opr.getText().toString().equals("-")) {
            if(num2>num1) {
                result = num2-num1;
            } else {
                result = num1-num2;
            }
        } else if(edt_opr.getText().toString().equals("+")) {
            result = num1+num2;
        } else if(edt_opr.getText().toString().equals("*")) {
            result = num1*num2;
        } else if(edt_opr.getText().toString().equals("/")) {
            result = num1/num2;
        } else {
            Toast.makeText(MainActivity4.this, "잘못입력하셨습니다.", Toast.LENGTH_SHORT).show();
        }
        Log.v("num1 :: ", String.valueOf(num1));
        Log.v("num2 :: ", String.valueOf(num2));
        Log.v("result :: ", String.valueOf(result));
        edt_num1.setText("");
        edt_num2.setText("");
        edt_opr.setText("");
        //문자열로 변경해서 보여주기 위함 String.valueOf(result 숫자,논리등등등)
        //또는 result+" " 과 같이 빈문자
        tv_result.setText(String.valueOf(result));
    }

        /*
    imploments View.OnclickListener을 불러오지 않고 익명객체 생성를 생성해서 사용하는 경우
    @Override
    btn_opr.setOnClickListener(view.onClickListener(){
        num1 = Integer.parseInt(edt_num1.getText().toString());
        num2 = Integer.parseInt(edt_num2.getText().toString());
        result = 0;
        if(edt_opr.getText().toString().equals("-")) {
            if(num2>num1) {
                result = num2-num1;
            } else {
                result = num1-num2;
            }
        } else if(edt_opr.getText().toString().equals("+")) {
            result = num1+num2;
        } else if(edt_opr.getText().toString().equals("*")) {
            result = num1*num2;
        } else if(edt_opr.getText().toString().equals("/")) {
            result = num1/num2;
        } else {
            Toast.makeText(MainActivity4.this, "잘못입력하셨습니다.", Toast.LENGTH_SHORT).show();
        }
        Log.v("num1 :: ", String.valueOf(num1));
        Log.v("num2 :: ", String.valueOf(num2));
        Log.v("result :: ", String.valueOf(result));
        edt_num1.setText("");
        edt_num2.setText("");
        edt_opr.setText("");
        tv_result.setText(String.valueOf(result));
    });
    */
}
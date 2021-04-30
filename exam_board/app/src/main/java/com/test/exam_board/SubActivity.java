package com.test.exam_board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {
    private EditText userid2;
    private EditText userpass2;
    private Button loginBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        userid2 = (EditText)findViewById(R.id.userid2);
        userpass2 = (EditText)findViewById(R.id.userpass2);
        loginBtn2 = (Button)findViewById(R.id.loginBtn2);

        loginBtn2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(userid2.getText().toString().equals("admin") && userpass2.getText().toString().equals("1234")) {
                    Intent intent = new Intent();
                    intent.putExtra("id", userid2.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
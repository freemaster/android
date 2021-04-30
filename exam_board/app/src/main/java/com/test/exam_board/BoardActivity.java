package com.test.exam_board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BoardActivity extends AppCompatActivity {
    private Button inputBtn;
    private EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        inputBtn = (Button)findViewById(R.id.inputBtn);
        inputText = (EditText)findViewById(R.id.inputText);

        inputBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("content", inputText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
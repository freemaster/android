package com.test.exam_board;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView loginText;
    private Button writeBtn;
    private Button loginBtn;
    private Button logoutBtn;
    private ListView boardList;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    final int LOGIN = 1;
    final int WRITE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginText = (TextView)findViewById(R.id.loginText);
        writeBtn = (Button)findViewById(R.id.writeBtn);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        boardList = (ListView)findViewById(R.id.boardList);

        writeBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

        list.add("승원");
        list.add("진호");
        list.add("양주");
        list.add("명훈");
        list.add("민관");

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.boardlist, list);
        boardList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.writeBtn) {
            Intent intent = new Intent(MainActivity.this, BoardActivity.class);
            startActivityForResult(intent, WRITE);
        }

        if(v.getId() == R.id.loginBtn) {
            Intent intent = new Intent(MainActivity.this, SubActivity.class);
            startActivityForResult(intent, LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN) {
            if(resultCode == RESULT_OK) {
                loginText.setText(data.getStringExtra("id")+"님 환영합니다.");

                loginBtn.setVisibility(View.INVISIBLE);
                logoutBtn.setVisibility(View.VISIBLE);
                writeBtn.setEnabled(true);

                Log.v(" data ::: ", String.valueOf(data.getStringExtra("id"))+"---"+LOGIN+"---"+RESULT_OK);
            }
        }

        if(requestCode == WRITE) {
            if(resultCode == RESULT_OK) {
                list.add(data.getStringExtra("content"));
                adapter.notifyDataSetChanged();

                Log.v(" data ::: ", String.valueOf(data.getStringExtra("content"))+"---"+WRITE+"---"+RESULT_OK);
            }
        }
    }

}
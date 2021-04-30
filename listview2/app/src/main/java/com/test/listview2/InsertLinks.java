package com.test.listview2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InsertLinks extends AppCompatActivity {
    private Button insertBtn2;
    private EditText tv_name2;
    private EditText tv_link2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_links);

        insertBtn2 = (Button)findViewById(R.id.insertBtn2);
        tv_name2 = (EditText)findViewById(R.id.tv_name2);
        tv_link2 = (EditText)findViewById(R.id.tv_link2);

        insertBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tv_name2.getText().toString();
                String link = tv_link2.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("tv_name", name);
                intent.putExtra("tv_link", link);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
package com.newkoad.deliver.add;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.newkoad.deliver.R;

public class BoardActivity extends Addition {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
    }
}

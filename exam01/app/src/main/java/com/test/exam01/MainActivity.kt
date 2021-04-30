package com.test.exam01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

public class MainActivity extend AppCompatActivity implements View.OnClickListner{
    Button btn_shake;
    ImageView img1, img2;
    TextView tv_score;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
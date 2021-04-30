package com.test.ex20210312_event;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity5 extends AppCompatActivity {

    private ImageView img;
    private Button btn_shake;
    private Random random;
    private int[] img_res = {R.drawable.a1, R.drawable.a2, R.drawable.a3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        img = (ImageView)findViewById(R.id.img);
        btn_shake = (Button)findViewById(R.id.btn_shake);
        random = new Random();

        btn_shake.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int num = random.nextInt(img_res.length);
                /*
                if(num==0) {
                    img.setImageResource(R.drawable.a1);
                } else if(num==1) {
                    img.setImageResource(R.drawable.a2);
                } else if(num==2) {
                    img.setImageResource(R.drawable.a3);
                }
                 */
                img.setImageResource(img_res[num]);
            }
        });
    }
}
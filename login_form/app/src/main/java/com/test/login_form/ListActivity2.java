package com.test.login_form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity2 extends AppCompatActivity {
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);

        lv = findViewById(R.id.listView);

        //리스트뷰의 항목이 선택된 걸 감지해보자!
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1. parent => ListView 자체를 의미
                //2. view =>  지금 선택한 view(이벤트가 발생한 해당 뷰)
                //3. position => 이벤트가 발생한 뷰의 index
                //4. id => 이벤트가 발생한 뷰의 고유 id값 = position
                String choice = "";
                if(position == 0) {
                    choice = "#800000";
                } else if(position == 1) {
                    choice = "#006400";
                } else if(position == 2) {
                    choice = "#191970";
                }

                Toast.makeText(getApplicationContext(), choice, Toast.LENGTH_SHORT).show();

                //어떤 색을 선택했는지 저장할 intent
                //intent의 행동이 정해진게 아니고 값만 전달하는 것이므로 빈값 인텐트를 생성
                Intent intent = new Intent();
                intent.putExtra("color", choice);

                //result결과 전달하는 상수 RESULT_OK -> 결과값이 성공
                setResult(RESULT_OK, intent);
                //종료료
                finish();
           }
        });
    }
}
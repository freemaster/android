package com.test.listview2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ContentListVO> contentList = new ArrayList<>();
    private ListView lv;
    private Button insertBtn;

    final int WRITE=1;

    //DBManager.java 정의 해주기
    DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터 베이스 파일생성, 테이블 생성, connection 알아서 다해줌
        manager = new DBManager(getApplicationContext());

        lv = (ListView)findViewById(R.id.listView);
        insertBtn = (Button)findViewById(R.id.insertBtn);

        ContentListVO vo = new ContentListVO();

        String[] names = {"네이버", "다음", "구글", "스마트인재개발원"};
        String[] links = {"http://naver.com", "http://daum.net", "http://google.com", "http://smhrd.or.kr"};
        for(int i=0; i<names.length; i++) {
            vo = new ContentListVO(names[i], links[i]);
            contentList.add(vo);
            Log.v(" vo ", vo.toString());
        }

        ContantAdapter adapter = new ContantAdapter(getApplicationContext(), R.layout.contentlist, contentList);
        lv.setAdapter(adapter);

        insertBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertLinks.class);
                startActivityForResult(intent, WRITE);
            }
        });

       //Cursor와 JDBC의 ResultSet과 같은 역할!!
        //DATA(행)을 가리키고 있는 화살표~
        //alt+enter를 통해서 DBManager에 getAllData method를 만들어 주자.
        Cursor cursor = manager.getAllData();

        //커서를 아랫칸으로 옮기는데 데이터가 있으면 true, 없으면 false 반환
        //데이터가 존재하는 동안 반복하기 위해 while문을 사용 !!!
        while(cursor.moveToNext()) {
            //해당행의 데이터 가져와서 arrayList에 축척하기!
            String title = cursor.getString(0);
            String address = cursor.getString(1);

            contentList.add(new ContentListVO(title, address));

            adapter.notifyDataSetChanged();
        }

        //리스트뷰 항목을 터치했을 때 해당 항목 지워버리기
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //이전 예제에서는 position (위치)로 해당 데이터를 지웠습니다.!!(ArrayList에서)
                //DB에 index를 부여할 순 있지만 position(매개변수) 과 index(DB)가 맞지 않을 수 있음!
                //결론 title로 지워야 합니다.

                //=> 지금 클릭한 항목의 title을 알아내야 합니다.
                //1. arraylist의 position번째 값을 꺼내는 방법!
                //2. 클릭이 일어난 항목의 textview에서 getText하는 방법

                TextView tv_link = view.findViewById(R.id.tv_name);
                manager.delete((String) tv_link.getText());

                //눈에 보이는 listView에서도 지워야 합니다.
                contentList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == WRITE){
            if(resultCode == RESULT_OK){
                String name = data.getStringExtra("tv_name");
                String link = data.getStringExtra("tv_link");
                contentList.add(new ContentListVO(name, link));

                //manager 클래스의 insert 메소드 호출~~
                //insert를 만들고 insert에 커서를 놓고 alt+enter를 누르면
                //에러를 해결해 나가는 방식으로 개발하는 것(리펙토링)
                // 리펙토링을 통해서 DBManager에 insert method를 만들어준다.
                manager.insert(new ContentListVO(name, link));
                //adapter.notifyDataSetChanged();

                Log.v(" NAME ::: ", String.valueOf(data.getStringExtra("tv_name")));
                Log.v(" LINK ::: ",String.valueOf(data.getStringExtra("tv_link")));
            }
        }
    }
}
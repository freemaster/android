package com.test.exam_listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ContantVO> contacts = new ArrayList<>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.listView);

        //arraylist에 연락처 정보 5개 추가
        ContantVO vo = new ContantVO();

        int[] imgs = {R.drawable.a11, R.drawable.a22, R.drawable.a33, R.drawable.a44, R.drawable.a53};
        String[] names = {"머냐고", "누구냐고", "어쩌라고", "하하하", "호호호"};
        String[] phones = {"010-111-2222", "010-222-3333", "010-333-4444", "010-444-5255", "010-555-6666"};
        for(int i=0; i<imgs.length; i++) {
            vo = new ContantVO(imgs[i], names[i], phones[i]);
            contacts.add(vo);
            Log.v(" vo ", vo.toString());
        }

        //ArrayAdapter는 simplelist.xml 에 textView 단독으로 있을때만 사용가능!
        //그런데 지금 우리는 contactlist.xml 에 custom 을 해두었기 때문에 ArrayAdapter 사용불가능!
        //해결방법!!! Adapter를 Custom하자!(ContantAdapter.java)
        ContantAdapter adapter = new ContantAdapter(getApplication(), R.layout.contentlist, contacts);
        lv.setAdapter(adapter);
    }
}
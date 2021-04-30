package com.test.listview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //리스뷰 안에 들어갈 데이터를 생성!! ==> 추가,삭제가 자유로운 ArrayList를 사용하여 생성

    private ListView lv;
    private List<String> list= new ArrayList<String>();

    private EditText editText;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        addBtn = (Button)findViewById(R.id.addBtn);

        lv = (ListView)findViewById(R.id.lv);

        list.add("승원");
        list.add("진호");
        list.add("양주");
        list.add("명훈");
        list.add("민관");

        //Adapter 생성
        //Adapter의 역할
        //simplelist.xml(항목 디자인, 템플릿)과 ArrayList(데이터)를 결합해서
        //listView에 차곡차곡 쌓아주는 일을 함!

        //매개변수
        //첫번째 : 현재페이지 정보 getApplicationContext()
        //두번째 : 항목의 템플릿 R.layout.simplelist
        //세번째 : 내가 넣고 싶은 데이터 list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simplelist, list);

        lv.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //추가버튼 시 데이터 추가
                String str = editText.getText().toString();

                //값을 list에 추가
                list.add(str);
                //적어놓은 글자 지우기
                editText.setText("");

                //adapter 새로고침~ 11버전부터는 자동으로 새로고침???
                adapter.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //list.remove(position);
                //adapter.notifyDataSetChanged();
                //Log.v(" position :: ", String.valueOf(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("삭제하기");
                builder.setMessage("삭제하시겠습니까?");
                //첫번째 : 제목(?)
                //두번째 : 어떤일을 할 것인지
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                //첫번째 : 제목(?)
                //두번째 : 위에서 yes처리를 하므로 아래에서 아무행위가 필요없어서 null을 넣어준다.
                builder.setNegativeButton("Cancle", null);
                builder.show();
            }
        });
    }
}
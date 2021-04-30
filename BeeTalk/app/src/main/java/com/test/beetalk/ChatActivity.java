package com.test.beetalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    //임시 채팅데이터 생성!
    public ArrayList<ChatVO> data = new ArrayList<>();
    public String login_id = null;
    public TextView tv_id;
    public ListView lv;
    public Button btn_send;
    public EditText edt_input;

    //파이어베이스 사용을 위해 객체생성
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://beetalk-b4fb2-default-rtdb.firebaseio.com/");
    //파이어베이스의 주소가져오기
    DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        login_id = getIntent().getStringExtra("id");
        tv_id = (TextView)findViewById(R.id.tv_id);
        lv = (ListView)findViewById(R.id.listview);
        btn_send = (Button)findViewById(R.id.btn_send);
        edt_input = (EditText)findViewById(R.id.edt_input);

        tv_id.setText(login_id);

        /*
        데이터를 자동으로 불러오므로 중지
        data.add(new ChatVO(R.drawable.c1, "dPsk", "안녕하세요!111", "09:58"));
        data.add(new ChatVO(R.drawable.c2, "dydanr", "안녕하세요!222", "09:58"));
        data.add(new ChatVO(R.drawable.c1, "dPsk", "안녕하세요!333", "09:58"));
        data.add(new ChatVO(R.drawable.c3, "tkdcjf", "안녕하세요!444", "09:58"));
        data.add(new ChatVO(R.drawable.c4, "tmddnjs", "안녕하세요!555", "09:58"));
        data.add(new ChatVO(R.drawable.c1, "dPsk", "안녕하세요!666", "09:58"));
        */

        ChatAdatper adapter = new ChatAdatper(getApplicationContext(), R.layout.chatlist, data, login_id);
        lv.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //채팅데이터(ChatVO) 생성
                ChatVO vo = new ChatVO(R.drawable.c1, login_id, edt_input.getText().toString(), "12:00");
                //push를 넣는 이유는 새로운 키가 생기게 하기 위함(즉 무작위 경로가 생김)
                myRef.push().setValue(vo);
            }
        });

        //firebase database에서 데이터 불러오기
        //주소에 갑이 변화할 때를 감지하는 Listener!
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //하위경로에 데이터가 추가 되었을 때~
                //추가된 데이터를 datasnapshot 형태로 가져온다.
                ChatVO vo = dataSnapshot.getValue(ChatVO.class);
                //chatvo 형태로 자장되어 있는 값을 가져오겠다.!!!!
                //★★★★★★ ChatVO 클래스에는 반드시 !!! default 생성자를 만들어 줘야 함!!!
                data.add(vo); //데이터 넣어주고 
                adapter.notifyDataSetChanged(); //데이터 새로고침
                edt_input.setText("");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //값이 바뀌었을 때
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //값이 사라졌을 때
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //값이 이동했을 때
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //에거가 발생해서 실패 했을 때
            }
        });
    }
}
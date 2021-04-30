package com.test.beetalk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class joinActivity extends AppCompatActivity {
    //입력한 id와 pw정보를 서버로 전송
    //Volly Library 사용!
    //1. Volly Library 임포트 하기!
    //2. RequestQueue, StringRequest
    private RequestQueue requestQueue; //서버와 통신할 통로!
    private StringRequest stringRequest; //내가 전송할 데이터!

    private EditText edt_id, edt_pw;
    private Button btn_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        edt_id = (EditText)findViewById(R.id.edt_id);
        edt_pw = (EditText)findViewById(R.id.edt_pw);

        btn_join = (Button)findViewById(R.id.btn_join);

        //3. requestQueue 생성!
        String url = "http://211.48.213.190:8081/MemberServer/JoinServlet";

        requestQueue = Volley.newRequestQueue(getApplicationContext()); //통로준비

        //4. StringRequest 생성!
        //1) 전송방식(타입)
        //2) url(서버의 주소)
        //3) 응답리스너
        //4) 에러리스너

        //5. 전송할 데이터 미리 정의
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //서버에서 돌려준 응답을 처리!
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        //}) 여기까지 생성자(객체생성한것임)  { 여기부터 다시 StringRequest 객체범위~~( getParams - 오버라이딩 )
        }) {
            @Nullable
            @Override
            //생성자 protected
            //리컨값 map<String, String> key value로 이루어진 맵타입을 리턴하겠다.
            //메소드이름 getParams() -> 매개변수 없은 메소드:: ()에 매개변수가 들어값
            //예외처리구문 throws
            protected Map<String, String> getParams() throws AuthFailureError {
                //전송할 데이터 Key, Value로 셋팅하기!
                Map<String, String> temp = new HashMap<>();
                temp.put("id", edt_id.getText().toString());
                temp.put("pw", edt_pw.getText().toString());

                return temp;
            }
        };

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6. 서버로 stringRequest 쏘기!
                requestQueue.add(stringRequest);
            }
        });

        //7. 인너넷 권한주기!
        //manifests > AndroidMainfest.xml 파일 수정

    }
}
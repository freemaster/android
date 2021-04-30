package com.test.ex0406;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Fragment_4 extends Fragment {
    //fragment4에서 배운것
    //1) fragment 상황에서 findviewbyid하기, toast띄우기
    //2) fragment1으로 데이터 넘기기
    //2-1)app 데이터(캐시)로 저장하기 => SharedPreference
    //    간단한 데이터를 저장할 때, 특히 로그인정보, 환경설정
    //    APP가 첫 실행인지 감지할 때

    private Button btnSet;
    private EditText edt_address;

    SharedPreferences spf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_4, container, false);

        btnSet = (Button)view.findViewById(R.id.btnSet);
        edt_address = (EditText)view.findViewById(R.id.edt_address);

        //MODE_PRIVATE => mySPF라는 이름의 프리퍼런스가 없으면 새로 생성하고
        //                있으면 있는걸 가져온다.
        spf = getActivity().getSharedPreferences("mySPF", Context.MODE_PRIVATE);

        //값 반영할 수 있는 객체
        SharedPreferences.Editor editor = spf.edit();

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast 띄울 때! getApplicationContent() => Fragment에서는 지원하지 않음.
                //앞에 getActivity(). 를 붙여주면 현재 Fragment가 포함된 Activity정보를 가지고 올 수 있음.

                //현재 url에 https:// 로 시작하지 않는다면 http:// 붙여주기
                String url = edt_address.getText().toString();
                if(!url.startsWith("https://")) {
                    url = "https://" + url;
                }
                Toast.makeText(getActivity().getApplicationContext(), url, Toast.LENGTH_SHORT).show();

                //spf에 값 저장하기
                //해당값을 app을 껏다 켜도 유지됨
                //"address" ==> 키값
                //url ==> value값
                editor.putString("address", url);
                editor.commit();//반드시 commit를 해줘야 반영이 된다.
            }
        });
        return view;
    }
}
package com.test.ex0406;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Fragment_1 extends Fragment {
    //Fragment1 에서 배운것
    //1)WebView 쓰는법
    //2) 저장된 url 정보 가져오는 법
    private WebView wv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fragment.xml 파일을 view로 inflate(id->view)로 반환
        //inflate ===> id를 view로 반환하는 것

        View view = inflater.inflate(R.layout.fragment_1, container, false);
        wv = view.findViewById(R.id.webview);

        //String url = "https://funyphp.com";
        //key:"address" => 데이터에 키값
        //defValue:null ==> 해당키에 상응하는 값이 없을 때 반환될 값;
        String url = getActivity().getSharedPreferences("mySPF", Context.MODE_PRIVATE).getString("address", null);


        //1-1) webview 환경 설정해 주기
        //webSettings객체에 설정
        WebSettings settings = wv.getSettings();
        //webview에서 자바스크립트 지원하지 않으므로 자바스크립트 지원하게 설정
        settings.setJavaScriptEnabled(true);

        //1-2) webclient 지정하기
        wv.setWebViewClient(new WebViewClient());

        //1-3) 띄워줄 url지정하기
        wv.loadUrl(url);

        //1-4) 인터넷 권한 부여해주기
        //manifests > AndroidManifest.xml 파엘에서 퍼미션을 추가한다.

        return view;
    }
}
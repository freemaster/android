package com.newkoad.deliver;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newkoad.deliver.R;
import com.newkoad.deliver.add.Addition;

public class WebBoardActivity extends Addition {

    private static final String TAG ="WebBoardActivity" ;

    private WebView mWebView;
    private WebSettings mWebSettings;
    private String userID;
    private String userPW;
    private String url;

    private String urlNotice = "";
    private String urlQna = "";


    @Override public void onBackPressed() {
        super.onBackPressed();
        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_board);

        mainApp = (MainApp)getApplicationContext();
        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("공 지 사 항");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }



        userID = mainApp.getUserID();
        userPW = mainApp.getUserPW();


        mWebView = (WebView) findViewById(R.id.webview);


        mWebView.setWebViewClient(new WebViewClient());

        mWebSettings = mWebView.getSettings();

        mWebSettings.setJavaScriptEnabled(true);

        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setLoadWithOverviewMode(true);

        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setDomStorageEnabled(true);

        String agent = mWebSettings.getUserAgentString();
        mWebSettings.setUserAgentString(agent +" ddbox");


        url = "https://dedibox.biz/login?userID="+ userID +"&userPW=" + userPW +"&url=" + "board/qna" ;


        mWebView.loadUrl(url);


        Button noticeBtn  = findViewById(R.id.notice_btn);
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://dedibox.biz/login?userID="+ userID +"&userPW=" + userPW +"&url=" + "board/notice" ;

                mWebView.loadUrl(url);

            }
        });

        Button qnaBtn = findViewById(R.id.qna_btn);
        qnaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "https://dedibox.biz/login?userID="+ userID +"&userPW=" + userPW +"&url=" + "board/qna" ;

                mWebView.loadUrl(url);


            }
        });

    }

    public void login(){

        url = "https://dedibox.biz/login?userID="+ userID +"&userPW=" + userPW +"&url=" + "board/notice" ;


        mWebView.loadUrl(url);
    }

    public void load(String url){

        mWebView.loadUrl(url);
    }




}

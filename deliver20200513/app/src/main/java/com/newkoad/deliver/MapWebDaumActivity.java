package com.newkoad.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newkoad.deliver.add.Addition;
import com.newkoad.deliver.common.Item;
import com.newkoad.deliver.common.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class MapWebDaumActivity extends Addition {

    private static final String TAG ="MapWebDaumActivity" ;


    private ArrayList<Item> lstItem = null;
    Order order;

    private Map<String, String> routes = null;


    WebView webView;
    double lat, lon;
    double cx, cy;
    double sPointx, sPointy;
    double ePointx, ePointy;
    boolean multi = false;
    JSONArray jsonArray ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_web_daum);

        mainApp = (MainApp)getApplicationContext();
        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("지도 보기");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }


        Intent intent = getIntent();
        lat = mainApp.latitude;
        lon = mainApp.longitude;
        String positions = "cx=" + lat + "&cy=" + lon ;


        lstItem =  mainApp.allocateList;



        if( intent.getStringExtra("view") != null) {

            multi = true;
            jsonArray = new JSONArray();

            for(int i = 0; i< lstItem.size(); i++){

                order = (Order) lstItem.get(i);


                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cx", lat);
                    jsonObject.put("cy", lon);


                    String spoints = order.getsPoints();
                    String[] spArray = spoints.split(",");
                    if (spArray.length == 2) {
                        sPointx = Double.parseDouble(spArray[0]);
                        sPointy = Double.parseDouble(spArray[1]);
                    }
                    jsonObject.put("sx", sPointx);
                    jsonObject.put("sy", sPointy);


                    String epoints = order.getePoints();
                    String[] epArray = epoints.split(",");
                    if (epArray.length == 2) {
                        ePointx = Double.parseDouble(epArray[0]);
                        ePointy = Double.parseDouble(epArray[1]);
                    }
                    jsonObject.put("ex", ePointx);
                    jsonObject.put("ey", ePointy);
                    jsonArray.put(jsonObject);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }

        if( intent.getParcelableExtra("order") != null) {

            order = (Order) intent.getParcelableExtra("order");


            String spoints = order.sPoints;
            String[] spArray = spoints.split(",");
            if (spArray.length == 2) {
                sPointx = Double.parseDouble(spArray[0]);
                sPointy = Double.parseDouble(spArray[1]);
                positions = positions + "&sx=" + sPointx + "&sy=" + sPointy;
            }


            String epoints = order.ePoints;
            String[] epArray = epoints.split(",");
            if (epArray.length == 2) {
                ePointx = Double.parseDouble(epArray[0]);
                ePointy = Double.parseDouble(epArray[1]);
                positions = positions + "&ex=" + ePointx + "&ey=" + ePointy;

            }

        }


        webView = (WebView) findViewById(R.id.webview_map);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        webView.clearCache(true);
        webView.clearHistory();


        if (multi) {

            webView.loadUrl(mainApp.getRootURL() + "/_map/map_daum_multi.php?routes=" + jsonArray.toString());
        } else {
            webView.loadUrl(mainApp.getRootURL() + "/_map/map_daum_delivery.php?" + positions);
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);





    }






}

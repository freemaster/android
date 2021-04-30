package com.newkoad.deliver;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.provider.Settings.Secure;


public class LoginActivity extends AppCompatActivity {

    private String newToken;
    private String id, pw;

    private String userID = "" , userPW = "";
    private EditText idText, pwText;

    private MainApp mainApp;
    private static final String TAG = "LoginActivity";

    private BackPressCloseHandler backPressCloseHandler;

    private SharedPreferences appData;
    SharedPreferences.Editor editor;
    private boolean saveLoginData, autoLoginData;
    private CheckBox idSaveCkb, onSaveCkb;

    ProgressDialog progressDialog = null;
    private File useFile;
    private File inStorage, outStorage;
    private Button updateBtn;


    @Override
    public void onBackPressed() {

        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainApp = (MainApp)getApplicationContext();
        backPressCloseHandler = new BackPressCloseHandler(this);
        mainApp.loginActivity =  this;







        idText = (EditText) findViewById(R.id.userID);


        pwText = (EditText) findViewById(R.id.userPW);


        idSaveCkb = (CheckBox) findViewById(R.id.idSaveCkb);

        onSaveCkb = (CheckBox) findViewById(R.id.onSaveCkb);

        final Button loginBtn = (Button) findViewById(R.id.login_btn);

        updateBtn = (Button) findViewById(R.id.update_btn);


        appData = getSharedPreferences("appData", MODE_PRIVATE);
        loadAppData();




        try {

            PackageInfo pInfo = mainApp.loginActivity.getPackageManager().getPackageInfo(getPackageName(), 0);

            mainApp.versionName = pInfo.versionName;
            mainApp.versionCode = pInfo.versionCode;



        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        if(saveLoginData){
            idText.setText(id);
            pwText.setText(pw);
            idSaveCkb.setChecked(saveLoginData);

        }

        if(autoLoginData){
            onSaveCkb.setChecked(autoLoginData);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }

        });







        TextView copyright = (TextView) findViewById(R.id.copyright);





        mainApp.setMACAddr( getMACAddress("wlan0"));




        if(autoLoginData){
            if(!idText.getText().toString().equals("")) {
                if(!pwText.getText().toString().equals("")) {
                    login();
                }
            }
        }

    }

    private void login(){



        userID = idText.getText().toString();
        userPW = pwText.getText().toString();

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject json = new JSONObject(response);
                    boolean success = json.getBoolean("success");
                    Log.d("response" , response.toString());
                    if (success) {

                        saveAppData();

                        mainApp.setUserPW(userPW);
                        if( json.getString("att_datetime").equals("null")){

                        }else{
                            mainApp.setAttendTime( json.getString("att_datetime") );
                        }
                        mainApp.setUserPhoto( json.getString("photoUrl"));
                        mainApp.setParentName( json.getString("parentName"));
                        mainApp.setRoute_dis( Integer.parseInt(json.getString("route_dis")));
                        mainApp.setRoute_time( Integer.parseInt(json.getString("route_time")));

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        intent.putExtra("userKey", json.getString("userKey") );

                        intent.putExtra("parentKey", json.getString("parentKey") );

                        intent.putExtra("userID", json.getString("userID") );

                        intent.putExtra("userPW", userPW );

                        intent.putExtra("userName", json.getString("userName"));

                        intent.putExtra("userGroup", json.getString("userGroup") );

                        intent.putExtra("isBranch", json.getString("isBranch"));



                        intent.putExtra("savePointTime", "1");


                        intent.putExtra("tokenp", json.getString("token") );

                        intent.putExtra("attend", json.getString("attend") );
                        intent.putExtra("boxID", json.getString("boxAuthKey") );


                        intent.putExtra("callLimit", json.getString("callLimit") );
                        intent.putExtra("dayLimit", json.getString("dayLimit") );

                        startActivity(intent);

                    }else {


                        String msg = "로그인에 실패하였습니다.";
                        if(json.getString("msg").length() > 4) {
                            msg = json.getString("msg");
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(msg)
                                .setNegativeButton("다시 시도", null)
                                .create()
                                .show();
                    }


                }catch(Exception e){
                    e.printStackTrace();


                }

            }
        };

        LoginRequest loginRequest = new LoginRequest( userID, userPW,  responseListener, errorListener);

        loginRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }



    class LoginRequest extends StringRequest{

        private Map<String, String> parameters;

        public LoginRequest(String userID, String userPW, Response.Listener<String> listener, Response.ErrorListener error ) {
            super(Method.POST,  mainApp.getRootURL() +"/login_app.php", listener, error);





            parameters = new HashMap<>();



            parameters.put("userID", userID);



            parameters.put("userPW", userPW);



           String androidID = Secure.getString( getContentResolver(), Secure.ANDROID_ID);


           mainApp.setAndroidID(androidID);


           parameters.put("aID", mainApp.getAndroidID());




        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }

    }


    private void loadAppData(){
        saveLoginData = appData.getBoolean("SAVE_IDPW_DATA", false);
        id= appData.getString("ID", "");
        pw= appData.getString("PW", "");
        autoLoginData = appData.getBoolean("SAVE_AUTO_LOGIN", false);


    }


    private void saveAppData(){

        editor = appData.edit();
        editor.putBoolean("SAVE_IDPW_DATA", idSaveCkb.isChecked());
        editor.putString("ID", idText.getText().toString().trim());
        editor.putString("PW", pwText.getText().toString().trim());
        editor.putBoolean("SAVE_AUTO_LOGIN", onSaveCkb.isChecked());

        editor.commit();



    }





    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { }
        return "";

    }





    private void clearApplicationCache(java.io.File dir){
        if(dir==null)
            dir = getCacheDir();
        else;
        if(dir==null)
            return;
        else;
        java.io.File[] children = dir.listFiles();
        try{
            for(int i=0;i<children.length;i++)
                if(children[i].isDirectory())
                    clearApplicationCache(children[i]);
                else children[i].delete();
        }
        catch(Exception e){}
    }


    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {

                deleteDir(new File(appDir, s));

            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }



    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;
        private Activity activity;
        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }
        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT); toast.show();
        }
    }












    void openPlayStore(){

        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }



}





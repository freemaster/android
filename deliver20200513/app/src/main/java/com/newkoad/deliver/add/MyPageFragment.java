package com.newkoad.deliver.add;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;
import com.newkoad.deliver.common.LoadImage;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;




public class MyPageFragment extends Fragment {

    private static final String TAG ="MyPageFragment" ;
    private MainApp mainApp ;
    private Context context;
    View v;

    ImageView driverImgview;
    Bitmap driverBitmap, newBitmap;
    Button chooseBtn, uploadBtn, imageDelBtn;
    RequestQueue queue;

    String URL = "";
    final int CODE_GALLERY_REQUEST = 999;
    Handler handler = new Handler();

    String imageUrl;
    TextView nameTv, idTv, phoneTv, emailTv, joinDateTv;

    Button changePwBtn;
    Dialog dialog;
    EditText currentEt, newEt, newConfirmEt;
    TextView refCurrentTv, refNewTv;

    LinearLayout informLay, resultLay;
    TextView resultTv;

    public MyPageFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApp = (MainApp) getActivity().getApplication();

        URL = mainApp.getUserPhoto() ;


        queue= Volley.newRequestQueue(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();


        v = inflater.inflate(R.layout.fragment_my_page,container,false);




        uploadBtn = v.findViewById(R.id.upload_btn);



        driverImgview= (ImageView) v.findViewById(R.id.driver_imgview);
        nameTv =(TextView) v.findViewById(R.id.name_tv);
        idTv =(TextView) v.findViewById(R.id.id_tv);
        phoneTv =(TextView) v.findViewById(R.id.phone_tv);
        emailTv =(TextView) v.findViewById(R.id.email_tv);
        joinDateTv =(TextView) v.findViewById(R.id.join_date_tv);

        changePwBtn = (Button) v.findViewById(R.id.change_pw_btn);
        changePwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getActivity());

                dialog.setTitle(" 변경");
                dialog.setContentView(R.layout.dialog_chage_pw);

                informLay = (LinearLayout) dialog.findViewById(R.id.inform_lay);
                resultLay = (LinearLayout) dialog.findViewById(R.id.result_lay);

                resultLay.setVisibility(View.GONE);
                resultTv = (TextView) dialog.findViewById(R.id.result_tv);


                currentEt = (EditText) dialog.findViewById(R.id.pw_current);
                newEt = (EditText) dialog.findViewById(R.id.pw_new);
                newConfirmEt = (EditText) dialog.findViewById(R.id.pw_new_confirm);

                refCurrentTv = (TextView) dialog.findViewById(R.id.ref_current);
                refNewTv = (TextView) dialog.findViewById(R.id.ref_new);

                Button changeBtn = (Button) dialog.findViewById(R.id.change_btn);
                changeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if( mainApp.getUserPW().equals(currentEt.getText().toString()) ){

                            if(newEt.getText().length() >= 4 ) {
                                if (newEt.getText().toString().equals(newConfirmEt.getText().toString())) {
                                    changePW();
                                } else {
                                    refNewTv.setText(" 새 비밀번호가 서로 같지 않음");
                                }
                            }else{
                                refNewTv.setText(" 새 비밀번호를 4자 이상 사용");
                            }
                        }else{
                            refCurrentTv.setText("현재 비밀번호 오류");
                        }
                    }
                });

                Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                Button resultCloseBtn = (Button) dialog.findViewById(R.id.result_close_btn);
                resultCloseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        mainApp.mainActivity.finish();
                        getActivity().finish();
                    }
                });
                dialog.show();

            }
        });

        getData();
        return v;
    }




    public void changePW(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {

                        }
                    }else {


                        if (jsonObject.getString("result").equals("true")) {


                            informLay.setVisibility(View.GONE);
                            resultLay.setVisibility(View.VISIBLE);

                            resultTv.setText(jsonObject.getString("msg"));
                        } else {
                            informLay.setVisibility(View.GONE);
                            resultLay.setVisibility(View.VISIBLE);

                            resultTv.setText(jsonObject.getString("msg"));

                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();

                    informLay.setVisibility(View.GONE);
                    resultLay.setVisibility(View.VISIBLE);
                    resultTv.setText("변경오류");


                }
            }
        };
        ChangePWRequest changePWRequest = new ChangePWRequest(responseListener);
        changePWRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(changePWRequest);
    }



    class ChangePWRequest extends StringRequest {

        private Map<String, String> parameters;
        public ChangePWRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/my/change_password.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());


            parameters.put("prePW", currentEt.getText().toString() );
            parameters.put("newPW", newEt.getText().toString());


        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }





    public void getData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {

                        }
                    }else {


                        imageUrl = jsonObject.getString("image_url");

                        nameTv.setText(jsonObject.getString("name"));
                        idTv.setText(jsonObject.getString("id"));
                        phoneTv.setText(jsonObject.getString("phone"));
                        emailTv.setText(jsonObject.getString("email"));
                        joinDateTv.setText(jsonObject.getString("join_date"));


                        String imageUrl = mainApp.getRootURL() + "/_images/rider/rider2.jpg";
                        LoadImage loadImage = new LoadImage(imageUrl);
                        Bitmap bitmap = loadImage.getBitmap();
                        driverImgview.setImageBitmap(bitmap);
                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        DataListRequest dataListRequest = new DataListRequest(responseListener);
        dataListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(dataListRequest);
    }


    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/my/mypage.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );


            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("orderKey", mainApp.getUserGroup() );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }






}

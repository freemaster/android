package com.newkoad.deliver;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.R;
import com.newkoad.deliver.add.Addition;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayResultActivity extends Addition {

    private static final String TAG ="PayResultActivity" ;
    private TextView result;
    public String trantype;
    public String resultcode;
    public static ActivityManager activityManager;
    private String resultCode = "";
    private String resultMsg ="";
    private String msg ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("처리 결과");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }

        result = (TextView)findViewById(R.id.pay_result_tv);
        Intent i = getIntent();
        msg += "TRAN_NO = ";
        msg += i.getStringExtra("TRAN_NO");
        msg += "\nAPPCALL_TRAN_NO = ";
        msg += i.getStringExtra("APPCALL_TRAN_NO");
        msg += "\nTRAN_TYPE = ";
        msg += i.getStringExtra("TRAN_TYPE");
        msg += "\nCARD_NUM = ";
        msg += i.getStringExtra("CARD_NUM");
        msg += "\nCARD_NAME = ";
        msg += i.getStringExtra("CARD_NAME");
        msg += "\nTOTAL_AMOUNT = ";
        msg += i.getStringExtra("TOTAL_AMOUNT");
        msg += "\nTAX = ";
        msg += i.getStringExtra("TAX");
        msg += "\nTIP = ";
        msg += i.getStringExtra("TIP");
        msg += "\nINSTALLMENT = ";
        msg += i.getStringExtra("INSTALLMENT");
        msg += "\nRESULT_CODE = ";
        msg += i.getStringExtra("RESULT_CODE");
        msg += "\nRESULT_MSG = ";
        msg += i.getStringExtra("RESULT_MSG");
        msg += "\nAPPROVAL_NUM = ";
        msg += i.getStringExtra("APPROVAL_NUM");
        msg += "\nORG_APPROVAL_NUM = ";
        msg += i.getStringExtra("ORG_APPROVAL_NUM");
        msg += "\nAPPROVAL_DATE =";
        msg += i.getStringExtra("APPROVAL_DATE");
        msg += "\nACQUIRER_CODE =";
        msg += i.getStringExtra("ACQUIRER_CODE");
        msg += "\nACQUIRER_NAME =";
        msg += i.getStringExtra("ACQUIRER_NAME");
        msg += "\nORDER_NUM =";
        msg += i.getStringExtra("ORDER_NUM");
        msg += "\nSHOP_TID =";
        msg += i.getStringExtra("SHOP_TID");
        msg += "\nSHOP_BIZ_NUM =";
        msg += i.getStringExtra("SHOP_BIZ_NUM");
        msg += "\nSHOP_NAME =";
        msg += i.getStringExtra("SHOP_NAME");
        msg += "\nSHOP_OWNER =";
        msg += i.getStringExtra("SHOP_OWNER");
        msg += "\nSHOP_ADDRESS =";
        msg += i.getStringExtra("SHOP_ADDRESS");
        msg += "\nSHOP_TEL =";
        msg += i.getStringExtra("SHOP_TEL");
        msg += "\nMERCHANT_NUM =";
        msg += i.getStringExtra("MERCHANT_NUM");
        msg += "\nCUSTOMER_CODE =";
        msg += i.getStringExtra("CUSTOMER_CODE");
        msg += "\nADD_FIELD =";
        msg += i.getStringExtra("ADD_FIELD");
        msg += "\nTRAN_SERIALNO =";
        msg += i.getStringExtra("TRAN_SERIALNO");
        msg += "\nRESPONSE_TYPE =";
        msg += i.getStringExtra("RESPONSE_TYPE");
        result.setText(msg);

        resultCode =i.getStringExtra("RESULT_CODE");
        resultMsg = i.getStringExtra("RESULT_MSG");

    }



    public void onBackPressed() {

        finish();
    }







    private void payResult(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("result").equals("true")){
                        finish();

                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        DataListRequest dataListRequest = new DataListRequest(responseListener);
        dataListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(dataListRequest);
    }


    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/2allocate/pay_result_kicc.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("type", "A");
            parameters.put("result_code", resultCode);
            parameters.put("reuslt_msg", resultMsg);
            parameters.put("total_msg", msg);


        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }



}

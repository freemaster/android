package com.newkoad.deliver.add;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;



public class MoneyGetActivity extends Addition {


    private static final String TAG ="MoneyGetActivity" ;


    TextView depositTv;
    TextView bankTv, accountTv, nameTv;
    EditText amountEt;
    TextView amountRefTv;
    TextView refundCanTv, taxRateTv, taxTv, refundAmountTv;


    private float tax ;
    private int hope;
    private int refund;
    private String hopeStr ;


    private Dialog myDialog ;
    private boolean  refund_can ;
    private boolean  refund_ok;
    private Button psOkBtn, psCancelBtn, goDepositBtn;

    private DecimalFormat formatter = new DecimalFormat("###,###");
    private String result="";

    EditText passwordEt ;
    TextView resultTv, feeTv ;


    private String refundTax = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_get);

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(" 환 급 ");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }

        depositTv = (TextView) findViewById(R.id.deposit_tv);
        bankTv = (TextView) findViewById(R.id.bank_tv);
        accountTv = (TextView) findViewById(R.id.account_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);

        amountRefTv = (TextView) findViewById(R.id.amount_ref_tv) ;
        amountEt = (EditText) findViewById(R.id.amount_et);  // 신청금액


        /*
        amountEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                hopeStr = amountEt.getText().toString();
                if(!hopeStr.equals("")) {

                    hope = Integer.parseInt(hopeStr.replace(",",""));

                    refund = (int) ( hope - (hope * (tax / 100)) );

                    taxTv.setText( formatter.format( hope * (tax / 100) ) );
                    refundAmountTv.setText( formatter.format(refund)  );



                }

                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                    result = formatter.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    amountEt.setText(result);
                    amountEt.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        */

        refundCanTv = (TextView) findViewById(R.id.refund_can_tv);

        taxRateTv = (TextView) findViewById(R.id.tax_rate_tv) ;
        //taxTv = (TextView) findViewById(R.id.tax_tv);
        feeTv = (TextView) findViewById(R.id.fee);


        Button refundBtn = (Button) findViewById(R.id.refund_btn);
        refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*
                if( refundAmountTv.getText().toString().equals("")){
                    amountRefTv.setText( " 공백 " );
                } else {
                    if( Integer.parseInt( refundAmountTv.getText().toString().replace(",","") ) > 1){
                        validation();
                    }else{
                        amountRefTv.setText( " 0 이상 " );
                    }
                }*/


                if( amountEt.getText().toString().equals("")){
                    amountRefTv.setText( " 공백 " );
                } else {
                    if( Integer.parseInt( amountEt.getText().toString().replace(",","") ) > 1){
                        validation();
                    }else{
                        amountRefTv.setText( " 0 이상 " );
                    }
                }


            }
        });
        getData();
    }

    private void psOk(){


        if(passwordEt.getText().toString().equals( mainApp.getUserPW())){

            refundAct();
        }else {
            resultTv.setText(" 비밀번호가 일치하지 않습니다.");

        }

    }



    private boolean validation(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {



                            Toast.makeText(context, "에러", Toast.LENGTH_LONG ).show();
                        }
                    }else {

                        refund_can = jsonObject.getBoolean("refund_can");


                        myDialog = new Dialog( MoneyGetActivity.this);

                        myDialog.setContentView(R.layout.dialog_password_ck);
                        myDialog.setTitle("비밀번호 확인");

                        passwordEt = (EditText) myDialog.findViewById(R.id.password_et);
                        resultTv = (TextView) myDialog.findViewById(R.id.result_tv);
                        if( refund_can) {
                            myDialog.setTitle("비밀번호 입력");
                        }else{
                            myDialog.setTitle("경고");
                            passwordEt.setText("예치금 부족");
                        }

                        psOkBtn = myDialog.findViewById(R.id.ps_ok);
                        psOkBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                psOk();
                            }
                        });

                        psCancelBtn = myDialog.findViewById(R.id.ps_cancel);
                        psCancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.cancel();
                            }
                        });

                        goDepositBtn = myDialog.findViewById(R.id.go_deposit);
                        goDepositBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MoneyGetActivity.this, DepositActivity.class);
                                myDialog.cancel();
                                finish();
                                startActivity(intent);
                            }
                        });

                        myDialog.show();
                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        ValidationRequest validationRequest = new ValidationRequest(responseListener);
        validationRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(validationRequest);

        return refund_can;
    }


    class ValidationRequest extends StringRequest {

        private Map<String, String> parameters;
        public ValidationRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit/refund_can.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );


            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());



            parameters.put("realAmount", amountEt.getText().toString().replace(",","")) ;
            parameters.put("taxRate", taxRateTv.getText().toString().replace("%","")) ;
            parameters.put("fee", feeTv.getText().toString().replace(",","")) ;


        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }




    private void refundAct(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));
                            finish();

                        }
                    }else {

                        refund_ok = jsonObject.getBoolean("refund");

                        if (refund_ok) {


                            String obj = " { 'from' :'" + mainApp.getUserKey() + "',  'to':'" + mainApp.getParentKey() + "' ,  'room' :'' ,  'msg': 'outrequest', 'data':'null', 'action':'outrequest'   }";


                            String realAmount = jsonObject.getString("realAmount");

                            String taxAmount = jsonObject.getString("taxAmount");

                            String feeAmount = jsonObject.getString("feeAmount");

                            String refundAmount = jsonObject.getString("refundAmount");


                            JSONObject json1 = new JSONObject(obj);

                            mainApp.mainActivity.sendMsg(json1.toString());


                            passwordEt.setVisibility(View.GONE);

                            psOkBtn.setVisibility(View.GONE);

                            psCancelBtn.setVisibility(View.GONE);

                            goDepositBtn.setVisibility(View.VISIBLE);

                            resultTv.setText(  " 신청금액 : " + realAmount + "\n 세금 : " + taxAmount + "\n 수수료 : " + feeAmount + "\n 실수령액 : " + refundAmount +"원  \n[환급신청]이 완료되었습니다.");

                        }
                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        ActRequest actRequest = new ActRequest(responseListener);
        actRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(actRequest);

    }


    class ActRequest extends StringRequest {

        private Map<String, String> parameters;
        public ActRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit/refund_act.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );



            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());




            parameters.put("realAmount", amountEt.getText().toString().replace(",","")) ;  // 요청금액
            parameters.put("taxRate", taxRateTv.getText().toString().replace("%","")) ;  // 세율
            parameters.put("fee", feeTv.getText().toString().replace(",","")) ;  // 수수료



        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }




    private void getData(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));
                            finish();

                        }
                    }else {
                        depositTv.setText(formatter.format(jsonObject.getInt("deposit")) + "원");
                        bankTv.setText(jsonObject.getString("bank"));
                        accountTv.setText(jsonObject.getString("acc"));
                        nameTv.setText(jsonObject.getString("rName"));

                        refundCanTv.setText(formatter.format(jsonObject.getInt("can")) + "원");

                        taxRateTv.setText(jsonObject.getString("taxRate") + "%");

                        feeTv.setText(jsonObject.getString("fee"));



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

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit/refund_page.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );

            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
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

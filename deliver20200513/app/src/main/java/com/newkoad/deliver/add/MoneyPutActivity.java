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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;



public class MoneyPutActivity extends Addition {


    private static final String TAG ="MoneyPutActivity" ;



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
    private boolean  charge_ok;

    private DecimalFormat formatter = new DecimalFormat("###,###");
    private String result="";


    private TextView resultTv, fee;
    private EditText passwordEt;

    private Button psOkBtn, psCancelBtn, goDepositBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_put);


        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(" 충 전 ");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }


        depositTv = (TextView) findViewById(R.id.deposit_tv);
        bankTv = (TextView) findViewById(R.id.bank_tv);
        accountTv = (TextView) findViewById(R.id.account_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);

        fee = (TextView) findViewById(R.id.fee);


        amountRefTv = (TextView) findViewById(R.id.amount_ref_tv) ;
        amountEt = (EditText) findViewById(R.id.amount_et);
        amountEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

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



        final Button chargeBtn = (Button) findViewById(R.id.charge_btn);
        chargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amountEt.getText().toString().equals("")){

                    amountRefTv.setText(" 공백입니다.  0 이상의 값을 입력해주세요.");

                }else{

                    if( Integer.parseInt(amountEt.getText().toString().replace(",","")) > 0){


                        myDialog = new Dialog( MoneyPutActivity.this);

                        myDialog.setContentView(R.layout.dialog_password_ck);
                        myDialog.setTitle("비밀번호 확인");

                        passwordEt = (EditText) myDialog.findViewById(R.id.password_et);
                        resultTv = (TextView) myDialog.findViewById(R.id.result_tv);

                        psOkBtn = myDialog.findViewById(R.id.ps_ok);
                        psOkBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(passwordEt.getText().toString().equals( mainApp.getUserPW())) {
                                    chargeAct(); // 충전처리
                                }else {
                                    resultTv.setText(" 비밀번호가 일치하지 않습니다.");
                                }
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
                                Intent intent = new Intent(MoneyPutActivity.this, DepositActivity.class);
                                myDialog.cancel();
                                finish();
                                startActivity(intent);
                            }
                        });
                        myDialog.show();


                    } else {
                        amountRefTv.setText(" 0 이상의 값을 입력해 주세요.");
                    }
                }

            }
        });
        read();
    }



    private void chargeAct(){

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


                        charge_ok = jsonObject.getBoolean("charge");
                        int chargeAmount = jsonObject.getInt("chargeAmount");


                        if (charge_ok) {


                            String obj = " { 'from' :'" + mainApp.getUserKey() + "',  'to':'admin' ,  'room' :'admin' ,  'msg': 'inrequest', 'data':'null', 'action':'inrequest'   }";
                            JSONObject json1 = new JSONObject(obj);
                            mainApp.mainActivity.sendMsg(json1.toString());

                            myDialog.setTitle("충전 결과");
                            psOkBtn.setVisibility(View.GONE);
                            psCancelBtn.setVisibility(View.GONE);
                            passwordEt.setVisibility(View.GONE);

                            goDepositBtn.setVisibility(View.VISIBLE);
                            resultTv.setText(chargeAmount + "원  [충전 신청]이 완료되었습니다.");
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

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit/charge_act.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );


            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());


            parameters.put("chargeAmount", amountEt.getText().toString().replace(",","")) ;
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }






    private void read(){



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
                        bankTv.setText(jsonObject.getString("c_bank"));
                        accountTv.setText(jsonObject.getString("c_account"));
                        nameTv.setText(jsonObject.getString("c_owner"));
                        fee.setText(jsonObject.getString("fee"));


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

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit/charge_page.php", listener, null);
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

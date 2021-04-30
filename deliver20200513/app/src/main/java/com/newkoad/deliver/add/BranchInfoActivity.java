package com.newkoad.deliver.add;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

public class BranchInfoActivity extends Addition {


    TextView stateTv, idTv, nameTv, classTv, numberTv, typeTv, kindTv;
    TextView ceoNameTv, phoneTv, telTv, faxTv, emailTv;
    TextView joinDateTv, depositTv, downPaymentTv, balanceTv, payNameTv, payBankTv;

    TextView zipcodeTv, addr1Tv, addr2Tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_info);

        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("회사정보");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        setHeaderBlue( (LinearLayout)findViewById(R.id.header) );

        stateTv = findViewById(R.id.state_tv);
        idTv = findViewById(R.id.id_tv);
        nameTv = findViewById(R.id.name_tv);

        classTv = findViewById(R.id.class_tv);
        numberTv = findViewById(R.id.number_tv);
        typeTv = findViewById(R.id.type_tv);
        kindTv = findViewById(R.id.kind_tv);

        ceoNameTv= findViewById(R.id.ceo_name_tv);
        phoneTv = findViewById(R.id.phone_tv);
        telTv = findViewById(R.id.tel_tv);
        faxTv = findViewById(R.id.fax_tv);
        emailTv = findViewById(R.id.email_tv);

        joinDateTv = findViewById(R.id.join_date_tv);
        depositTv = findViewById(R.id.deposit_tv);
        downPaymentTv = findViewById(R.id.down_payment_tv);
        balanceTv = findViewById(R.id.balance_tv);
        payNameTv = findViewById(R.id.pay_name_tv);
        payBankTv = findViewById(R.id.pay_bank_tv);






        getData();

    }





    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add_mgr/branch_info.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
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


    private void getData(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject json = new JSONObject(response);

                    stateTv.setText(json.getString("state"));
                    idTv.setText(json.getString("id"));
                    nameTv.setText(json.getString("name"));

                    classTv.setText(json.getString("class"));
                    numberTv.setText(json.getString("number"));
                    typeTv.setText(json.getString("type"));
                    kindTv.setText(json.getString("kind"));

                    ceoNameTv.setText(json.getString("ceo_name"));
                    phoneTv.setText(json.getString("phone"));
                    telTv.setText(json.getString("tel"));
                    faxTv.setText(json.getString("fax"));
                    emailTv.setText(json.getString("email"));

                    joinDateTv.setText(json.getString("join_date"));
                    depositTv.setText(json.getString("deposit"));
                    downPaymentTv.setText(json.getString("down_payment"));
                    balanceTv.setText(json.getString("balance"));
                    payNameTv.setText(json.getString("pay_name"));
                    payBankTv.setText(json.getString("pay_bank"));




                    progressDialog.dismiss();

                }catch(Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        };

        DataListRequest dataListRequest = new DataListRequest(responseListener);
        dataListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(dataListRequest);
    }



}



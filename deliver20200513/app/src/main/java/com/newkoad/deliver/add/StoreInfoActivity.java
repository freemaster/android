package com.newkoad.deliver.add;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.R;
import com.newkoad.deliver.add.Addition;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StoreInfoActivity extends Addition {


    TextView stateTv, idTv, nameTv;
    TextView classTv, numberTv, typeTv, kindTv;
    TextView ceoNameTv, phoneTv, telTv, faxTv, emailTv;
    TextView deadlineTv, deliveryCostTv;
    TextView startDateTv, mgrPayTv, mgrPayDayTv;
    TextView vaccountTv, bankNameTv, bankNumbTv, bankUserTv;
    CheckBox customerPayCb, mgrPayAutoCb, cashReceiptCb ;
    TextView zipcodeTv, addrRoadTv, addrLandTv, addrDetailTv;
    long addrXp, addrYp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("가맹점 정보"     ) ;
        headerCloseBtn = findViewById(R.id.header_closeBtn);

        stateTv = findViewById(R.id.state_tv);
        idTv = findViewById(R.id.id_tv);
        nameTv =findViewById(R.id.name_tv);

        classTv = findViewById(R.id.class_tv);
        numberTv= findViewById(R.id.number_tv);
        typeTv=findViewById(R.id.type_tv);
        kindTv=findViewById(R.id.kind_tv);

        ceoNameTv=findViewById(R.id.ceo_name_tv);
        phoneTv=findViewById(R.id.phone_tv);
        telTv=findViewById(R.id.tel_tv);
        faxTv=findViewById(R.id.fax_tv);
        emailTv=findViewById(R.id.email_tv);

        deadlineTv=findViewById(R.id.deadline_tv);
        deliveryCostTv=findViewById(R.id.delivery_coast);

        startDateTv=findViewById(R.id.start_date_tv);
        mgrPayTv=findViewById(R.id.mgr_pay_tv);
        mgrPayDayTv=findViewById(R.id.mgr_pay_day_tv);

        vaccountTv=findViewById(R.id.vaccount_tv);
        bankNameTv=findViewById(R.id.bank_name_tv);
        bankNumbTv=findViewById(R.id.bank_numb_tv);
        bankUserTv=findViewById(R.id.bank_user_tv);

        customerPayCb=findViewById(R.id.customer_pay_cb);
        mgrPayAutoCb=findViewById(R.id.mgr_pay_auto_cb);
        cashReceiptCb=findViewById(R.id.cash_receipt_cb);

        zipcodeTv=findViewById(R.id.zipcode_tv);
        addrRoadTv=findViewById(R.id.addr_road_tv);
        addrLandTv=findViewById(R.id.addr_land_tv);
        addrDetailTv=findViewById(R.id.addr_detail_tv);


        getData();



    }




    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/5add_mgr/store_mgr_info.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ));
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ));
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ));
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

                    deadlineTv.setText(json.getString("deadline"));
                    deliveryCostTv.setText(json.getString("delivery_cost"));

                    startDateTv.setText(json.getString("start_date"));
                    mgrPayTv.setText(json.getString("mgr_pay"));
                    mgrPayDayTv.setText(json.getString("mgr_pay_day"));

                    vaccountTv.setText(json.getString("vaccount"));
                    bankNameTv.setText(json.getString("bank_name"));
                    bankNumbTv.setText(json.getString("bank_numb"));
                    bankUserTv.setText(json.getString("bank_user"));

                    zipcodeTv.setText(json.getString("zipcode"));
                    addrRoadTv.setText(json.getString("addr_road"));
                    addrLandTv.setText(json.getString("addr_land"));
                    addrDetailTv.setText(json.getString("addr_detail"));

                    addrXp = json.getLong("addr_xp");
                    addrYp = json.getLong("addr_yp");





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

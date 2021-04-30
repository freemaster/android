package com.newkoad.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.add.Addition;
import com.newkoad.deliver.common.Adver;
import com.newkoad.deliver.common.Item;
import com.newkoad.deliver.common.Order;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class FinishKSActivity extends Addition {


    private static final String TAG ="FinishKSActivity" ;
    private FinishKSActivity finishActivity;

    protected String type;
    protected Item item;
    protected Order order;
    protected Adver adver;


    private JSONObject jsonObject = null;
    private JSONObject logObject = null;
    private String log = null;

    private String tran_serial_no = "";

    TextView authNum,  authDate  ;

    private DecimalFormat decimalFormat = new DecimalFormat("###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_ks);

        intent = getIntent();
        item = (Item)intent.getParcelableExtra("item");

        if(item.type.equals("O")){    order = (Order) item;       }else{      adver = (Adver) item;         }



        headerTitle = (TextView) findViewById(R.id.header_title);

        headerTitle.setText("완료");

        headerCloseBtn =findViewById(R.id.header_closeBtn);

        finishActivity = (FinishKSActivity) this;


        TextView orderDatetimeTv = findViewById(R.id.order_datetime_tv);
        orderDatetimeTv.setText(order.getDatetime());

        TextView sNameTv = findViewById(R.id.s_name_tv);
        sNameTv.setText( order.getsName());

        TextView sOwnerTv = findViewById(R.id.s_owner_tv);
        sOwnerTv.setText( order.getsOwner() );

        TextView sNumberTv = findViewById(R.id.s_number_tv);
        sNumberTv.setText( order.getsNumber() );

        TextView sAddrTv = findViewById(R.id.s_load_tv);
        sAddrTv.setText( order.getsRoad() );


        TextView sPhoneTv = findViewById(R.id.s_phone_tv);
        sPhoneTv.setText(PhoneNumberUtils.formatNumber(order.getsPhone()));

        TextView ePhoneTv = findViewById(R.id.e_phone_tv);
        ePhoneTv.setText(PhoneNumberUtils.formatNumber(order.getePhone()));


        TextView eAddrTv = findViewById(R.id.e_addr_tv);
        eAddrTv.setText(order.geteRoad());

        TextView goodTv = findViewById(R.id.order_good_tv);
        goodTv.setText( decimalFormat.format(order.getgPrice()) +  "원");

        TextView deliveryTv = findViewById(R.id.order_delivery_tv);
        deliveryTv.setText( decimalFormat.format(order.getdPrice())  + "원");

        TextView sPayTypeTv = findViewById(R.id.order_pay_type_tv);
        sPayTypeTv.setText(order.getPayTypeStr());







        authNum = findViewById(R.id.auth_num);
        authDate = findViewById(R.id.auth_date);


        getData();


    }



    private void getData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String anum = "";
                try {


                    log = response;
                    jsonObject = new JSONObject(response);

                    Log.d(TAG, "-----------response-------------------------");
                    Log.d(TAG, response);

                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));
                            finishActivity.finish();
                        }
                    }else {


                            TextView authNum = (TextView) findViewById(R.id.auth_num);
                            authNum.setText(jsonObject.getString("AuthNum"));

                            anum = jsonObject.getString("AuthNum") ;

                             TextView  authDate = (TextView) findViewById(R.id.auth_date);
                            authDate.setText(jsonObject.getString("Authdate"));

                            Button payCancel = findViewById(R.id.pay_cancel);
                            if( anum.length() > 6  ){
                                payCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent = new Intent(context, PayKSActivity.class);
                                        intent.putExtra("item", order);
                                        intent.putExtra("log", log);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                payCancel.setVisibility(View.GONE);

                            }


                        String log = jsonObject.getString("vlog_msg").toString();
                        logObject = new JSONObject(log.toString());

                        authNum.setText(logObject.getString("authNum"));
                        authDate.setText(logObject.getString("authdate"));





                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };

        FinishDataRequest finishDataRequest =new FinishDataRequest( responseListener);

        finishDataRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(finishDataRequest);

    }


    class FinishDataRequest extends StringRequest {
        private Map<String, String> parameters;
        public FinishDataRequest( Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/3finish/finish_detail.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());

            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("orderKey", String.valueOf(order.getKey() ) );


            Log.d(TAG, "---------------------------------");
            Log.d(TAG, "paramaters: " + parameters);

        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }

}

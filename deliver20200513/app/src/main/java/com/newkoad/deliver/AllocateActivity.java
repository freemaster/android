package com.newkoad.deliver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AllocateActivity extends ReceiveActivity {

    private static final String TAG ="AllocateActivity" ;
    AllocateActivity allocateActivity ;
    Dialog payDialog, payInfoDialog;
    Button pickupBtn , payBtn,  finishBtn  ;// bottom


    EditText payAmountEt ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainApp.setOrderKey(0);
        mainApp.allocateActivity = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        headerTitle.setText("배차상세 ");

        allocateActivity = (AllocateActivity) this;


        mainApp.setAllocateActivity(allocateActivity) ;

        mainApp.act = false;


        mainApp.setOrderKey(order.key);
        allocateBtn.setVisibility(View.GONE);


        pickupBtn = (Button) findViewById(R.id.pickupBtn);
        if(item.status == 4) {
            pickupBtn.setVisibility(View.VISIBLE);
        }


        payBtn = (Button) findViewById(R.id.payBtn);
        if(item.status == 5 ){
            payBtn.setVisibility(View.VISIBLE);
            payBtn.setText(" 결  제 (" + order.getPayTypeStr() +")");
        }

        smsCustomerBtn = (ImageButton) findViewById(R.id.sms_customer_btn);
        smsCustomerBtn.setVisibility(View.VISIBLE);

        pickupBtn = (Button)findViewById(R.id.pickupBtn);
        pickupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject json = new JSONObject(response);



                            JSONObject jsonObject = new JSONObject(response);

                            if( jsonObject.has("aidresult") ) {

                                if (jsonObject.getString("aidresult").equals("fail")) {

                                    mainApp.mainActivity.openAlert(jsonObject.getString("msg"));

                                    allocateActivity.finish();
                                }
                            }else {

                                if (jsonObject.getString("pickup").equals("true")) {



                                    int parentKey = mainApp.parentKey;

                                    int key = item.key;

                                    int userKey = mainApp.userKey;

                                    String obj1 = " { 'from' :'" + userKey + "',  'to':'b" + parentKey + "' ,  'room' :'b3' ,  'msg': 'pickup', 'data':'" + key + "', 'action':'pickup'   }";

                                    String obj = " { 'from' :'" + userKey + "',  'to':'b" + parentKey + "' ,  'room' :'b3' ,  'msg': 'pickup', 'data':'" + key + "', 'action':'pickup'   }";

                                    JSONObject json1 = new JSONObject(obj);

                                    mainApp.mainActivity.sendMsg(json1.toString());


                                    pickupBtn.setVisibility(View.GONE);

                                    payBtn.setVisibility(View.VISIBLE);

                                    JSONObject json3 = new JSONObject(obj1);

                                    mainApp.mainActivity.doPickup();
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                PickupRequest pickupRequest =new PickupRequest(item.key, responseListener);

                pickupRequest.setShouldCache(false);

                RequestQueue queue = Volley.newRequestQueue(AllocateActivity.this);

                queue.add(pickupRequest);

            }
        });



        payBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                intent = new Intent( context, PayKSActivity.class);

                intent.putExtra("item" , order);

                startActivity(intent);

            }
        });



    }



    class PickupRequest extends StringRequest {

        private Map<String, String> parameters;
        public PickupRequest(int itemKey,  Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() +"/2allocate/allocate_pickup.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ));
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ));
            parameters.put("userID", mainApp.getUserID() );

            parameters.put("userGroup", mainApp.getUserGroup());

            parameters.put("aID", mainApp.getAndroidID());
            parameters.put("fID", String.valueOf( mainApp.getUserKey() ));

            parameters.put("key", String.valueOf( itemKey ) );
            parameters.put("token", mainApp.getTokenp());

        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


}





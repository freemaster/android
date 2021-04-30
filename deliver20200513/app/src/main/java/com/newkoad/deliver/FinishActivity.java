package com.newkoad.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.newkoad.deliver.add.Addition;
import com.newkoad.deliver.common.Adver;
import com.newkoad.deliver.common.Item;
import com.newkoad.deliver.common.Order;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class FinishActivity extends Addition {


    private static final String TAG ="FinishActivity" ;
    private FinishActivity finishActivity;

    protected String type;
    protected Item item;
    protected Order order;
    protected Adver adver;


    private JSONObject jsonObject = null;
    private JSONObject logObject = null;
    private String log = null;

    private String tran_serial_no = "";



    private DecimalFormat decimalFormat = new DecimalFormat("###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        intent = getIntent();
        item = (Item)intent.getParcelableExtra("item");

        if(item.type.equals("O")){
            order = (Order) item;

        }else{
            adver = (Adver) item;

        }



        headerTitle = (TextView) findViewById(R.id.header_title);

        headerTitle.setText("완료상세");

        headerCloseBtn =findViewById(R.id.header_closeBtn);

        finishActivity = (FinishActivity) this;


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



        getData();


    }



    private void getData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    log = response;
                    jsonObject = new JSONObject(response);




                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));
                            finishActivity.finish();
                        }
                    }else {



                        TextView payType = findViewById(R.id.pay_type_tv);
                        payType.setText(jsonObject.getString("TRAN_TYPE"));

                        TextView payDatetime = findViewById(R.id.pay_datetime_tv);
                        payDatetime.setText(jsonObject.getString("APPROVAL_DATE"));

                        TextView payAmount = findViewById(R.id.pay_amount_tv);
                        if( !jsonObject.getString("TOTAL_AMOUNT").equals("null") ) {
                            payAmount.setText(decimalFormat.format(Integer.parseInt(jsonObject.getString("TOTAL_AMOUNT"))) + "원");
                        }


                        if (jsonObject.getString("TRAN_TYPE").equals("credit")) {
                            LinearLayout cardInfoLay = findViewById(R.id.card_info_lay);
                            cardInfoLay.setVisibility(View.VISIBLE);

                            TextView payCardNum = findViewById(R.id.pay_card_num_tv);
                            payCardNum.setText(jsonObject.getString("CARD_NUM"));

                            TextView payCardName = findViewById(R.id.pay_card_name_tv);
                            payCardName.setText(jsonObject.getString("CARD_NAME"));

                            TextView payInstallment = findViewById(R.id.pay_installment_tv);
                            payInstallment.setText(jsonObject.getString("INSTALLMENT"));

                            TextView payResultCode = findViewById(R.id.pay_result_code_tv);
                            payResultCode.setText(jsonObject.getString("RESULT_CODE"));

                            TextView payResultMsg = findViewById(R.id.pay_result_msg_tv);
                            payResultMsg.setText(jsonObject.getString("RESULT_MSG"));


                            TextView apprNum = findViewById(R.id.appr_num);
                            apprNum.setText(jsonObject.getString("APPROVAL_NUM"));
                            TextView apprDate = findViewById(R.id.appr_date);
                            apprDate.setText(jsonObject.getString("APPROVAL_DATE"));
                            TextView tranSerialNo = findViewById(R.id.tran_serialno);
                            tranSerialNo.setText(jsonObject.getString("TRAN_SERIALNO"));
                            tran_serial_no = jsonObject.getString("TRAN_SERIALNO");


                            TextView shopBizNum = findViewById(R.id.shop_biz_num);
                            shopBizNum.setText(jsonObject.getString("SHOP_BIZ_NUM"));

                            TextView shopName = findViewById(R.id.shop_name);
                            shopName.setText(jsonObject.getString("SHOP_NAME"));

                            TextView shopOwner = findViewById(R.id.shop_owner);
                            shopOwner.setText(jsonObject.getString("SHOP_OWNER"));

                            TextView shopTel = findViewById(R.id.shop_tel);
                            shopTel.setText(jsonObject.getString("SHOP_TEL"));




                            Button payCancel = findViewById(R.id.pay_cancel);
                            if( tran_serial_no.length() > 6  ){
                                payCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent = new Intent(context, PayActivity.class);
                                        intent.putExtra("item", order);
                                        intent.putExtra("log", log);

                                        startActivity(intent);
                                    }
                                });

                            }else {
                                payCancel.setVisibility(View.GONE);

                            }

                        }





                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };

        FinishDataRequest finishDataRequest =new FinishDataRequest( responseListener);

        finishDataRequest.setShouldCache(false);




        RequestQueue queue = newRequestQueue(this);
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



        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }

}

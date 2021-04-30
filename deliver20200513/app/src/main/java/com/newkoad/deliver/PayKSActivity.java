package com.newkoad.deliver;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
@SuppressWarnings("unchecked")
public class PayKSActivity extends Addition {

    private static final String TAG ="PayKSActivity" ;
    private PayKSActivity payKSActivity;

    EditText et_Amount;
    Button btn_credit ,btn_creditCancel;
    Button btn_cashCard ,btn_cashCardCancel ;
    Button btn_cashKey, btn_cashKeyCancel;
    Button btn_pointKey, btn_pointKeyCancel;
    Button btn_pointCard, btn_pointCardCancel;

    String prevAuthNum = "";
    String prevAuthDate = "";
    String prevClassfication = "";

    public LinkedHashMap<String, String> map = new LinkedHashMap<>();

    HashMap<String, String> b_hash  = new HashMap<>();
    HashMap<String, String> m_hash  = new HashMap<>() ;


    Dialog dialog ;
    Intent rIntent;
    protected Item item;
    protected String type;
    protected Order order;
    protected Adver adver;

    public int payType  =  0;
    private String tranType = "";


    private String RESULT_CODE = "";
    private String RESULT_MSG = "";
    private String log = null;

    public Button cashBtn, cardBtn, preBtn, cancelBtn;




    public String tran_type;
    public boolean cancel = false;

    boolean card_cancel = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ks);

        mainApp = (MainApp)getApplicationContext();
        context = getApplicationContext();
        payKSActivity = (PayKSActivity) this;

        intent = getIntent();
        item = (Item)intent.getParcelableExtra("item");
        log = (String) intent.getStringExtra("log");

        if(item.type.equals("O")){
            order = (Order) item;
        }else{
            adver = (Adver) item;
        }




        et_Amount = (EditText)findViewById(R.id.txtAmount);

        btn_credit = (Button)findViewById(R.id.btn_credit);
        btn_cashCard = (Button)findViewById(R.id.btn_cashcard);
        btn_cashKey = (Button)findViewById(R.id.btn_cashkey);
        btn_creditCancel = (Button)findViewById(R.id.btn_credit_cancel);
        btn_cashCardCancel = (Button)findViewById(R.id.btn_cashcard_cancel);
        btn_cashKeyCancel = (Button)findViewById(R.id.btn_cashkey_cancel);
        btn_pointCard = (Button)findViewById(R.id.btn_pointcard);
        btn_pointCardCancel = (Button)findViewById(R.id.btn_pointcard_cancel);
        btn_pointKey = (Button)findViewById(R.id.btn_pointkey);
        btn_pointKeyCancel = (Button)findViewById(R.id.btn_pointkey_cancel);

        btn_credit.setOnClickListener(bClickListener);
        btn_cashCard.setOnClickListener(bClickListener);
        btn_cashKey.setOnClickListener(bClickListener);
        btn_creditCancel.setOnClickListener(bClickListener);
        btn_cashCardCancel.setOnClickListener(bClickListener);
        btn_cashKeyCancel.setOnClickListener(bClickListener);
        btn_pointCard.setOnClickListener(bClickListener);
        btn_pointCardCancel.setOnClickListener(bClickListener);
        btn_pointKey.setOnClickListener(bClickListener);
        btn_pointKeyCancel.setOnClickListener(bClickListener);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");



        if( log != null) {

            cancel = true;
            tran_type ="credit_cancel";



            try {
                JSONObject jsonObject = new JSONObject(log);




                prevAuthNum = jsonObject.getString("AuthNum") ;
                prevAuthDate = jsonObject.getString("Authdate");
                et_Amount.setText(jsonObject.getString("amount"));



            }catch (Exception e){
                e.printStackTrace();
            }

        }else {

            et_Amount.setText( decimalFormat.format( order.gettPrice() ) );
        }



        if(cancel){


            cancelBtn = findViewById(R.id.cancel_btn);
            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setOnClickListener(bClickListener);

        }else {

            cardBtn = (Button) findViewById(R.id.card_btn);
            cardBtn.setOnClickListener(bClickListener);


            if (order.getPayType() == 1) {


                cashBtn = (Button) findViewById(R.id.cash_btn);
                cashBtn.setVisibility(View.VISIBLE);
                cashBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payType = 1;
                        payTrans();
                    }
                });

                cardBtn = (Button) findViewById(R.id.card_btn);
                cardBtn.setVisibility(View.VISIBLE);

            } else if (order.getPayType() == 2) {


                cashBtn = (Button) findViewById(R.id.cash_btn);
                cashBtn.setVisibility(View.VISIBLE);
                cashBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payType = 1;
                        payTrans();
                    }
                });

                cardBtn = (Button) findViewById(R.id.card_btn);
                cardBtn.setVisibility(View.VISIBLE);


            } else if (order.getPayType() == 3) {

                preBtn = (Button) findViewById(R.id.pre_btn);
                preBtn.setVisibility(View.VISIBLE);
                preBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        payType = 3;
                        payTrans();
                    }
                });

            }

        }
        dialog = new Dialog(PayKSActivity.this);
        dialog.setContentView(R.layout.dialog_pay_result);
        dialog.setTitle(" 결과");

        Button closeBtn = dialog.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainApp.getAllocateActivity().finish();
                dialog.dismiss();
                finish();
            }
        });


        TextView sNameTv = findViewById(R.id.s_name_tv);

        sNameTv.setText( order.getsName());

        TextView sOwnerTv = findViewById(R.id.s_owner_tv);

        sOwnerTv.setText( order.getsOwner() );

        TextView sAddrTv = findViewById(R.id.s_load_tv);

        sAddrTv.setText( order.getsRoad() );

        TextView sPhoneTv = findViewById(R.id.s_phone_tv);
        sPhoneTv.setText(PhoneNumberUtils.formatNumber(order.getsPhone()));

    }




    Button.OnClickListener bClickListener = new Button.OnClickListener()
    {
        public void onClick(View v){


            HashMap<String, byte[]> m_hash = new HashMap<String, byte[] >();


            m_hash.put("TelegramType","0200".getBytes());


            String tid = order.getsTid();
            //m_hash.put("DPTID","AT0258341A".getBytes());
            //b_hash.put("DPTID","AT0258341A");

            m_hash.put("DPTID",tid.getBytes());
            b_hash.put("DPTID",tid);

            m_hash.put("PosEntry","S".getBytes());
            m_hash.put("PayType","00".getBytes());

            m_hash.put("TotalAmount",getStrMoneytoTgAmount(et_Amount.getText().toString()));

            b_hash.put("TotalAmount",et_Amount.getText().toString().replace(",", ""));

            m_hash.put("ServicAmount",getStrMoneytoTgAmount("0"));
            m_hash.put("TaxAmount",getStrMoneytoTgAmount("0"));
            m_hash.put("FreeAmount",getStrMoneytoTgAmount("0"));
            m_hash.put("AuthNum", "".getBytes());
            m_hash.put("Authdate", "".getBytes());
            m_hash.put("Amount",getStrMoneytoTgAmount(et_Amount.getText().toString()));
            m_hash.put("Filler","".getBytes());
            m_hash.put("SignTrans","N".getBytes());

            m_hash.put("PlayType","D".getBytes());
            m_hash.put("CardType","".getBytes());
            m_hash.put("BranchNM","데몬테스트가맹점".getBytes());
            m_hash.put("BIZNO","".getBytes());
            m_hash.put("TransType","".getBytes());
            ComponentName compName = new ComponentName("ksnet.kscic","ksnet.kscic.PaymentDlg");

            Intent intent = new Intent(Intent.ACTION_MAIN);

            switch(v.getId())  {

                case R.id.card_btn :

                    Log.d( TAG , "aasd:::"+lsInstall("ksnet.kscic"));
                    if (lsInstall("ksnet.kscic") == false )
                    {
                        String url = "market://details?id=" + "ksnet.kscic";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                    }
                    else
                    {
                        makeSendFirstMessage();
                        payType = 2;
                        m_hash.put("ReceiptNo","X".getBytes());
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setComponent(compName);
                        intent.putExtra("AdminInfo_Hash",m_hash);
                        intent.setAction("test");
                        startActivityForResult(intent,0);
                    }

                    break;


                case R.id.cancel_btn:

                    card_cancel = true;
                    makeSendFirstMessage();
                    m_hash.put("TelegramType","0420".getBytes());
                    m_hash.put("ReceiptNo","X".getBytes());
                    m_hash.put("AuthNum",prevAuthNum.getBytes());
                    m_hash.put("Authdate",prevAuthDate.getBytes());


                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);

                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);



                    break;



                case R.id.btn_credit:

                    m_hash.put("ReceiptNo","X".getBytes());
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);
                    break;

                case R.id.btn_credit_cancel:

                    m_hash.put("TelegramType","0420".getBytes());
                    m_hash.put("ReceiptNo","X".getBytes());
                    m_hash.put("AuthNum",prevAuthNum.getBytes());
                    m_hash.put("Authdate",prevAuthDate.getBytes());


                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);

                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);

                    break;

                case R.id.btn_cashcard:


                    m_hash.put("ReceiptNo","".getBytes());
                    m_hash.put("PayType","00".getBytes());
                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);

                    break;

                case R.id.btn_cashcard_cancel:

                    m_hash.put("TelegramType","0420".getBytes());
                    m_hash.put("ReceiptNo","".getBytes());
                    m_hash.put("AuthNum",prevAuthNum.getBytes());

                    m_hash.put("Authdate",prevAuthDate.getBytes());
                    m_hash.put("PayType","10".getBytes());

                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);

                    break;

                case R.id.btn_cashkey:


                    m_hash.put("ReceiptNo","01000001234".getBytes());
                    m_hash.put("PosEntry","K".getBytes());

                    System.out.println("send [ReceiptNo]:: "+new String("01012345678".getBytes()));
                    System.out.println("send [TelegramType]:: "+new String("0200".getBytes()));
                    System.out.println("send [PosEntry]:: "+new String("K".getBytes()));
                    System.out.println("send [AuthNum]:: "+new String("".getBytes()));
                    System.out.println("send [Authdate]:: "+new String("".getBytes()));
                    System.out.println("send [PayType]:: "+new String("00".getBytes()));


                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);

                    break;

                case R.id.btn_cashkey_cancel:

                    m_hash.put("TelegramType","0420".getBytes());
                    m_hash.put("ReceiptNo","01012345678".getBytes());
                    m_hash.put("AuthNum",prevAuthNum.getBytes());
                    m_hash.put("Authdate",prevAuthDate.getBytes());
                    m_hash.put("PosEntry","K".getBytes());
                    m_hash.put("PayType","10".getBytes());

                    System.out.println("send [ReceiptNo]:: "+new String("01012345678".getBytes()));
                    System.out.println("send [TelegramType]:: "+new String("0420".getBytes()));
                    System.out.println("send [PosEntry]:: "+new String("K".getBytes()));
                    System.out.println("send [AuthNum]:: "+new String(prevAuthNum.getBytes()));
                    System.out.println("send [Authdate]:: "+new String(prevAuthDate.getBytes()));
                    System.out.println("send [PayType]:: "+new String("10".getBytes()));

                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);

                    break;


                case R.id.btn_pointcard:

                    m_hash.put("TransType","PC".getBytes());
                    m_hash.put("ReceiptNo","".getBytes());
                    m_hash.put("WorkType","03".getBytes());

                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);

                    break;


                case R.id.btn_pointcard_cancel:

                    m_hash.put("TransType","PC".getBytes());
                    m_hash.put("TelegramType","0420".getBytes());
                    m_hash.put("ReceiptNo","".getBytes());

                    m_hash.put("WorkType","03".getBytes());
                    m_hash.put("PayType","01".getBytes());

                    m_hash.put("AuthNum",prevAuthNum.getBytes());
                    m_hash.put("Authdate",prevAuthDate.getBytes());

                    break;


                case R.id.btn_pointkey:

                    m_hash.put("TransType","PC".getBytes());
                    m_hash.put("ReceiptNo","1111222233334444".getBytes());
                    m_hash.put("WorkType","03".getBytes());
                    m_hash.put("PayType","01".getBytes());
                    m_hash.put("PosEntry","K".getBytes());

                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);
                    break;


                case R.id.btn_pointkey_cancel:

                    m_hash.put("TransType","PC".getBytes());
                    m_hash.put("ReceiptNo","1111222233334444".getBytes());
                    m_hash.put("WorkType","03".getBytes());
                    m_hash.put("PayType","01".getBytes());
                    m_hash.put("PosEntry","K".getBytes());

                    m_hash.put("AuthNum",prevAuthNum.getBytes());
                    m_hash.put("Authdate",prevAuthDate.getBytes());

                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(compName);
                    intent.putExtra("AdminInfo_Hash",m_hash);
                    intent.setAction("test");
                    startActivityForResult(intent,0);
                    break;
            }


        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && data != null)   {

            RESULT_CODE = "0000";

            Log.d(TAG, m_hash.toString() );

            m_hash =  (HashMap<String, String>)data.getSerializableExtra("result");


            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(m_hash !=null) {



                prevAuthNum = m_hash.get("AuthNum");
                prevAuthDate = m_hash.get("Authdate");
                prevClassfication = m_hash.get("Classification");

                m_hash.put("amount", et_Amount.getText().toString().replace(",", "") );

                System.out.println("recv [Classification]:: "+new String(m_hash.get("Classification")));
                System.out.println("recv [TelegramType]:: "+new String(m_hash.get("TelegramType")));
                System.out.println("recv [Dpt_Id]:: "+new String(m_hash.get("Dpt_Id")));
                System.out.println("recv [Enterprise_Info]:: "+new String(m_hash.get("Enterprise_Info")));

                System.out.println("recv [Full_Text_Num]:: "+new String(m_hash.get("Full_Text_Num")));
                System.out.println("recv [Status]:: "+new String(m_hash.get("Status")));

                System.out.println("recv [Authdate]:: "+new String(m_hash.get("Authdate")));
                System.out.println("recv [Message1]:: "+new String(m_hash.get("Message1")));
                System.out.println("recv [Message2]:: "+new String(m_hash.get("Message2")));
                System.out.println("recv [AuthNum]:: "+new String(m_hash.get("AuthNum")));


                System.out.println("recv [FranchiseID]:: "+new String(m_hash.get("FranchiseID")));
                System.out.println("recv [IssueCode]:: "+new String(m_hash.get("IssueCode")));
                System.out.println("recv [CardName]:: "+new String(m_hash.get("CardName")));

                System.out.println("recv [PurchaseCode]:: "+new String(m_hash.get("PurchaseCode")));
                System.out.println("recv [PurchaseName]:: "+new String(m_hash.get("PurchaseName")));
                System.out.println("recv [Remain]:: "+new String(m_hash.get("Remain")));
                System.out.println("recv [point1]:: "+new String(m_hash.get("point1")));
                System.out.println("recv [point2]:: "+new String(m_hash.get("point2")));
                System.out.println("recv [point3]:: "+new String(m_hash.get("point3")));
                System.out.println("recv [notice1]:: "+new String(m_hash.get("notice1")));
                System.out.println("recv [notice2]:: "+new String(m_hash.get("notice2")));
                System.out.println("recv [CardNo]:: "+new String(m_hash.get("CardNo")));
            }


            Toast.makeText(this, "성공", Toast.LENGTH_LONG).show();

            payTrans();
        } else if(resultCode ==RESULT_FIRST_USER && data != null) {

            m_hash.put("Error1", "가맹점 다운로드 후 사용" );

        } else{
            Toast.makeText(this, "응답값 리턴 실패", Toast.LENGTH_LONG).show();
            m_hash.put("Error1", "응답값 리턴 실패" );
        }

        if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "앱 호출 실패", Toast.LENGTH_LONG).show();
            m_hash.put("Error2", "앱 호출 실패" );
        }


        if(card_cancel ){

            finish();
        }

    }

    public byte[] getStrMoneytoTgAmount(String Money)
    {
        byte[] TgAmount = null;
        if (Money.length() == 0 ) {
            Toast.makeText(PayKSActivity.this, "테스트 금액으로 승인진행", Toast.LENGTH_SHORT).show();
            return "000000001004".getBytes();
        }else
        {
            Long longMoney = Long.parseLong(Money.replace(",", ""));
            Money = String.format("%012d",longMoney);

            TgAmount = Money.getBytes();
            return TgAmount;
        }
    }
































    public void cardTranData(){

        makeSendFirstMessage();

    }


    private void makeCashMessage(){

        map.put("TOTAL_AMOUNT",  String.valueOf(  order.gettPrice() ) );
        map.put("SHOP_BIZ_NUM", order.getsNumber() );
        map.put("SHOP_NAME", order.getsName() );
        map.put("SHOP_OWNER", order.getsOwner() );
        map.put("SHOP_ADDRESS", order.getsRoad() );
        map.put("SHOP_TEL", order.getsPhone());
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("APPROVAL_DATE", simpleDateFormat.format(date));


        m_hash.put("TOTAL_AMOUNT",  String.valueOf(  order.gettPrice() ) );
        m_hash.put("SHOP_BIZ_NUM", order.getsNumber() );
        m_hash.put("SHOP_NAME", order.getsName() );
        m_hash.put("SHOP_OWNER", order.getsOwner() );
        m_hash.put("SHOP_ADDRESS", order.getsRoad() );
        m_hash.put("SHOP_TEL", order.getsPhone());
        m_hash.put("APPROVAL_DATE", simpleDateFormat.format(date));
    }



    private void makeSendFirstMessage(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        CardFirstRequest cardFirstRequest = new CardFirstRequest(responseListener);
        cardFirstRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(cardFirstRequest);
    }


    class CardFirstRequest extends StringRequest {

        private Map<String, String> parameters;
        public CardFirstRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/2allocate/pay_first.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());


            parameters.put("orderKey", String.valueOf(order.getKey() ) );
            parameters.put("payType", String.valueOf( order.getPayType() ) );

            m_hash.put("TOTAT_AMOUNT" ,  et_Amount.getText().toString() );
            m_hash.put("order_key" ,  String.valueOf(order.getKey()) );



            JSONObject jsonObject = new JSONObject(b_hash);  // 처음 전
            parameters.put("totalMsg", jsonObject.toString() );


            if(payType == 1) {
                parameters.put("cash", String.valueOf(order.gettPrice()));
                parameters.put("card", "0");
            }else if(payType == 2){

                parameters.put("cash", "0");
                parameters.put("card", String.valueOf(order.gettPrice()));
            }else if( payType  == 3){
                parameters.put("cash", "0");
                parameters.put("card", "0");

            }

        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }




    public void payTrans(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                TextView shopNameTv = dialog.findViewById(R.id.shop_name_tv);
                TextView amountTv = dialog.findViewById(R.id.total_amoun_tv);
                TextView resultMsgTv = dialog.findViewById(R.id.result_msg_tv);
                TextView resultCodeTv = dialog.findViewById(R.id.result_code_tv);

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));

                            payKSActivity.finish();
                        }
                    }else {

                        if (jsonObject.getString("payok").equals("true")) {


                            item.status = 7;
                            mainApp.gpsUpdateCnt = 0;
                            mainApp.locationList.clear();

                            int parentKey = mainApp.parentKey;
                            int key = item.key;
                            int userKey = mainApp.userKey;


                            String obj = " { 'from' :'" + userKey + "',  'to':'b" + parentKey + "' ,  'room' :'b" + parentKey + "' ,  'msg': 'finish', 'data':'" + order.getsKey() + "', 'action':'finish'   }";
                            JSONObject json1 = new JSONObject(obj);
                            mainApp.mainActivity.sendMsg(json1.toString());


                            String obj2 = " { 'from' :'" + userKey + "',  'to':'" + mainApp.getBoxID() + "' ,   'msg': 'stop', 'data':'" + order.getsKey() + "', 'action':'stop'   }";
                            JSONObject json2 = new JSONObject(obj2);
                            mainApp.mainActivity.sendMsg(json2.toString());


                            mainApp.mainActivity.doFinish();
                            resultMsgTv.setText("성공");
                        } else {

                            resultMsgTv.setText("실패");
                        }


                        if (payType == 1) {
                            amountTv.setText(String.valueOf(order.gettPrice()));
                            shopNameTv.setText(order.getsName());

                        } else if (payType == 2) {



                        } else if (payType == 3) {
                            amountTv.setText(String.valueOf(order.gettPrice()));
                            shopNameTv.setText(order.getsName());

                        }

                        dialog.show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        PayTransRequest payTransRequest =new PayTransRequest(responseListener);
        payTransRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(PayKSActivity.this);
        queue.add(payTransRequest);

    }


    class PayTransRequest extends StringRequest {
        private Map<String, String> parameters;
        public PayTransRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/2allocate/pay_trans.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());

            parameters.put("orderKey", String.valueOf(order.getKey() ) );


            parameters.put("RESULT_CODE", RESULT_CODE );
            parameters.put("RESULT_MSG", RESULT_MSG );



            if(payType == 1) {
                parameters.put("payType", String.valueOf(payType) );
                parameters.put("cash", String.valueOf(order.gettPrice()));
                parameters.put("card", "0");

                //map.put("TRAN_TYPE", "cash" );
                m_hash.put("TRAN_TYPE", "cash" );
                makeCashMessage();
            }else if(payType == 2){
                parameters.put("payType", String.valueOf(payType) );
                parameters.put("cash", "0");
                parameters.put("card", String.valueOf(order.gettPrice()));
            }else if( payType == 3){
                parameters.put("payType", String.valueOf( payType) );
                parameters.put("cash", "0");
                parameters.put("card", "0");

                m_hash.put("TRAN_TYPE", "prepay" );
                makeCashMessage();
            }




            JSONObject jsonObject = null;
            if( m_hash != null ){
                jsonObject = new JSONObject(m_hash);
                parameters.put("totalMsg", jsonObject.toString() );
            }

            Log.d("route : " , mainApp.mainActivity.getRoute().toString() );
            parameters.put("route" , mainApp.mainActivity.getRoute().toString() );



        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }

    private boolean lsInstall(String name )
    {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(name.trim(), PackageManager.GET_META_DATA);
            ApplicationInfo appInfo = pi.applicationInfo;
            // 패키지가 있을 경우.
            Log.d(TAG,"Enabled value = " + appInfo.enabled);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.d(TAG,"패키지가 설치 되어 있지 않습니다.");
            return false;
        }

    }
}

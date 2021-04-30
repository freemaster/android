package com.newkoad.deliver;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class PayActivity extends Addition implements View.OnClickListener {
    private static final String TAG ="PayActivity" ;

    private PayActivity payActivity;
    private String msg ="";
    private String payStep = "B";
    public int payType  =  0;


    private String tranType = "";

    protected String type;
    protected Item item;
    protected Order order;
    protected Adver adver;


    public LinearLayout installment_val_lay;
    public LinearLayout appr_num_lay , appr_date_lay;
    public LinearLayout tran_serialno_lay, sign_flag_lay;
    public LinearLayout tid_lay, biznum_lay, name_lay, owner_lay, address_lay, tel_lay;
    public LinearLayout add_field_lay, order_num_lay, customer_code_lay;
    public LinearLayout taxLay;

    public TextView tranno;
    public TextView amount , tax ;

    public EditText tip ,  installment;
    public String installmentVal;

    public EditText order_num , customer_code;
    public EditText receipt_email ;
    public EditText biznum ;

    public TextView appr_num , appr_date, tran_serialno ;
    public TextView installment_val;  // my
    private TextView receipt_sms , tid;
    private TextView  name ,  owner ,  address ,  tel;


    public EditText add_field ,  sign_flag;
    public EditText keyin_flag;
    public EditText print_option;

    public Button cashBtn;
    public Button cardBtn;
    public Button preBtn;
    public Button cancelBtn;


    public String tran_type;
    public String result_code;
    public boolean multi;
    public boolean input_addfield;

    public boolean cancel = false;


    public int month  = 0;
    private Button selBtn = null;
    private Button nowIn = null;
    private Button in0, in2, in3, in4, in5, in6, in7, in8, in9, in12;
    public LinkedHashMap<String, String> map = new LinkedHashMap<>();

    ArrayAdapter<CharSequence> adspin;

    public static String LF = String.format("%c", 0x0a);
    public static String AL = String.format("%c", 0x1b) + String.format("%c", 0x61) + String.format("%c", 0x00);
    public static String AC = String.format("%c", 0x1b) + String.format("%c", 0x61) + String.format("%c", 0x01);
    public static String AR = String.format("%c", 0x1b) + String.format("%c", 0x61) + String.format("%c", 0x02);
    public static String UL_ON = String.format("%c", 0x1b) + String.format("%c", 0x2d) + String.format("%c", 0x02);
    public static String UL_OFF = String.format("%c", 0x1b) + String.format("%c", 0x2d) + String.format("%c", 0x00);
    public static String IVT_ON = String.format("%c", 0x1d) + String.format("%c", 0x42) + String.format("%c", 0x01);
    public static String IVT_OFF = String.format("%c", 0x1d) + String.format("%c", 0x42) + String.format("%c", 0x00);
    public static String X1 = String.format("%c", 0x1b) + String.format("%c", 0x21) + String.format("%c", 0x02);
    public static String X2 = String.format("%c", 0x1b) + String.format("%c", 0x21) + String.format("%c", 0x30);
    public static String W2 = String.format("%c", 0x1b) + String.format("%c", 0x21) + String.format("%c", 0x20);
    public static String H2 = String.format("%c", 0x1b) + String.format("%c", 0x21) + String.format("%c", 0x10);
    public static String BC = String.format("%c", 0x1d) + String.format("%c", 0x68) + String.format("%c", 0x40) + String.format("%c", 0x1d) + String.format("%c", 0x77) + String.format("%c", 0x02) + String.format("%c", 0x1d) + String.format("%c", 0x6B) + String.format("%c", 0x49);

    Dialog dialog ;
    Intent rIntent;


    public LinearLayout goodLay, deliveryLay, inLay;
    public TextView goodPrice, deliveryPrice;


    private String RESULT_CODE = "";
    private String RESULT_MSG = "";
    private String log = null;




  @Override
  public void onClick(View v) {


      switch (v.getId()){
          case R.id.in0 :
              month  = 0;
              changeColor( (Button) v.findViewById(R.id.in0) );
              break;

          case R.id.in2 :
              month  = 2;
              changeColor( (Button) v.findViewById(R.id.in2) );
              break;

          case R.id.in3 :
              month  = 3;
              changeColor( (Button) v.findViewById(R.id.in3) );
              break;

          case R.id.in4 :
              month  = 4;
              changeColor( (Button) v.findViewById(R.id.in4) );
              break;

          case R.id.in5 :
              month  = 5;
              changeColor( (Button) v.findViewById(R.id.in5) );
              break;

          case R.id.in6 :
              month  = 6;
              changeColor( (Button) v.findViewById(R.id.in6) );
              break;

          case R.id.in7 :
              month  = 7;
              changeColor( (Button) v.findViewById(R.id.in7) );
              break;

          case R.id.in8 :
              month  = 8;
              changeColor( (Button) v.findViewById(R.id.in8) );
              break;

          case R.id.in9 :
              month  = 9;
              changeColor( (Button) v.findViewById(R.id.in9) );
              break;

          case R.id.in12 :
              month  = 12;
              changeColor( (Button) v.findViewById(R.id.in12) );
              break;

      }
  }


    public void changeColor(Button btn){

        if( month ==  Integer.parseInt( nowIn.getText().toString() )){


        }else {
            selBtn = btn;
            btn.setTextColor(ContextCompat.getColor(context, R.color.color_green));
            btn.setBackgroundResource(R.drawable.btn_gray);



            nowIn.setTextColor(ContextCompat.getColor(context, R.color.color_gray));
            nowIn.setBackgroundResource(R.color.color_gray_dark);
            nowIn = btn;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mainApp = (MainApp)getApplicationContext();
        context = getApplicationContext();


        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("결제 정보");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }
        payActivity = (PayActivity) this;

        intent = getIntent();
        item = (Item)intent.getParcelableExtra("item");
        log = (String) intent.getStringExtra("log");


        appr_num = (TextView) findViewById(R.id.appr_num);
        appr_date = (TextView) findViewById(R.id.appr_date);
        tran_serialno = (TextView)findViewById(R.id.tran_serialno);
        installment_val = (TextView) findViewById(R.id.installment_val) ;



        tran_type ="credit";



        if( log != null) {

            cancel = true;

            tran_type ="credit_cancel";


            LinearLayout in_lay = findViewById(R.id.in_lay);
            in_lay.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = new JSONObject(log);

                appr_num.setText(jsonObject.getString("APPROVAL_NUM"));
                appr_date.setText(jsonObject.getString("APPROVAL_DATE"));
                tran_serialno.setText(jsonObject.getString("TRAN_SERIALNO"));
                installment_val.setText(jsonObject.getString("INSTALLMENT"));

                installmentVal = jsonObject.getString("INSTALLMENT");

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{

            installment_val_lay = (LinearLayout) findViewById(R.id.installment_val_lay);
            appr_num_lay = (LinearLayout) findViewById(R.id.appr_num_lay);
            appr_date_lay = (LinearLayout) findViewById(R.id.appr_date_lay);
            tran_serialno_lay = (LinearLayout) findViewById(R.id.tran_serialno_lay);


            installment_val_lay.setVisibility(View.GONE);
            appr_num_lay.setVisibility(View.GONE);
            appr_date_lay.setVisibility(View.GONE);
            tran_serialno_lay.setVisibility(View.GONE);

        }

        if(item.type.equals("O")){
            order = (Order) item;
        }else{
            adver = (Adver) item;
        }

        nowIn = (Button) findViewById(R.id.in0);
        selBtn = (Button) findViewById(R.id.in0);

        in0 = (Button) findViewById(R.id.in0);
        in2 = (Button) findViewById(R.id.in2);
        in3 = (Button) findViewById(R.id.in3);
        in4 = (Button) findViewById(R.id.in4);
        in5 = (Button) findViewById(R.id.in5);
        in6 = (Button) findViewById(R.id.in6);
        in7 = (Button) findViewById(R.id.in7);
        in8 = (Button) findViewById(R.id.in8);
        in9 = (Button) findViewById(R.id.in9);
        in12 = (Button) findViewById(R.id.in12);

        in0.setOnClickListener(this);
        in2.setOnClickListener(this);
        in3.setOnClickListener(this);
        in4.setOnClickListener(this);
        in5.setOnClickListener(this);
        in6.setOnClickListener(this);
        in7.setOnClickListener(this);
        in8.setOnClickListener(this);
        in9.setOnClickListener(this);
        in12.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner01);

        appr_num_lay = (LinearLayout) findViewById(R.id.appr_num_lay);
        appr_date_lay = (LinearLayout) findViewById(R.id.appr_date_lay);
        tran_serialno_lay = (LinearLayout) findViewById(R.id.tran_serialno_lay);
        sign_flag_lay = (LinearLayout) findViewById(R.id.sign_flag_lay);

        tid_lay = (LinearLayout) findViewById(R.id.tid_lay);
        biznum_lay = (LinearLayout) findViewById(R.id.biznum_lay);
        name_lay= (LinearLayout) findViewById(R.id.name_lay);
        owner_lay= (LinearLayout) findViewById(R.id.owner_lay);
        address_lay= (LinearLayout) findViewById(R.id.address_lay);
        tel_lay= (LinearLayout) findViewById(R.id.tel_lay);

        add_field_lay = (LinearLayout) findViewById(R.id.add_field_lay);
        order_num_lay = (LinearLayout) findViewById(R.id.order_num_lay);
        customer_code_lay = (LinearLayout) findViewById(R.id.customer_code_lay);

        tranno = (TextView) findViewById(R.id.appcall_tran_no);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        amount = (TextView) findViewById(R.id.amount);
        amount.setText( decimalFormat.format( order.gettPrice() ));

        taxLay = (LinearLayout) findViewById(R.id.tax_lay);
        tax = (TextView) findViewById(R.id.tax);
        tax.setText("0");

        tip = (EditText)findViewById(R.id.tip);
        installment = (EditText)findViewById(R.id.installment);

        order_num = (EditText)findViewById(R.id.order_num);
        customer_code = (EditText)findViewById(R.id.customer_code);
        add_field = (EditText)findViewById(R.id.add_field);

        receipt_email = (EditText)findViewById(R.id.receipt_email);
        receipt_sms = (TextView)findViewById(R.id.receipt_sms);


        tid = (TextView)findViewById(R.id.tid);
        tid.setText(order.getsTid());


        biznum = (EditText)findViewById(R.id.biznum);
        biznum.setText(order.getsNumber() );

        name = (TextView) findViewById(R.id.name);
        name.setText( order.getsName() );

        owner = (TextView) findViewById(R.id.owner);
        owner.setText( order.getsOwner() );

        address = (TextView) findViewById(R.id.address);
        address.setText( order.getsRoad() );

        tel = (TextView) findViewById(R.id.tel);
        tel.setText( order.getsTel() );



        sign_flag = (EditText)findViewById(R.id.sign_flag);

        keyin_flag = (EditText)findViewById(R.id.keyin_flag);

        print_option = (EditText)findViewById(R.id.print_option);



        SimpleDateFormat formater = new SimpleDateFormat("yyMMddHHmmss", Locale.KOREA);
        Date current = new Date();
        tranno.setText(formater.format(current));

        adspin = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tranType = parent.getItemAtPosition(position).toString();

                if (position == 3 || position == 4 || position == 5 || position == 10){
                    appr_num_lay.setVisibility(View.VISIBLE);
                    appr_date_lay.setVisibility(View.VISIBLE);
                    tran_serialno_lay.setVisibility(View.VISIBLE);
                    sign_flag_lay.setVisibility(View.VISIBLE);
                } else if (position == 7){
                    appr_num_lay.setVisibility(View.VISIBLE);
                    appr_date_lay.setVisibility(View.VISIBLE);
                } else {
                }
                tran_type = adspin.getItem(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });




        goodLay =(LinearLayout) findViewById(R.id.good_lay);
        deliveryLay = (LinearLayout) findViewById(R.id.delivery_lay);
        inLay = (LinearLayout) findViewById(R.id.in_lay);

        goodPrice = (TextView) findViewById(R.id.good_pric);
        deliveryPrice = (TextView) findViewById(R.id.delivery_price);


        if(cancel){


            cancelBtn = findViewById(R.id.cancel_btn);
            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardTranData();
                }
            });

        }else {

            if(order.getPayType() == 1){

                goodLay.setVisibility(View.VISIBLE);
                deliveryLay.setVisibility(View.VISIBLE);
                inLay.setVisibility(View.GONE);

                goodPrice.setText( decimalFormat.format(order.getgPrice()));
                deliveryPrice.setText( decimalFormat.format(order.getdPrice()));

                cashBtn = (Button)findViewById(R.id.cash_btn);
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
                cardBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        payType = 2;
                        cardTranData();

                    }
                });

            }else if( order.getPayType() == 2) {

                goodLay.setVisibility(View.GONE);
                deliveryLay.setVisibility(View.GONE);
                inLay.setVisibility(View.VISIBLE);

                cashBtn = (Button)findViewById(R.id.cash_btn);
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
                cardBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        payType = 2;
                        cardTranData();

                    }
                });

            }else if(order.getPayType() == 3){


                goodLay.setVisibility(View.GONE);
                deliveryLay.setVisibility(View.GONE);
                inLay.setVisibility(View.GONE);

                amount.setText("0");

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


        dialog = new Dialog(PayActivity.this);
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

    }




    protected void onResume(){
        super.onResume();

        multi = true;
        input_addfield = false;


    }



    public void cardTranData(){

        ComponentName compName = new ComponentName("kr.co.kicc.ecm","kr.co.kicc.ecm.SmartCcmsMain");

        Intent intent = new Intent(Intent.ACTION_MAIN);


        if("print".equals(tran_type)){

            String printData = "";
            printData += AL + X1 + "============================================\r\n";
            printData += AL + X1 + " No  품명                     수량      금액\r\n";
            printData += AL + X1 + "============================================\r\n";
            printData += AL + X1 + " 1   [일화]맥콜/뚱캔           72     36,360\r\n";
            printData += AL + X1 + " 2   [코카콜라]암바사/뚱캔     96     45,120\r\n";
            printData += AL + X1 + " 3   [매일유업]흰우유          48     26,400\r\n";
            printData += AL + X1 + "--------------------------------------------\r\n";
            printData += AL + X1 + "--------------------------------------------\r\n";
            printData += LF;
            printData += LF;

            intent.putExtra("TRAN_TYPE", "print");
            intent.putExtra("PRINT_DATA", printData);
            intent.putExtra("PRINT_OPTION", this.print_option.getText().toString());

            Log.d(TAG, "PRINT_DATA [" + printData+ "]" ) ;

        } else {
            intent.putExtra("TRAN_NO", "1");
            intent.putExtra("APPCALL_TRAN_NO", this.tranno.getText().toString());
            intent.putExtra("TRAN_TYPE", tran_type);


            intent.putExtra("TOTAL_AMOUNT", this.amount.getText().toString().replace("," , "")  );

            intent.putExtra("TAX",this.tax.getText().toString());
            intent.putExtra("TIP",this.tip.getText().toString());

            intent.putExtra("RECEIPT_EMAIL",this.receipt_email.getText().toString());
            intent.putExtra("RECEIPT_SMS",this.receipt_sms.getText().toString());



            if(cancel){
                intent.putExtra("INSTALLMENT", installmentVal);
            }else {
                intent.putExtra("INSTALLMENT", selBtn.getText().toString());
            }

            intent.putExtra("SIGN_FLAG", this.sign_flag.getText().toString());
            intent.putExtra("KEYIN_FLAG", this.keyin_flag.getText().toString());
            intent.putExtra("PRINT_OPTION", this.print_option.getText().toString());

            if(multi){
                intent.putExtra("SHOP_TID", this.tid.getText().toString());  //tid
                intent.putExtra("SHOP_BIZ_NUM",this.biznum.getText().toString());  // 사업자번호
                intent.putExtra("SHOP_NAME",this.name.getText().toString());
                intent.putExtra("SHOP_OWNER",this.owner.getText().toString());
                intent.putExtra("SHOP_ADDRESS",this.address.getText().toString());
                intent.putExtra("SHOP_TEL",this.tel.getText().toString());
            }

            if(input_addfield){
                intent.putExtra("ADD_FIELD",this.add_field.getText().toString());
            }else{
                intent.putExtra("ORDER_NUM",this.order_num.getText().toString());
                intent.putExtra("CUSTOMER_CODE",this.customer_code.getText().toString());
            }

            if("credit_cancel".equals(tran_type)||"p_cash_cancel".equals(tran_type)||"c_cash_cancel".equals(tran_type)||"v_cash_cancel".equals(tran_type)){
                intent.putExtra("TRAN_SERIALNO",this.tran_serialno.getText().toString());
            }

            if("credit_cancel".equals(tran_type)||"p_cash_cancel".equals(tran_type)||"c_cash_cancel".equals(tran_type)||"v_cash_cancel".equals(tran_type)||"history".equals(tran_type)){
                intent.putExtra("APPROVAL_NUM",this.appr_num.getText().toString());
                intent.putExtra("APPROVAL_DATE",this.appr_date.getText().toString());
            }
        }

        makeSendFirstMessage();

        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(compName);
        startActivityForResult(intent, 1);
    }



    private void makeCashMessage(){

        map.put("TOTAL_AMOUNT",  String.valueOf(  order.gettPrice() ) );
        map.put("TAX", tax.getText().toString() );
        map.put("TIP", tip.getText().toString() );



        map.put("SHOP_BIZ_NUM", order.getsNumber() );
        map.put("SHOP_NAME", order.getsName() );
        map.put("SHOP_OWNER", order.getsOwner() );
        map.put("SHOP_ADDRESS", order.getsRoad() );
        map.put("SHOP_TEL", order.getsPhone());


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        map.put("APPROVAL_DATE", simpleDateFormat.format(date));
    }

    private void makeSendFirstMessage(){




        map.put("TRAN_NO", "");
        map.put("APPCALL_TRAN_NO", tranno.getText().toString());
        map.put("TRAN_TYPE", tranType );
        map.put("CARD_NUM", "");
        map.put("CARD_NAME", "");
        map.put("TOTAL_AMOUNT", amount.getText().toString() );
        map.put("TAX", tax.getText().toString() );
        map.put("TIP", tip.getText().toString() );



        map.put("INSTALLMENT", selBtn.getText().toString() );

        map.put("RESULT_CODE", "");
        map.put("RESULT_MSG", "");
        map.put("APPROVAL_NUM", appr_num.getText().toString());
        map.put("ORG_APPROVAL_NUM", "");
        map.put("APPROVAL_DATE", appr_date.getText().toString());
        map.put("ACQUIRER_CODE", "");
        map.put("ACQUIRER_NAME", "");
        map.put("ORDER_NUM", order_num.getText().toString() );
        map.put("SHOP_TID", tid.getText().toString() );
        map.put("SHOP_BIZ_NUM", biznum.getText().toString() );
        map.put("SHOP_NAME", name.getText().toString() );
        map.put("SHOP_OWNER", owner.getText().toString() );
        map.put("SHOP_ADDRESS", address.getText().toString() );
        map.put("SHOP_TEL", tel.getText().toString());
        map.put("MERCHANT_NUM", "");
        map.put("CUSTOMER_CODE", customer_code.getText().toString());
        map.put("ADD_FIELD",  add_field.getText().toString());
        map.put("TRAN_SERIALNO", tran_serialno.getText().toString() );
        map.put("RESPONSE_TYPE", "");



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

            JSONObject jsonObject = new JSONObject(map);

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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        rIntent = data;


        RESULT_CODE = data.getStringExtra("RESULT_CODE");
        map.clear();
        map.put("TRAN_NO", data.getStringExtra("TRAN_NO"));
        map.put("APPCALL_TRAN_NO", data.getStringExtra("APPCALL_TRAN_NO"));
        map.put("TRAN_TYPE", data.getStringExtra("TRAN_TYPE"));
        map.put("CARD_NUM", data.getStringExtra("CARD_NUM"));
        map.put("CARD_NAME", data.getStringExtra("CARD_NAME"));
        map.put("TOTAL_AMOUNT", data.getStringExtra("TOTAL_AMOUNT"));
        map.put("TAX", data.getStringExtra("TAX"));
        map.put("TIP", data.getStringExtra("TIP"));
        map.put("INSTALLMENT", data.getStringExtra("INSTALLMENT"));
        map.put("RESULT_CODE",  data.getStringExtra("RESULT_CODE"));
        map.put("RESULT_MSG", data.getStringExtra("RESULT_MSG"));
        map.put("APPROVAL_NUM", data.getStringExtra("APPROVAL_NUM"));
        map.put("ORG_APPROVAL_NUM", "");
        map.put("APPROVAL_DATE", data.getStringExtra("APPROVAL_DATE"));
        map.put("ACQUIRER_CODE", data.getStringExtra("ACQUIRER_CODE"));
        map.put("ACQUIRER_NAME", data.getStringExtra("ACQUIRER_NAME"));
        map.put("ORDER_NUM", data.getStringExtra("ORDER_NUM"));

        map.put("SHOP_TID", data.getStringExtra("SHOP_TID"));
        map.put("SHOP_BIZ_NUM", data.getStringExtra("SHOP_BIZ_NUM"));
        map.put("SHOP_NAME", data.getStringExtra("SHOP_NAME"));
        map.put("SHOP_OWNER",  data.getStringExtra("SHOP_OWNER"));
        map.put("SHOP_ADDRESS",  data.getStringExtra("SHOP_ADDRESS"));
        map.put("SHOP_TEL", data.getStringExtra("SHOP_TEL"));

        map.put("MERCHANT_NUM", data.getStringExtra("MERCHANT_NUM"));
        map.put("CUSTOMER_CODE", data.getStringExtra("CUSTOMER_CODE"));
        map.put("ADD_FIELD", data.getStringExtra("ADD_FIELD"));
        map.put("TRAN_SERIALNO", data.getStringExtra("TRAN_SERIALNO"));
        map.put("RESPONSE_TYPE",  data.getStringExtra("RESPONSE_TYPE"));


        if(resultCode==RESULT_OK) {
            if(requestCode==1) {

                payTrans();
            }

        } else if (resultCode==RESULT_CANCELED){

            payTrans();

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
                            payActivity.finish();
                        }
                    }else {

                        if (jsonObject.getString("payok").equals("true")) {

                            item.status = 7;

                            mainApp.gpsUpdateCnt = 0;

                            mainApp.locationList.clear();

                            int parentKey = mainApp.parentKey;

                            int key = item.key;

                            int userKey = mainApp.userKey;


                            String obj = " { 'from' :'" + userKey + "',  'to':'b" + parentKey + "' ,  'room' :'b" + parentKey + "' ,  'msg': 'finish', 'data':'" + key + "', 'action':'finish'   }";

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

                            amountTv.setText(rIntent.getStringExtra("TOTAL_AMOUNT"));

                            shopNameTv.setText(rIntent.getStringExtra("SHOP_NAME"));

                            resultMsgTv.setText(rIntent.getStringExtra("RESULT_MSG"));

                            resultCodeTv.setText(rIntent.getStringExtra("RESULT_CODE"));

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
        RequestQueue queue = Volley.newRequestQueue(PayActivity.this);
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
                map.put("TRAN_TYPE", "cash" );
                makeCashMessage();
            }else if(payType == 2){
                parameters.put("payType", String.valueOf(payType) );
                parameters.put("cash", "0");
                parameters.put("card", String.valueOf(order.gettPrice()));
            }else if( payType == 3){
                parameters.put("payType", String.valueOf( payType) );
                parameters.put("cash", "0");
                parameters.put("card", "0");

                map.put("TRAN_TYPE", "prepay" );
                makeCashMessage();
            }

            JSONObject jsonObject = new JSONObject(map);
            parameters.put("totalMsg", jsonObject.toString() );

            Log.d("route" , mainApp.mainActivity.getRoute().toString());
            parameters.put("route" , mainApp.mainActivity.getRoute().toString() );




        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }

}





package com.newkoad.deliver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.Map;

public class ReceiveActivity extends Addition {

    private static final String TAG ="ReceiveActivity" ;

    ReceiveActivity receiveActivity;
    protected String type;
    protected Item item;
    protected Order order;
    protected Adver adver;

    private Dialog confirmDialog;
    protected Button allocateBtn , cancelBtn;

    TextView storeTv;


    TextView sRoadTv, sLandTv;
    TextView eRoadTv, eLandTv;
    TextView gPriceTv, dPriceTv , payTypeTv ;
    TextView rsDistanceTv;
    TextView seDistanceTv;
    TextView datetimeTv , passTimeTv , requireTimeTv;

    ProgressBar progressBar;



    protected ImageButton kakaoTalkBtn,  naviStoreBtn ,  naviCustomerBtn;
    protected ImageButton  phoneBranchBtn, phoneStoreBtn, phoneCustomerBtn ;
    protected ImageButton smsBranchBtn, smsCustomerBtn;  // branch
    protected ImageButton mapBtn;

    protected DecimalFormat formatter = new DecimalFormat("###,###");

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainApp.setOrderKey(0);
        mainApp.receiveActivity = null;
    }

    private void setDateOrder(final Order order){


        final String sPhone = order.getsPhone();

        phoneStoreBtn = (ImageButton) findViewById(R.id.phone_store_btn);
        phoneStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + order.getsPhone());
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
            }
        });

        phoneCustomerBtn = (ImageButton) findViewById(R.id.phone_customer_btn);
        phoneCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + order.getePhone());
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
            }
        });

        smsCustomerBtn = (ImageButton) findViewById(R.id.sms_customer_btn);
        smsCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("smsto:" + order.getePhone());

                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "-");
                startActivity(intent);
            }
        });



        storeTv = (TextView)findViewById(R.id.store_tv);
        storeTv.setText( order.getsName() );


        sRoadTv = (TextView)findViewById(R.id.s_road_tv);
        sRoadTv.setText( order.getsRoad());

        //sLandTv = (TextView)findViewById(R.id.s_land_tv);
        //sLandTv.setText( order.getsLand());


        eRoadTv = (TextView)findViewById(R.id.e_road_tv);
        eRoadTv.setText( "도로명 : "+order.geteRoad() );

        eRoadTv = (TextView)findViewById(R.id.e_memo);
        eRoadTv.setText( "고객요구사항 : "+order.geteMemo() );

        eLandTv = (TextView)findViewById(R.id.e_land_tv);
        eLandTv.setText( "번지  : "+order.geteLand() );



        gPriceTv = (TextView)findViewById(R.id.gprice_tv);

        gPriceTv.setText( formatter.format(order.gettPrice() ) );

        dPriceTv = (TextView)findViewById(R.id.dprice_tv);

        dPriceTv.setText( formatter.format(order.getdPrice() )  );


        payTypeTv = (TextView) findViewById(R.id.pay_type_tv);

        payTypeTv.setText(order.getPayTypeStr());


        rsDistanceTv = (TextView) findViewById(R.id.rs_distance_tv);
        Location startLoc = new Location("point A");
        startLoc.setLatitude(mainApp.latitude);
        startLoc.setLongitude(mainApp.longitude);
        Location endLoc = new Location("point B");
        String spoints = order.sPoints;
        String[] spArray = spoints.split(",");
        if(spArray.length == 2) {
            endLoc.setLatitude( Double.parseDouble(spArray[0]) );
            endLoc.setLongitude( Double.parseDouble(spArray[1]) );
        }
        rsDistanceTv.setText(String.format( "%.1f",  startLoc.distanceTo(endLoc)  / 1000 )  + "km"  );


        seDistanceTv = (TextView) findViewById(R.id.se_distance_tv);
        seDistanceTv.setText( String.valueOf( order.getDistance() ) + "km" );



        naviStoreBtn = (ImageButton) findViewById(R.id.navi_store_btn);
        naviStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NaviTmapActivity.class);
                intent.putExtra("target", "s");
                intent.putExtra("order", order);
                startActivity(intent);
            }
        });


        naviCustomerBtn = (ImageButton) findViewById(R.id.navi_customer_btn);
        naviCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NaviTmapActivity.class);
                intent.putExtra("target", "c");
                intent.putExtra("order", order);
                startActivity(intent);

            }
        });


    }


    private void setDateAdver(Adver adver){


    }

    private void setDateTime(Order order){

        int min = 0;
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        progressBar.setMax(order.getrTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Date curDate , reqDate ;
        long diff = 0 ;


        SimpleDateFormat dsdf = new SimpleDateFormat("mm");
        String str ="";
        long minute  = 0 ;
        Date gapDate;
        try {


            reqDate = sdf.parse(order.getDatetime());
            long reqDateTime = reqDate.getTime();

            curDate = sdf.parse(currentDateandTime);
            long curDateTime = curDate.getTime() ;

            minute = (curDateTime - reqDateTime) / 60000;


        }catch (Exception e){

        }



        progressBar.setProgress( (int) minute );


        datetimeTv = (TextView)findViewById(R.id.datetime_tv);
        datetimeTv.setText( order.getDatetime()  );


        passTimeTv = (TextView)findViewById(R.id.pass_time_tv);
        passTimeTv.setText(  String.valueOf(minute));

        requireTimeTv = (TextView)findViewById(R.id.require_time_tv);
        requireTimeTv.setText(  String.valueOf( order.getrTime() ) );


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        intent = getIntent();
        item = (Item)intent.getParcelableExtra("item");

        if(item.type.equals("O")){
            order = (Order) item;
            setDateOrder(order);
            setDateTime(order);
        }else{
            adver = (Adver) item;
            setDateAdver(adver);
        }




        mapBtn =(ImageButton) findViewById(R.id.map_btn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MapWebDaumActivity.class);
                intent.putExtra("order", order);
                startActivity(intent);
            }
        });


        headerTitle = (TextView) findViewById(R.id.header_title);
        receiveActivity = (ReceiveActivity) this;
        mainApp.receiveActivity = (ReceiveActivity) this;

        mainApp.setOrderKey(order.key);

        headerTitle.setText("접수" );

        headerCloseBtn =findViewById(R.id.header_closeBtn);
        mainApp.act = false;


        allocateBtn =(Button) findViewById(R.id.allocateBtn);
        allocateBtn.setVisibility(View.VISIBLE);




        final Button allocateConfirm = (Button) findViewById(R.id.allocateBtn);
        allocateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(mainApp.getAttendTime() != null) {

                    confirmDialog = new Dialog(ReceiveActivity.this);

                    confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    confirmDialog.setContentView(R.layout.dialog_allocate_confirm);

                    TextView title = confirmDialog.findViewById(R.id.title);

                    Button cancel = confirmDialog.findViewById(R.id.cancel_btn);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDialog.cancel();
                        }
                    });

                    Button ok = confirmDialog.findViewById(R.id.ok_btn);



                    if (mainApp.getCallLimit() > mainApp.getAllocateCnt()) {

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                allocate();
                            }
                        });

                    } else {
                        title.setText(" 더 이상 배차 할수 없음");
                        ok.setVisibility(View.GONE);
                        cancel.setText(" 닫 기 ");
                    }

                    confirmDialog.show();

                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveActivity.this);
                    builder.setTitle( " 경 고 " )
                            .setMessage(  " 현재 일을 시작하지 않았습니다. ")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    confirmDialog.cancel();
                                }
                            })
                            .setNegativeButton("닫기", null)
                            .create()
                            .show();



                }
            }
        });



        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mainApp.receiveActivity != null){

                }

                finish();
            }
        });





        if(mainApp.getUserGroup().equals("B")){

            LinearLayout header = (LinearLayout) findViewById(R.id.header);
            header.setBackgroundResource( R.color.colorMenuOffMgr );


            LinearLayout menuManager = (LinearLayout) findViewById(R.id.menu_manager);


            LinearLayout actLayout = findViewById(R.id.act_layout);
            actLayout.setVisibility(View.GONE);
        }




    }


    private void allocate(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {

                            openAlert(jsonObject.getString("msg"));

                            receiveActivity.finish();

                        }
                    }else {

                        if( jsonObject.getString("allocate").equals("true") ){

                            item.status = 4;

                            mainApp.locationList.clear();

                            mainApp.gpsUpdateCnt = 0;

                            mainApp.mainActivity.startLocation();


                            int parentKey = mainApp.parentKey;

                            int key = item.key;

                            int userKey = mainApp.userKey;


                            String obj = " { 'from' :'"+ userKey +"',  'to':'b"+ parentKey +"' ,  'room' :'b"+ parentKey +"' ,  'msg': 'allocate', 'data':'" + order.getsKey() +"', 'action':'allocate'   }" ;

                            JSONObject json1 = new JSONObject( obj);

                            mainApp.mainActivity.sendMsg(  json1.toString() );

                            String obj2 = " { 'from' :'"+ userKey +"',  'to':'"+ mainApp.getBoxID() +"' ,   'msg': 'play', 'data':'" + order.getsKey() +"', 'action':'play'   }" ;

                            JSONObject json2 = new JSONObject( obj2);

                            mainApp.mainActivity.sendMsg(  json2.toString() );


                            mainApp.mainActivity.doAllocate( userKey );


                            mainApp.setAllocateCnt( mainApp.getAllocateCnt() + 1 );


                            intent = new Intent(ReceiveActivity.this, AllocateActivity.class );

                            if( item.type.equals("A") ) {
                                intent.putExtra("item", adver);
                            }else{
                                intent.putExtra("item", order);
                            }

                            mainApp.getSoundManager().play("a_ok");
                            confirmDialog.cancel();
                            ReceiveActivity.this.startActivityForResult(intent, 0);

                        }else {



                            AlertDialog.Builder builder = new AlertDialog.Builder(ReceiveActivity.this);
                            builder.setTitle( jsonObject.getString("msg") )
                                    .setMessage(  " ")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            confirmDialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("닫기", null)
                                    .create()
                                    .show();

                        }
                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };



        AllocateRequest allocateRequest =new AllocateRequest( item.key, responseListener);


        allocateRequest.setShouldCache(false);


        RequestQueue queue = Volley.newRequestQueue(ReceiveActivity.this);


        queue.add(allocateRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode){
            case 0:
                if(mainApp.act == true){
                    mainApp.mainActivity.selectTab(0);
                }else {
                    mainApp.mainActivity.selectTab(1);
                }
                finish();
        }
    }



    class AllocateRequest extends StringRequest {

        private Map<String, String> parameters;
        public AllocateRequest(int itemKey,  Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() +"/1receive/receive_allocate.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ));

            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ));
            parameters.put("key", String.valueOf( itemKey ) );
            parameters.put("token", mainApp.getTokenp());
            parameters.put("aID", mainApp.getAndroidID());


            parameters.put("dayLimit", String.valueOf(mainApp.getDayLimit()) );

            parameters.put("spointx",String.valueOf(mainApp.latitude) );
            parameters.put("spointy",String.valueOf(mainApp.longitude) );

        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


    class DriverSearchRequest extends StringRequest{
        final static private String URL = "";
        private Map<String, String> parameters;

        public DriverSearchRequest(String play,  Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("play", play);
            //parameters.put("userPW", userPW);
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


    class DriverAppleyRequest extends StringRequest{
        final static private String URL = "";


        private Map<String, String> parameters;

        public DriverAppleyRequest(String play,  Response.Listener<String> listener) {


            super(Method.POST, URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("play", play);

        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


}








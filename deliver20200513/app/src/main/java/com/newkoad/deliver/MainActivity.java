package com.newkoad.deliver;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;

import android.util.Log;
import android.view.View;


import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.add.AdditionFragment;

import com.newkoad.deliver.add.SoundManager;

import com.newkoad.deliver.SocketIOService.MyBinder;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity" ;
    private MainApp mainApp;
    private Context context;
    private Context mContext;

    SocketIOService mSocketIOService;
    boolean mSocketIOBound = false;
    Intent intent;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private Intent serviceIntent;


    protected LinearLayout topbar;


    protected BadgeTabLayout tabLayout;

    protected TabLayout.Tab tab0;
    protected TabLayout.Tab tab1;
    protected TabLayout.Tab tab2;
    protected TabLayout.Tab tab3;

    protected ViewPager viewPager;
    protected ViewPagerAdapter adapter;
    protected Dialog doDialog ;

    LinearLayout mainLayout;
    ReceiveFragment receiveFragment;
    AllocateFragment allocateFragment;
    FinishFragment finishFragment;
    AdditionFragment additionFragment;

    Dialog deliveryState;
    TextView branchTv;



    ArrayList<Location> locationList = new ArrayList<>();
    int gpsUpdateCnt = 0;
    LocationManager locationManager;
    LocationListener locationListener;
    String provider;
    boolean isGPSEnable;
    boolean isNetworkEnable;

    SoundManager soundManager = null;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    private BackPressCloseHandler backPressCloseHandler;

    private SharedPreferences appData;
    SharedPreferences.Editor editor;
    private String locationPoints = "";





    @Override
    public void onBackPressed() {

        backPressCloseHandler.onBackPressed();
    }


    public void putBadge(int index, int count){
        if(count == 0 ){
            tabLayout.with(tabLayout.getTabAt(index)).badge(false).build();
        }else{
            tabLayout.with(tabLayout.getTabAt(index)).badge(true).badgeCount(count).build();
        }
    }


    private BroadcastReceiver receiveReceiver = new ReceiveReceiver();
    public class ReceiveReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

           receiveFragment.listUpdate();
        }
    }


    private BroadcastReceiver allocateReceiver = new AllocateReceiver();
    public class AllocateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {


            receiveFragment.listUpdate();

            int forcing = intent.getIntExtra("forcing", 0);
            if(forcing > 0) {
                allocateFragment.listUpdate();
            }
        }
    }


    private BroadcastReceiver cancelReceiver = new CancelReceiver();
    public class CancelReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {


            int orderKey = intent.getIntExtra("orderKey",0);
            int userKey = intent.getIntExtra("userKey",0);

            if(mainApp.getOrderKey() == orderKey){
                if(mainApp.receiveActivity !=  null){  mainApp.receiveActivity.finish(); }
                if(mainApp.allocateActivity != null){ mainApp.allocateActivity.finish(); }
            }

            if( userKey == mainApp.getUserKey() ) {

                allocateFragment.listUpdate();

                try {

                    String obj2 = " { 'from' :'" + userKey + "',  'to':'" + mainApp.getBoxID() + "' ,   'msg': 'stop', 'data':'" + orderKey + "', 'action':'stop'   }";
                    JSONObject json2 = new JSONObject(obj2);
                    mainApp.mainActivity.sendMsg(json2.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle( "["+ orderKey + "] 주문이 취소 되었습니다.")
                        .setMessage(" ")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tab0.select();
                            }
                        })
                        .create()
                        .show();

            }
        }
    }



    public void doAllocate(int from ){

        if(from == mainApp.getUserKey() ){
            receiveFragment.listUpdate();
            allocateFragment.listUpdate();
        }else{
            receiveFragment.listUpdate();
        }

    }

    public void doPickup(){

        allocateFragment.listUpdate();
    }

    public void doFinish(){
        allocateFragment.listUpdate();
        finishFragment.listUpdate();
    }




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(getApplicationContext(), SocketIOService.class);
        stopService(intent);

        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
        unregisterReceiver(receiveReceiver);
        unregisterReceiver(allocateReceiver);
        unregisterReceiver(cancelReceiver);
    }


    private  void loadAppData(){
        locationPoints = appData.getString("POINTS", "");
    }

    private void saveAppData(long time, double x, double y){

        locationPoints = appData.getString("POINTS", "");
        editor = appData.edit();
        editor.commit();

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }


        mainApp = (MainApp)getApplicationContext();  //mainApp = (MainApp)getApplication();
        backPressCloseHandler = new BackPressCloseHandler(this);


        mainApp.appName ="rider";
        mainApp.mainActivity = this; // getInstance
        context = getApplicationContext();
        mContext = getApplicationContext();


        IntentFilter intentFilter = new IntentFilter("receive_change");
        registerReceiver(receiveReceiver, intentFilter);

        IntentFilter allocateFilter = new IntentFilter("allocate_change");
        registerReceiver(allocateReceiver, allocateFilter);

        IntentFilter cancelFilter = new IntentFilter("cancel_call");
        registerReceiver(cancelReceiver, cancelFilter);






        mainApp.locationList = locationList;
        locationList.clear();

        mainApp.locationList.clear();



        appData = getSharedPreferences("appData", MODE_PRIVATE);
        loadAppData();


        intent = getIntent();
        if (mainApp.getUserKey() == 0  ) {
            mainApp.setUserKey(Integer.parseInt(intent.getStringExtra("userKey")));
            mainApp.setParentKey(Integer.parseInt(intent.getStringExtra("parentKey")));
            mainApp.setActionKey(Integer.parseInt(intent.getStringExtra("userKey")));  // 처음

            mainApp.setSavePointTime(Integer.parseInt(intent.getStringExtra("savePointTime")));


            mainApp.setUserGroup(intent.getStringExtra("userGroup"));
            mainApp.setUserID(intent.getStringExtra("userID"));
            mainApp.setUserPW(intent.getStringExtra("userPW"));
            if (intent.getStringExtra("isBranch").equals("Y")) {
                mainApp.setBranch(true);
            } else {
                mainApp.setBranch(false);
            }

            mainApp.setUserName(intent.getStringExtra("userName"));
            mainApp.setDateTime(intent.getStringExtra("dateTime"));
            mainApp.setTokenp(intent.getStringExtra("tokenp"));
            mainApp.setBoxID(intent.getStringExtra("boxID"));


            mainApp.setCallLimit(Integer.parseInt(intent.getStringExtra("callLimit")));
            mainApp.setDayLimit(Integer.parseInt(intent.getStringExtra("dayLimit")));


            if (intent.getStringExtra("attend").equals("true")) {

                mainApp.setAttend(true);
            } else {

                mainApp.setAttend(false);
            }
        }

        soundManager = SoundManager.getInstance();
        soundManager.init(context);
        soundManager.addAll();

        mainApp.setSoundManager(soundManager);

        //////////////////////////////////////
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        tabLayout = (BadgeTabLayout) findViewById(R.id.tablayout_rider);  // 기본

        // header & main
        if(mainApp.getUserGroup().equals("B")) {  // 지사
            tabLayout.setVisibility(View.GONE); // 기사 감추기
            tabLayout = (BadgeTabLayout) findViewById(R.id.tablayout_mgr); // 변경
            tabLayout.setVisibility(View.VISIBLE);

            branchTv = findViewById(R.id.branch_tv);  // 지사이름
            branchTv.setText(mainApp.getUserName());  // parentName은 관리자


            topbar = (LinearLayout) findViewById(R.id.toolbarTop);
            topbar.setVisibility(View.VISIBLE);

            initViewPager();

        }else {
            tabLayout.setVisibility(View.VISIBLE);


            initViewPager();
        }





        if(mainApp.getAttend()){
        } else{

            doDialog = new Dialog(MainActivity.this);
            doDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            doDialog.setContentView(R.layout.dialog_attend);
            doDialog.show();


            Button attendBtn = (Button) doDialog.findViewById(R.id.attentBtn);
            attendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {



                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if( jsonObject.has("aidresult") ) {
                                    if(jsonObject.getString("aidresult").equals("fail")) {
                                        openAlert(jsonObject.getString("msg"));

                                    }
                                }else {
                                    if (jsonObject.getString("attend").equals("true")) {
                                        mainApp.setAttend(true);
                                        mainApp.setAttendTime(jsonObject.getString("attendTime"));

                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("출근처리결과")
                                                .setMessage(mainApp.getAttendTime() + "  출근 ")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        doDialog.cancel();
                                                        mainLayout.setVisibility(View.GONE);
                                                        viewPager.setVisibility(View.VISIBLE);

                                                    }
                                                })
                                                .setNegativeButton("닫기", null)
                                                .create()
                                                .show();
                                    } else {

                                    }
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    AttendRequest attendRequest =new AttendRequest("start", responseListener);
                    attendRequest.setShouldCache(false);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(attendRequest);
                }
            });

        }




        if( mainApp.getBranch() ){

            LinearLayout branchLay = (LinearLayout) findViewById(R.id.branch_lay);
            branchLay.setVisibility(View.VISIBLE);

            TextView branchTv = (TextView) findViewById(R.id.branch_tv);
            branchTv.setText(mainApp.getParentName());


        }



        if (mSocketIOService.serviceIntent==null) {
            serviceIntent = new Intent(this, SocketIOService.class);
            intent.putExtra("tokenp", mainApp.getTokenp() );
            intent.putExtra("userKey", mainApp.getUserKey());
            startService(serviceIntent);
            if(mSocketIOBound == false ) {
                Log.d( "==========" , mSocketIOBound + "========");
                bindService(serviceIntent, mSocketIOServiceConnection, Context.BIND_AUTO_CREATE);

            }
        } else {
            serviceIntent = SocketIOService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }
        checkPermissionF();
    }





    private void checkPermissionF() {




        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("단말기의 파일쓰기 권한이 필요합니다.\\n계속하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {



                                        requestPermissions(new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.CALL_PHONE
                                        }, 1);
                                    }

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"권한 요청 취소", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();


                } else {

                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE
                    }, 1);

                }
            }else{

                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                provider = locationManager.getBestProvider(new Criteria(), true);
                startLocation();

            }

        } else {


            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            provider = locationManager.getBestProvider(new Criteria(), true);
            startLocation();

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {

            for(int i = 0 ; i < permissions.length ; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    }

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    }
                }
            }

        } else {
            //System.out.println("onRequestPermissionsResult ( 권한 거부) ");
            //Toast.makeText(getApplicationContext(), "요청 권한 거부", Toast.LENGTH_SHORT).show();
        }

    }




    private void callPermission() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);  //GPS
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)  != PackageManager.PERMISSION_GRANTED){
            requestPermissions( new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_COARSE_LOCATION); //NET
        } else {
            isPermission = true;
        }


    }



    public void initViewPager(){

        viewPager.setOffscreenPageLimit(2);  //N 페이지 가있는 경우 setOffscreenPageLimit(N-1)모든 페이지를 메모리에 유지
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.AddFragment( new ReceiveFragment(),"R");
        adapter.AddFragment( new AllocateFragment(), "A");
        adapter.AddFragment( new FinishFragment(), "F");

        adapter.AddFragment( new AdditionFragment(), "Add");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tab0 = tabLayout.getTabAt(0);
        tab1 = tabLayout.getTabAt(1);
        tab2 = tabLayout.getTabAt(2);
        tab3 = tabLayout.getTabAt(3);


        tabLayout.with(tab0).icon(R.drawable.menu_img1);
        tabLayout.with(tab0).text(true).setText("접수").build();
        tabLayout.with(tab0).badge(false).build();

        tabLayout.with(tab1).icon(R.drawable.menu_img2);
        tabLayout.with(tab1).text(true).setText("배차");
        tabLayout.with(tab1).badge(false).build();


        tabLayout.with(tab2).icon(R.drawable.menu_img3);
        tabLayout.with(tab2).text(true).setText("완료");
        tabLayout.with(tab2).badge(false).build();

        tabLayout.with(tab3).icon(R.drawable.menu_img5);
        tabLayout.with(tab3).text(true).setText("내정보");
        tabLayout.with(tab3).badge(false).build();




        receiveFragment = (ReceiveFragment) adapter.getItem(0);
        allocateFragment = (AllocateFragment) adapter.getItem(1);
        finishFragment = (FinishFragment) adapter.getItem(2);
        additionFragment = (AdditionFragment)  adapter.getItem(3);
        //msgNodeFragment = (MsgNodeFragment) adapter.getItem(3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: receiveFragment.getData(); break;
                    case 1: allocateFragment.getData(); break;
                    case 2: finishFragment.getData(); break;
                    case 3: additionFragment.getDataRider(); break;
                }
            }
        });

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#33343a"));


        if(mainApp.getUserGroup().equals("B")){
            tab3.select();
        }


        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
    }

    public String getTimeStr(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm:ss");
        return sdfNow.format(date);
    }

    public void selectTab(int tab){
        switch(tab){
            case 0: tab0.select(); break;
            case 1: tab1.select(); break;
            case 2: tab2.select(); break;
            case 3: tab3.select(); break;
        }
    }





    private ServiceConnection mSocketIOServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder myBinder = (MyBinder) service;
            mSocketIOService = myBinder.getService();
            mSocketIOBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mSocketIOBound = false;
        }

    };




    class AttendRequest extends StringRequest {

        private Map<String, String> parameters;
        public AttendRequest(String play, Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() +"/5add/go_work.php", listener, null);

            Log.d(TAG, "AttendRequest" +  mainApp.getRootURL() );
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

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




    class BranchSetRequest extends StringRequest {
        private Map<String, String> parameters;
        public BranchSetRequest( Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() +"/5add_mgr/branch_set.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf(mainApp.getUserKey()) );
            parameters.put("userPW", "");
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }









    public JSONArray getRoute(){

        // Hello ... ??


        JSONArray jsonArray = new JSONArray();

        for(int i =0; i < mainApp.xList.size() ; i++ ){

            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("x", mainApp.xList.get(i));
                jsonObject.put("y", mainApp.yList.get(i));
                jsonArray.put( jsonObject );
            }catch (Exception e){

            }

        }
        return jsonArray;



    }


    public void stopLocation(){
        locationManager.removeUpdates(locationListener);
    }


    public void startLocation(){

        final long minTime = mainApp.getRoute_time() ;
        final long minDis = mainApp.getRoute_dis() ;
        Log.d("Route_time" , ""+minTime);
        Log.d("Route_dis" , ""+minDis);
        if(locationManager != null){

            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {


                    mainApp.locationList.add(location);


                    long time = location.getTime();

                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    double alt = location.getAltitude() ;

                    float  acc = location.getAccuracy();
                    float speed = location.getSpeed();



                    mainApp.latitude = lat;
                    mainApp.longitude = lon;

                    mainApp.xList.add(String.valueOf(lat));
                    mainApp.yList.add(String.valueOf(lon));


                    Log.d(TAG, " === " + mainApp.xList.get(gpsUpdateCnt)  +  "  " + mainApp.yList.get(gpsUpdateCnt) );
                    gpsUpdateCnt++;





                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            try {

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };


                    SavePointRequest savePointRequest =new SavePointRequest( responseListener);
                    savePointRequest.setShouldCache(false);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(savePointRequest);


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e(TAG, "onStatusChanged : ");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e(TAG, "onProviderEnabled : ");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e(TAG, "onProviderDisabled : ");
                }
            };

            Log.d(TAG, "PackageManager.PERMISSION_GRANTED : " + PackageManager.PERMISSION_GRANTED) ;


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }


            if(isGPSEnable && isNetworkEnable){

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDis, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDis, locationListener);

            }else{


            }


            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null){

                mainApp.latitude = lastKnownLocation.getLatitude();
                mainApp.longitude = lastKnownLocation.getLongitude();


            }




        }
    }


    public void getAppList(){

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mApps = context.getPackageManager().queryIntentActivities( mainIntent, 0);

        for (int i = 0; i < mApps.size(); i++) {

        }

    }

    public void getAllAppList(){
        List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);
        for(ApplicationInfo app : apps) {
            if((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {


            } else {

            }
        }
    }


    public void openAlert(String msg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("")
                .setMessage( msg )
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }






    class SavePointRequest extends StringRequest {

        private Map<String, String> parameters;
        public SavePointRequest( Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() +"/savePointTime.php", listener, null);

            Log.d(TAG, "   savePointTime   " +  mainApp.getRootURL() );
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());

            parameters.put("x", String.valueOf(mainApp.latitude ));
            parameters.put("y", String.valueOf(mainApp.longitude));


        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }




    public void sendMsg(String msg){

        mSocketIOService.sendMsg(msg);
    }



    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;
        private Activity activity;

        public BackPressCloseHandler(Activity context) {

            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();

                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 로그아웃 됩니다.", Toast.LENGTH_SHORT); toast.show();
        }
    }


    @Override
    public void onPause(){
        super.onPause();

    }


}



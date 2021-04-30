package com.newkoad.deliver.add;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.LoginActivity;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.MyFragment;
import com.newkoad.deliver.R;


import com.newkoad.deliver.WebBoardActivity;

import com.newkoad.deliver.common.LoadImage;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class AdditionFragment  extends MyFragment {

    TextView countTv, revenueTv, depositTv , datetimeTv;
    View v;
    Button goWorkBtn , goHomeBtn ;
    Button riderBtn, branchBtn;
    private boolean branch = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = (MainApp) getActivity().getApplication();

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_addition,container,false);



        LinearLayout userSelectLay = (LinearLayout) v.findViewById(R.id.user_select_lay);
        if( mainApp.getBranch()){
            userSelectLay.setVisibility(View.VISIBLE);

            riderBtn = (Button) v.findViewById(R.id.rider_btn);
            riderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    branch =false;
                    mainApp.setAddBranch(false);
                    mainApp.setActionKey(mainApp.userKey);
                    riderBtn.setSelected(true);
                    branchBtn.setSelected(false);
                    getDataRider();

                }
            });

            branchBtn = (Button) v.findViewById(R.id.branch_btn);
            branchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    branch=true;
                    mainApp.setAddBranch(true);
                    mainApp.setActionKey(mainApp.parentKey);
                    riderBtn.setSelected(false);
                    branchBtn.setSelected(true);
                    getDataBranch();
                }
            });
            riderBtn.setSelected(true);
        }else{
            userSelectLay.setVisibility(View.GONE);
        }


        getDataRider();





        TextView userIDTv = v.findViewById(R.id.id_tv);




        userIDTv.setText(mainApp.getUserID());

        TextView boxIDTv = v.findViewById(R.id.box_id_tv);




        boxIDTv.setText(mainApp.getBoxID());

        TextView nameTv = v.findViewById(R.id.name_tv);


        nameTv.setText(mainApp.getUserName());


        ImageView riderIv = v.findViewById(R.id.rider_iv);

        String imgPath =  mainApp.getUserPhoto();


        LoadImage loadImage = new LoadImage(imgPath);


        Bitmap bitmap = loadImage.getBitmap();



        riderIv.setImageBitmap(bitmap);

        countTv = (TextView) v.findViewById(R.id.count_tv);
        revenueTv = (TextView) v.findViewById(R.id.revenue_tv);
        depositTv = (TextView) v.findViewById(R.id.deposit_tv);
        datetimeTv = (TextView) v.findViewById(R.id.datetime_tv);


        TextView parentNameTv = v.findViewById(R.id.parent_name);
        parentNameTv.setText(mainApp.getParentName() + "[" +mainApp.getParentKey() + "]") ;


        goWorkBtn = (Button) v.findViewById(R.id.go_work_btn);
        goHomeBtn = (Button) v.findViewById(R.id.go_home_btn);

        if(mainApp.getAttend()){
            String[] datetimes = mainApp.getAttendTime().split(" ");
            datetimeTv.setText( "출근시간\n" + datetimes[0] +"\n" + datetimes[1]);
            goWorkBtn.setVisibility(View.GONE);

        }else {

        }


        goHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainApp.getAllocateCnt() == 0 ) {
                    goHome();
                }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("배차 존재" )
                                .setMessage(  " ")
                                .setNegativeButton("닫기", null)
                                .create()
                                .show();
                }
            }
        });


        goWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWork();
            }
        });










        Button putBtn = (Button) v.findViewById(R.id.put_btn);
        putBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, MoneyPutActivity.class);
                context.startActivity(intent);
            }
        });




        Button boardOpen = (Button) v.findViewById(R.id.board_open);
        boardOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebBoardActivity.class);

                context.startActivity(intent);
            }
        });


        Button salesStats = (Button) v.findViewById(R.id.get_btn);
        salesStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, MoneyGetActivity.class);
                context.startActivity(intent);
            }
        });


        Button depositManage = (Button) v.findViewById(R.id.myinfo_btn);
        depositManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyInfoActivity.class);
                context.startActivity(intent);
            }
        });



        Button logoutBtn = (Button) v.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mainApp.logout();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);

            }
        });



        Button BranchAlarm = (Button) v.findViewById(R.id.setting_btn);
        BranchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                context.startActivity(intent);
            }
        });



        Button riderDepositBtn = (Button) v.findViewById(R.id.rider_deposit_btn);
        riderDepositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DepositActivity.class);
                context.startActivity(intent);
            }
        });




        return v;

    }


    private void setRider(){

        LinearLayout attendLay = (LinearLayout) v.findViewById(R.id.attend_lay);
        attendLay.setVisibility(View.VISIBLE);


        Button mgrRiderBtn = (Button) v.findViewById(R.id.mgr_rider_btn);
        mgrRiderBtn.setVisibility(View.GONE);

        Button mgrStoreBtn = (Button) v.findViewById(R.id.mgr_store_btn);
        mgrStoreBtn.setVisibility(View.GONE);

        Button mgrCostBoxBtn = (Button) v.findViewById(R.id.mgr_cost_box_btn);
        mgrCostBoxBtn.setVisibility(View.GONE);

        Button mgrCostMgrBtn = (Button) v.findViewById(R.id.mgr_cost_mgr_btn);
        mgrCostMgrBtn.setVisibility(View.GONE);




        Button riderBoxBtn = (Button) v.findViewById(R.id.rider_box_btn);
        riderBoxBtn.setVisibility(View.VISIBLE);
        riderBoxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BoxActivity.class);
                context.startActivity(intent);
            }
        });



        Button riderLteBtn = (Button) v.findViewById(R.id.rider_lte_btn);
        riderLteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LteActivity.class);
                context.startActivity(intent);
            }
        });





    }







    private void setBranch(){



        LinearLayout attendLay = (LinearLayout) v.findViewById(R.id.attend_lay);
        attendLay.setVisibility(View.GONE);

        Button riderBoxBtn = (Button) v.findViewById(R.id.rider_box_btn);
        riderBoxBtn.setVisibility(View.GONE);

        Button boxUpdateBtn = (Button) v.findViewById(R.id.box_update_btn);
        boxUpdateBtn.setVisibility(View.GONE);




        Button mgrRiderBtn = (Button) v.findViewById(R.id.mgr_rider_btn);

        mgrRiderBtn.setVisibility(View.VISIBLE);

        Button mgrStoreBtn = (Button) v.findViewById(R.id.mgr_store_btn);

        mgrStoreBtn.setVisibility(View.VISIBLE);

        Button mgrCostBoxBtn = (Button) v.findViewById(R.id.mgr_cost_box_btn);

        mgrCostBoxBtn.setVisibility(View.VISIBLE);

        Button mgrCostMgrBtn = (Button) v.findViewById(R.id.mgr_cost_mgr_btn);

        mgrCostMgrBtn.setVisibility(View.VISIBLE);

        Button driverManage = (Button) v.findViewById(R.id.mgr_rider_btn);
        driverManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RiderMgrActivity.class);
                context.startActivity(intent);
            }
        });

        Button storeManage = (Button) v.findViewById(R.id.mgr_store_btn);
        storeManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoreMgrActivity.class);
                context.startActivity(intent);
            }
        });

        Button costBoxtBtn = (Button) v.findViewById(R.id.mgr_cost_box_btn);
        costBoxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CostBoxActivity.class);
                context.startActivity(intent);
            }
        });


        Button costMgrBtn = (Button) v.findViewById(R.id.mgr_cost_mgr_btn);
        costMgrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CostMgrActivity.class);
                context.startActivity(intent);
            }
        });

        Button costLteBtn = (Button) v.findViewById(R.id.mgr_cost_lte_btn);
        costLteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CostLteActivity.class);
                context.startActivity(intent);
            }
        });

    }







    public void getDataRider(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if( jsonObject.has("aidresult") ) {

                        if (jsonObject.getString("aidresult").equals("fail")) {

                            mainApp.mainActivity.openAlert(jsonObject.getString("msg"));
                        }
                    }else {

                        countTv.setText(jsonObject.getString("count").toString());
                        revenueTv.setText(jsonObject.getString("revenue").toString());
                        depositTv.setText(jsonObject.getString("deposit").toString());


                        setRider();
                    }
                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        RiderRequest riderRequest = new RiderRequest(responseListener);
        riderRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(riderRequest);
    }



    class RiderRequest extends StringRequest {

        private Map<String, String> parameters;

        public RiderRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/addition.php", listener, null);

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









    public void getDataBranch(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {

                            mainApp.mainActivity.openAlert(jsonObject.getString("msg"));
                        }





                    }else {

                        countTv.setText(jsonObject.getString("count").toString());
                        revenueTv.setText(jsonObject.getString("revenue").toString());
                        depositTv.setText(jsonObject.getString("deposit").toString());
                        setBranch();
                    }
                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        BranchRequest branchRequest = new BranchRequest(responseListener);
        branchRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(branchRequest);
    }

    class BranchRequest extends StringRequest {

        private Map<String, String> parameters;
        public BranchRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/addition.php", listener, null);
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










    public void goHome(){



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {






                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {
                            mainApp.mainActivity.openAlert(jsonObject.getString("msg"));
                        }
                    }else {
                        String[] datetimes = jsonObject.getString("datetime").split(" ");
                        datetimeTv.setText("퇴근시간\n" + datetimes[0]+"\n" + datetimes[1]);
                        goHomeBtn.setVisibility(View.GONE);
                        goWorkBtn.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        HomeRequest homeRequest = new HomeRequest(responseListener);
        homeRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(homeRequest);
    }




    class HomeRequest extends StringRequest {

        private Map<String, String> parameters;
        public HomeRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/go_home.php", listener, null);
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



    public void goWork(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {
                            mainApp.mainActivity.openAlert(jsonObject.getString("msg"));
                        }
                    }else {

                        String[] datetimes = jsonObject.getString("datetime").split(" ");

                        datetimeTv.setText("출근시간\n" + datetimes[0] +"\n"+ datetimes[1]);
                        goHomeBtn.setVisibility(View.VISIBLE);
                        goWorkBtn.setVisibility(View.GONE);
                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };



        WorkRequest workRequest = new WorkRequest(responseListener);



        workRequest.setShouldCache(false);




        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());




        queue.add(workRequest);
    }











    class WorkRequest extends StringRequest {

        private Map<String, String> parameters;
        public WorkRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/go_work.php", listener, null);
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


}

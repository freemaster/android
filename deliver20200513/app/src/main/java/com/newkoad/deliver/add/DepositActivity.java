package com.newkoad.deliver.add;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DepositActivity extends Addition {

    private static final String TAG ="DepositActivity" ;
    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();

    Dialog conditionDialog;


    int mYear, mMonth, mDay, mHour, mMinute;
    TextView sDate, eDate;

    Calendar calendar;
    private long minTime =0 , maxTime =0;
    private Date minDate, maxDate;
    DatePicker sDatePicker, eDatePicker;
    DatePickerDialog datePickerDialog;
    String endDateStr;



    TextView addCountTv, addPriceTv, subCcountTv, subPpriceTv;
    String search_type, search_class;

    CheckBox allTypeCk, addCk, subCk;
    CheckBox allClassCk, chargeCk, withdrawCk, adverCk, partCk, deliveryCk, lteCk, mgrCk;

    String resultType = "";
    String resultClass = "";
    String resultTypeStr = "";
    String resultClassStr = "";

    TextView depositTv;
    TextView conditionRefTv, conditionRef2Tv;



    DatePickerDialog.OnDateSetListener sDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    mYear = year; mMonth = monthOfYear;  mDay = dayOfMonth;

                    sDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
                }
            };


    DatePickerDialog.OnDateSetListener eDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;  mMonth = monthOfYear; mDay = dayOfMonth;
                    eDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));

                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        mainApp = (MainApp)getApplicationContext();
        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("예치금 관리");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }


        dataRV = (RecyclerView) findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(context));
        dataRV.setAdapter(dataListAdapter);

        addPriceTv = findViewById(R.id.add_price_tv);
        subPpriceTv = findViewById(R.id.sub_price_tv);



        sDate = (TextView) findViewById(R.id.s_date_et);
        eDate = (TextView) findViewById(R.id.e_date_et);

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
        eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));

        Button todayBtn= findViewById(R.id.today_btn);
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));

            }
        });



        Button oneweekBtn= findViewById(R.id.week_btn);
        oneweekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sDate.setText(get7DayAgoDate(mYear, mMonth, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
            }
        });

        Button onemonthBtn= findViewById(R.id.month_btn);
        onemonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate.setText(String.format("%d-%d-%d", mYear, (mMonth + 1) -1, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
            }
        });


        Button depositListBtn = findViewById(R.id.search_btn);
        depositListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });


        depositTv = findViewById(R.id.deposit_tv);
        conditionRefTv = findViewById(R.id.condition_ref_tv);
        conditionRef2Tv = findViewById(R.id.condition_ref2_tv);

        final Button conditionBtn = findViewById(R.id.condition_btn);
        conditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setConditionRider();
            }
        });


        sDate = (TextView) findViewById(R.id.s_date_et);
        eDate = (TextView) findViewById(R.id.e_date_et);

        calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH)  ;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        sDate.setText(String.format("%d-%d-%d", mYear, mMonth +1, mDay));
        eDate.setText(String.format("%d-%d-%d", mYear, mMonth  +1, mDay));

        sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener sDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear = year; mMonth = monthOfYear;  mDay = dayOfMonth;
                                sDate.setText(String.format("%d-%02d-%02d", mYear,  mMonth + 1, mDay));

                            }
                        };


                datePickerDialog = new DatePickerDialog(DepositActivity.this, sDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener eDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear = year;  mMonth = monthOfYear; mDay = dayOfMonth;
                                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
                            }
                        };
                datePickerDialog = new DatePickerDialog(DepositActivity.this, eDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        getData();

    }

    private String get7DayAgoDate(int year , int month , int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        cal.add(Calendar.DATE, -7);
        java.util.Date weekago = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(weekago);
    }

    private void getData(){
        dataList.clear();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));
                            finish();

                        }
                    }else {

                        depositTv.setText(jsonObject.getString("deposit"));
                        search_class = jsonObject.getString("search_class");
                        search_type = jsonObject.getString("search_type");

                        addPriceTv.setText( jsonObject.getString("add_price") );
                        subPpriceTv.setText( jsonObject.getString("sub_price") );

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject json = jsonArray.getJSONObject(i);
                            dataList.add(  new ItemData(json )   );
                        }

                        dataListAdapter.notifyDataSetChanged();
                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        DataListRequest dataListRequest = new DataListRequest(responseListener);
        dataListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(dataListRequest);
    }



    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit/deposit_list.php", listener, null);
            parameters = new HashMap<>();

            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );

            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());

            parameters.put("startDate", sDate.getText().toString() );

            if( calDateBetweenAandB(  eDate.getText().toString(), sDate.getText().toString() ) > 90 ){

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate;
                try {
                    startDate = format.parse(sDate.getText().toString());
                    calendar.setTime(startDate);
                    calendar.add(Calendar.MONTH, +4);
                    endDateStr =  calendar.get(Calendar.YEAR) +"-" + calendar.get(Calendar.MONTH) + "-"+calendar.get(Calendar.DAY_OF_MONTH);

                }catch (Exception e){
                    e.printStackTrace();
                }
                parameters.put("endDate", endDateStr);
            }else{
                parameters.put("endDate", eDate.getText().toString() );
            }





            parameters.put("searchType",  resultType );
            parameters.put("searchClass",  resultClass);
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }










    class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<ItemData> itemList;
        ItemData itemData;

        class DataListHolder extends RecyclerView.ViewHolder{
            protected LinearLayout itemLayout;
            protected TextView cell1,  cell2 ,  cell3 , cell4;
            public DataListHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                cell1 = (TextView) itemView.findViewById(R.id.cell_1);
                cell2 = (TextView) itemView.findViewById(R.id.cell_2);
                cell3 = (TextView) itemView.findViewById(R.id.cell_3);
                cell4 = (TextView) itemView.findViewById(R.id.cell_4);

            }
        }

        public DataListAdapter(Context mContext, List<ItemData> mList) {
            this.mContext = mContext;
            this.itemList = mList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_deposit_c4, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) itemList.get(i) ;

            dataListHolder.cell1.setText(itemData.datetime.replace(" ", "\n"));
            dataListHolder.cell2.setText(itemData.classx);
            dataListHolder.cell3.setText(itemData.price);
            dataListHolder.cell4.setText(itemData.memo);
            dataListHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }



    public class ItemData {
        public String datetime;
        public String classx = "" ;
        public String price;
        public String memo ;
        public ItemData(JSONObject json){
            try {
                this.datetime = json.getString("datetime");

                switch( json.getInt("class") ) {
                    case 1: this.classx = "충전"; break;
                    case 2: this.classx = "출금"; break;
                    case 3: this.classx = "광고"; break;
                    case 4: this.classx = "리스"; break;
                    case 5: this.classx = "배달료"; break;
                    case 6: this.classx = "LTE"; break;
                    case 7: this.classx = "관리비"; break;
                    case 8: this.classx = "취소수수료"; break;
                    case 9: this.classx = "콜취소"; break;
                    default: this.classx = "" ; break;
                }


                this.price = json.getString("price");

                this.memo= json.getString("memo").equals("null") ? "" : json.getString("memo");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    public void setConditionMgr(){


    }

    public void setConditionRider(){

        conditionDialog = new Dialog(this);
        conditionDialog.setContentView(R.layout.dialog_deposit_condition);
        conditionDialog.setTitle("검색조건 설정");

        allTypeCk = (CheckBox) conditionDialog.findViewById(R.id.all_type_ck);
        addCk = (CheckBox) conditionDialog.findViewById(R.id.add_ck);
        subCk = (CheckBox) conditionDialog.findViewById(R.id.sub_ck);


        allTypeCk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(allTypeCk.isChecked()) {
                    addCk.setChecked(true);
                    subCk.setChecked(true);
                }else{
                    addCk.setChecked(false);
                    subCk.setChecked(false);
                }
            }
        });


        allClassCk = (CheckBox) conditionDialog.findViewById(R.id.all_class_ck);
        chargeCk = (CheckBox) conditionDialog.findViewById(R.id.charge_ck);
        withdrawCk = (CheckBox) conditionDialog.findViewById(R.id.withdraw_ck);
        adverCk = (CheckBox) conditionDialog.findViewById(R.id.adver_ck);
        partCk = (CheckBox) conditionDialog.findViewById(R.id.part_ck);
        deliveryCk = (CheckBox) conditionDialog.findViewById(R.id.delivery_ck);
        lteCk = (CheckBox) conditionDialog.findViewById(R.id.lte_ck);
        mgrCk = (CheckBox) conditionDialog.findViewById(R.id.mgr_ck);

        allClassCk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allClassCk.isChecked()){
                    chargeCk.setChecked(true);
                    withdrawCk.setChecked(true);
                    adverCk.setChecked(true);
                    partCk.setChecked(true);
                    deliveryCk.setChecked(true);
                    lteCk.setChecked(true);
                    mgrCk.setChecked(true);

                }else{
                    chargeCk.setChecked(false);
                    withdrawCk.setChecked(false);
                    adverCk.setChecked(false);
                    partCk.setChecked(false);
                    deliveryCk.setChecked(false);
                    lteCk.setChecked(false);
                    mgrCk.setChecked(false);
                }

            }
        });


        Button okBtn = conditionDialog.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 조건 적용
                resultClass ="";
                resultType = "";
                resultClassStr = "";
                resultTypeStr = "";


                if(addCk.isChecked()){  resultType = resultClass + "P,";   resultTypeStr = resultTypeStr +"적립 ";   }
                if(subCk.isChecked()) { resultType = resultClass + "M,";   resultTypeStr = resultTypeStr +"차감"; }

                if(allTypeCk.isChecked()) { resultClass = "";  resultTypeStr = " [적립/차갑 전체] "; }


                if(chargeCk.isChecked()){  resultClass = resultClass + "1,";   resultClassStr = resultClassStr + "충전 ";   }
                if(withdrawCk.isChecked()) { resultClass = resultClass + "2,";  resultClassStr = resultClassStr + "출금 ";   }
                if(adverCk.isChecked()) { resultClass = resultClass + "3,";    resultClassStr = resultClassStr + "광고 "; }
                if(partCk.isChecked()) { resultClass = resultClass + "4,";    resultClassStr = resultClassStr + "리스 "; } //할부
                if(deliveryCk.isChecked()) { resultClass = resultClass + "5,";    resultClassStr = resultClassStr + "배달 "; }
                if(lteCk.isChecked()) { resultClass = resultClass + "6,";   resultClassStr = resultClassStr + "LTE ";  }
                if(mgrCk.isChecked()) { resultClass = resultClass + "7,";   resultClassStr = resultClassStr + "관리비";  }


                if(allClassCk.isChecked()) {resultClass = "";  resultClassStr =" [분류 전체] ";  }


                conditionRefTv.setText( resultTypeStr  );
                conditionRef2Tv.setText( resultClassStr.replace(" ", ",") );
                conditionDialog.cancel();

            }
        });
        Button noBtn = conditionDialog.findViewById(R.id.no_btn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionDialog.cancel();
            }
        });
        conditionDialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = conditionDialog.getWindow();
        int x = (int)(size.x * 0.9f);
        int y = (int)(size.y * 0.9f);
        window.setLayout(x, y);

    }

    public int calDateBetweenAandB( String dateA, String dateB)
    {

        String date1 = dateA;
        String date2 = dateB;

        int returnVal = 0;

        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date FirstDate = format.parse(date1);
            Date SecondDate = format.parse(date2);


            long calDate = FirstDate.getTime() - SecondDate.getTime();

            long calDateDays = calDate / ( 24*60*60 *1000);

            returnVal = (int) Math.abs(calDateDays) ;



        } catch(Exception e) {

        }

        return returnVal;
    }

}

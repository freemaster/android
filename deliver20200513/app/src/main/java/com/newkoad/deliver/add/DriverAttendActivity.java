package com.newkoad.deliver.add;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.R;
import com.newkoad.deliver.add.Addition;
import com.newkoad.deliver.common.MonthYearPickerDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DriverAttendActivity extends Addition {


    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<DriverAttend> dataList = new LinkedList<>();


    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_attend);

        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("근태관리"     ) ;
        headerCloseBtn =findViewById(R.id.header_closeBtn);

        getData();

        dataRV = (RecyclerView) findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(this));
        dataRV.setAdapter(dataListAdapter);


        final TextView searchMonthTv = findViewById(R.id.search_month);
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        ImageButton calendarBtn = (ImageButton) findViewById(R.id.calendar_btn);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {

                        searchMonthTv.setText( String.format("%d-%d", mYear, mMonth + 1));
                    }
                });
                pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");

            }
        });



    }


    DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                }
            };



    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/5add_mgr/list_mgr_driver_attend.php", listener, null);
            parameters = new HashMap<>();
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
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject json = jsonArray.getJSONObject(0);


                    for(int i = 1; i < jsonArray.length(); i++){
                        json = jsonArray.getJSONObject(i);

                        dataList.add(  new DriverAttend( json )   );
                    }


                    dataListAdapter.notifyDataSetChanged();
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



    class DriverAttend {
        protected String day;
        protected String in;
        protected String out;
        protected int delivery;

        public DriverAttend(JSONObject json){
            try{
                this.day = json.getString("day");
                this.in = json.getString("in");
                this.out = json.getString("out");
                this.delivery = json.getInt ("delivery");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<DriverAttend> mDataList;
        DriverAttend driverAttend;

        class DataListHolder extends RecyclerView.ViewHolder{
            protected LinearLayout itemLayout;
            protected TextView cell1,  cell2 ,  cell3 , cell4, cell5;
            public DataListHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                cell1 = (TextView) itemView.findViewById(R.id.cell_1);
                cell2 = (TextView) itemView.findViewById(R.id.cell_2);
                cell3 = (TextView) itemView.findViewById(R.id.cell_3);
                cell4 = (TextView) itemView.findViewById(R.id.cell_4);

            }
        }

        public DataListAdapter(Context mContext, List<DriverAttend> mList) {
            this.mContext = mContext;
            this.mDataList = mList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_driver_attend, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListAdapter.DataListHolder(v);
            return holder;

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DataListHolder dataListHolder = (DataListHolder) holder;
            driverAttend = (DriverAttend) mDataList.get(i) ;

            dataListHolder.cell1.setText(driverAttend.day);
            dataListHolder.cell2.setText(driverAttend.in );
            dataListHolder.cell3.setText(driverAttend.out);
            dataListHolder.cell4.setText(String.valueOf(driverAttend.delivery));

        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }



}

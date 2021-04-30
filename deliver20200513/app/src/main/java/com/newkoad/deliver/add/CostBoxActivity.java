package com.newkoad.deliver.add;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;
import com.newkoad.deliver.add.Addition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CostBoxActivity extends Addition {

    private static final String TAG ="CostBoxActivity" ;
    private MainApp mainApp ;
    private Context context;


    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();
    private Dialog doDialog ;


    private int cursor = 0;
    private String searchDate ="";
    TextView searchDateTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgr_activity_cost_box);
        mainApp = (MainApp) getApplication();
        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(" 디디박스 할부금");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        setHeaderBlue( (LinearLayout)findViewById(R.id.header) );





        dataRV = (RecyclerView) findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(this));
        dataRV.setAdapter(dataListAdapter);


        searchDateTv = (TextView) findViewById(R.id.search_date_tv);
        searchDateTv.setText(getCurrentDateMonth(0));

        ImageButton prevBtn = (ImageButton) findViewById(R.id.prev_month_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor--;
                searchDate = getCurrentDateMonth(cursor);
                searchDateTv.setText(searchDate);
                getData();
            }
        });
        ImageButton nextBtn = (ImageButton) findViewById(R.id.next_month_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor++;
                searchDate = getCurrentDateMonth(cursor);
                searchDateTv.setText(searchDate);
                getData();

            }
        });
        Button thisBtn = (Button) findViewById(R.id.this_month_btn);
        thisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDateTv.setText(getCurrentDateMonth(0));
            }
        });




        getData();

    }


    public static String getCurrentDateMonth(int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, month);
        Date newDate = calendar.getTime();

        String dateFormat = "yyyy-MM";
        String yearMonth = new SimpleDateFormat(dateFormat).format(newDate);
        return yearMonth;
    }


    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/5add/finance/cost_box.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );

            parameters.put("userKey", String.valueOf( mainApp.getActionKey()) );


            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );

            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );

            parameters.put("userID", mainApp.getUserID() );

            parameters.put("userGroup", mainApp.getUserGroup());

            parameters.put("aID", mainApp.getAndroidID());

            if(!searchDate.equals("")) {
                parameters.put("searchDate", searchDate);
            }

        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


    private void getData(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataList.clear();


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));
                            finish();

                        }
                    }else {

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            dataList.add(new ItemData(json));
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(dataListRequest);
    }



    class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<ItemData> mDataList;
        ItemData itemData;
        DecimalFormat decimalFormat = new DecimalFormat("###,###");

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
                cell5 = (TextView) itemView.findViewById(R.id.cell_5);
            }
        }

        public DataListAdapter(Context mContext, List<ItemData> mList) {
            this.mContext = mContext;
            this.mDataList = mList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_cost_box, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) mDataList.get(i) ;

            dataListHolder.cell1.setText(itemData.name);
            dataListHolder.cell2.setText(itemData.id);
            dataListHolder.cell3.setText(decimalFormat.format(itemData.rentPrice));
            dataListHolder.cell4.setText(String.valueOf(itemData.payDay));

            if( itemData.payResult.equals("완납")){
                dataListHolder.cell5.setTextColor(Color.YELLOW);
            }else{
                dataListHolder.cell5.setTextColor(Color.WHITE);
            }
            dataListHolder.cell5.setText(itemData.payResult);




        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    class ItemData {
        public String name;
        public String id;


        public int rentPrice ;
        public int payDay;


        public String payResult = "";
        public String buyType ;
        public ItemData(JSONObject json){
            try{
                this.name = json.getString("riderName");
                this.id = json.getString("riderId");

                this.rentPrice = json.getInt("rentPrice");
                this.payDay = json.getInt("payDay");

                if(json.getInt("payResult") == 1){
                    payResult ="완납";
                }else{
                    payResult="미납";
                }




            }catch ( JSONException e)       {
                e.printStackTrace();
            }
        }
    }




}

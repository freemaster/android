package com.newkoad.deliver.add;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class BoxActivity extends Addition {

    private static final String TAG ="CAL_LTE_FRG" ;
    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();

    private TextView countTv, moneyTv;
    private TextView okCountTv, okMoneyTv;
    private TextView noCountTv, noMoneyTv;


    private int cursor = 0;
    private String searchDate;
    TextView searchDateTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        mainApp = (MainApp)getApplicationContext();
        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("디디박스 요금");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        if(mainApp.getUserGroup().equals("B")) {
            setHeaderBlue((LinearLayout) findViewById(R.id.header));
        }



        dataRV = (RecyclerView) findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(context));
        dataRV.setAdapter(dataListAdapter);




        countTv = (TextView) findViewById(R.id.count);
        moneyTv = (TextView) findViewById(R.id.money);
        okCountTv = (TextView) findViewById(R.id.ok_count);
        okMoneyTv = (TextView) findViewById(R.id.ok_money);
        noCountTv = (TextView) findViewById(R.id.no_count);
        noMoneyTv = (TextView) findViewById(R.id.no_money);



        searchDateTv = (TextView) findViewById(R.id.search_date_tv);
        searchDateTv.setText(getCurrentDateYear(0));

        ImageButton prevBtn = (ImageButton) findViewById(R.id.prev_month_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor--;
                searchDate = getCurrentDateYear(cursor);
                searchDateTv.setText(searchDate);
                getData();
            }
        });
        ImageButton nextBtn = (ImageButton) findViewById(R.id.next_month_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor++;
                searchDate = getCurrentDateYear(cursor);
                searchDateTv.setText(searchDate);
                getData();

            }
        });

        getData();
    }



    public static String getCurrentDateYear(int year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, year);
        Date newDate = calendar.getTime();

        String dateFormat = "yyyy";
        String yearMonth = new SimpleDateFormat(dateFormat).format(newDate);
        return yearMonth;
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

                        okCountTv.setText(jsonObject.getString("ok_count") + "건");
                        okMoneyTv.setText(jsonObject.getString("ok_money") + "원");
                        noCountTv.setText(jsonObject.getString("no_count") + "건");
                        noMoneyTv.setText(jsonObject.getString("no_money") + "원");

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 1; i < jsonArray.length(); i++) {
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
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(dataListRequest);
    }





    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/finance/cost_box_detail.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("aID", mainApp.getAndroidID());
            parameters.put("search_date", searchDateTv.getText().toString());
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
            protected TextView cell1,  cell2 ,  cell3 ;
            public DataListHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                cell1 = (TextView) itemView.findViewById(R.id.cell_1);
                cell2 = (TextView) itemView.findViewById(R.id.cell_2);
                cell3 = (TextView) itemView.findViewById(R.id.cell_3);

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
            v = LayoutInflater.from(mContext).inflate(R.layout.item_box_rider, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;

        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) itemList.get(i) ;

            dataListHolder.cell1.setText(itemData.payDate);
            dataListHolder.cell2.setText(itemData.payMonth);

            dataListHolder.cell3.setText(itemData.payState);

        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }


    public class ItemData {
        public String payDate;
        public String payMonth;

        public String payState ;
        public ItemData(JSONObject json){
            try {
                this.payDate = json.getString("pay_date");

                this.payMonth = json.getString("pay_month");

                this.payState= json.getString("pay_state");

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


}

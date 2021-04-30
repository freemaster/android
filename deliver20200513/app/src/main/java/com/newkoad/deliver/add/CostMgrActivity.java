package com.newkoad.deliver.add;

import android.app.Dialog;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CostMgrActivity extends Addition {

    private static final String TAG ="CostMgrActivity" ;

    private MainApp mainApp ;
    private Context context;


    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();
    private Dialog doDialog ;


    private DecimalFormat decimalFormat = new DecimalFormat("###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgr_activity_cost_mgr);


        mainApp = (MainApp) getApplication();
        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("가맹점 관리비");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        setHeaderBlue( (LinearLayout)findViewById(R.id.header) );

        dataRV = (RecyclerView) findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(this));
        dataRV.setAdapter(dataListAdapter);


        getData();


    }





    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/5add/finance/cost_mgr.php", listener, null);
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


    private void getData(){



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONObject jsonObject = new JSONObject(response) ;
                    if( jsonObject.has("aidresult") ) {
                        if(jsonObject.getString("aidresult").equals("fail")) {
                            openAlert(jsonObject.getString("msg"));
                            finish();
                        }
                    }else {

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            Log.d(TAG, i + " " + json.toString());
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

        class DataListHolder extends RecyclerView.ViewHolder{
            protected LinearLayout itemLayout;
            protected TextView storeName, storeId, payDay, payResult;
            protected TextView mgrCostTv, mgrTaxTv, mgrTotalTv;

            public DataListHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);

                storeName = (TextView) itemView.findViewById(R.id.store_name);
                storeId = (TextView) itemView.findViewById(R.id.store_id);
                payDay = (TextView) itemView.findViewById(R.id.pay_day);
                payResult = (TextView) itemView.findViewById(R.id.pay_result);

                mgrCostTv = (TextView) itemView.findViewById(R.id.mgr_cost);
                mgrTaxTv = (TextView) itemView.findViewById(R.id.mgr_tax);
                mgrTotalTv = (TextView) itemView.findViewById(R.id.mgr_total);
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
            v = LayoutInflater.from(mContext).inflate(R.layout.item_cost_mgr, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) mDataList.get(i) ;

            dataListHolder.storeName.setText(itemData.name);
            dataListHolder.storeId.setText(itemData.id);
            dataListHolder.payDay.setText(itemData.payDay);
            dataListHolder.payResult.setText(itemData.result);

            dataListHolder.mgrCostTv.setText("관리비:"+ decimalFormat.format(itemData.mgrCost) + "원");
            dataListHolder.mgrTaxTv.setText("세금:" + decimalFormat.format(itemData.mgrTax) + "원");
            dataListHolder.mgrTotalTv.setText("합계:" + decimalFormat.format(itemData.mgrTotal) + "원");

        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    class ItemData {
        public String name;
        public String id;
        public int deposit;
        public int mgrCost;
        public int mgrTax;
        public int mgrTotal;

        public String payDay;
        public String result;

        public ItemData(JSONObject json){
            try{
                this.name = json.getString("mem_username");
                this.id = json.getString("mem_userid");

                this.mgrCost = json.getInt("msd_maintenance_cost") ;
                this.mgrTax = json.getInt("msd_maintenance_tax") ;
                this.mgrTotal = json.getInt("msd_maintenance_total") ;

                this.payDay = json.getString("co_contract_day");
                this.result = json.getString("result");
            }catch ( JSONException e)       {
                e.printStackTrace();
            }
        }
    }



}

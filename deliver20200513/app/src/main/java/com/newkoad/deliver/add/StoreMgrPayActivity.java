package com.newkoad.deliver.add;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.newkoad.deliver.R;
import com.newkoad.deliver.add.Addition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StoreMgrPayActivity extends Addition {

    private static final String TAG ="StoreMgrPayActivity" ;

    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<StoreMgrPay> dataList = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_mgr_pay);

        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("관리비");
        headerCloseBtn =findViewById(R.id.header_closeBtn);

        getData();

        dataRV = (RecyclerView) findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(this));
        dataRV.setAdapter(dataListAdapter);
    }



    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/5add_mgr/store_mgr_pay.php", listener, null);
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

                        dataList.add(  new StoreMgrPay( json )   );
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



    class StoreMgrPay {
        protected int key;
        protected String day;
        protected int payDay;
        protected int nopayCharge;
        protected String payState;

        public StoreMgrPay(JSONObject json){
            try{
                this.day = json.getString("day");
                this.payDay = json.getInt("pay_day");
                this.nopayCharge = json.getInt("nopay_charge");
                this.payState = json.getString("pay_state");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<StoreMgrPay> mDataList;
        StoreMgrPay storeMgrPay;

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

        public DataListAdapter( Context mContext, List<StoreMgrPay> mList) {

            this.mContext = mContext;
            this.mDataList = mList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_store_mgr_pay, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DataListHolder dataListHolder = (DataListHolder) holder;
            storeMgrPay = (StoreMgrPay) mDataList.get(i) ;

            dataListHolder.cell1.setText(storeMgrPay.day);
            dataListHolder.cell2.setText(String.valueOf(storeMgrPay.payDay) );
            dataListHolder.cell3.setText(String.valueOf(storeMgrPay.nopayCharge) );
            dataListHolder.cell4.setText(storeMgrPay.payState);


        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }



}

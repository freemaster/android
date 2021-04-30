package com.newkoad.deliver.add;

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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RiderMgrActivity extends Addition {

    private static final String TAG ="RiderMgrActivity" ;
    private MainApp mainApp ;
    private Context context;


    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgr_activity_rider_mgr);

        mainApp = (MainApp)getApplicationContext();

        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);

        headerTitle.setText("기사 관리 [" + mainApp.getActionKey() +"]" );

        headerCloseBtn =findViewById(R.id.header_closeBtn);

        setHeaderBlue( (LinearLayout)findViewById(R.id.header) );


        dataRV = (RecyclerView) findViewById(R.id.data_RV);

        dataListAdapter = new DataListAdapter(context, dataList);

        dataRV.setLayoutManager(new LinearLayoutManager(context));

        dataRV.setAdapter(dataListAdapter);


        getData();

    }





    public void getData(){


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

                    }

                }catch(Exception e){
                    e.printStackTrace();

                }
                dataListAdapter.notifyDataSetChanged();
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

            super(Method.POST, mainApp.getRootURL() + "/5add/rider/rider.php", listener, null);
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



    class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<ItemData> itemList;
        ItemData itemData;

        class DataListHolder extends RecyclerView.ViewHolder{
            protected LinearLayout itemLayout;
            protected TextView levelTv, nameTv, idTv, attendTv, taxTv, lastloginTv, boxidTv, rentTypeTv, monthPayTv, payDayTv, contractTv;
            public DataListHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);

                levelTv = (TextView) itemView.findViewById(R.id.level_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                idTv = (TextView) itemView.findViewById(R.id.id_tv);
                attendTv = (TextView) itemView.findViewById(R.id.att_tv);
                taxTv = (TextView) itemView.findViewById(R.id.tax_tv);
                lastloginTv = (TextView) itemView.findViewById(R.id.lastlogin_tv);

                boxidTv = (TextView) itemView.findViewById(R.id.dd_id_tv);
                rentTypeTv = (TextView) itemView.findViewById(R.id.dd_rent_type_tv);
                monthPayTv = (TextView) itemView.findViewById(R.id.dd_months_payment_tv);
                payDayTv = (TextView) itemView.findViewById(R.id.dd_payment_day_tv);
                contractTv = (TextView) itemView.findViewById(R.id.dd_contract_date);
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
            v = LayoutInflater.from(mContext).inflate(R.layout.item_rider, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;

        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) itemList.get(i) ;



            dataListHolder.nameTv.setText(itemData.name);
            dataListHolder.idTv.setText(itemData.id);
            dataListHolder.attendTv.setText(itemData.att);
            if(itemData.tax.equals("y")){
                dataListHolder.taxTv.setText("과세");
            }else{
                dataListHolder.taxTv.setText("비과세");
            }

            dataListHolder.lastloginTv.setText(itemData.lastlogin);
            dataListHolder.boxidTv.setText(itemData.dd_id);


        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }



    public class ItemData {

        int key;
        String level;
        String id;
        String name;
        String lastlogin;
        String tax ;
        String dd_id;
        String dd_rent_type;
        String dd_payment_day;
        String dd_total_payment;
        String dd_pre_payment;
        String dd_months_payment;
        String dd_contract_date;
        String att ;
        public ItemData(JSONObject json){
            try {
                key = json.getInt("mem_id");
                level = json.getString("level");
                id = json.getString("id");
                name = json.getString("name");
                lastlogin = json.getString("lastlogin");
                tax = json.getString("tax");
                dd_id = json.getString("dd_id");
                dd_rent_type = json.getString("dd_rent_type");
                dd_payment_day = json.getString("dd_payment_day");

                dd_total_payment = json.getString("dd_total_payment");

                dd_pre_payment = json.getString("dd_pre_payment");
                dd_months_payment = json.getString("dd_months_payment");
                dd_contract_date = json.getString("dd_contract_date");
                att = json.getString("att");

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }



}

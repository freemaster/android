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



public class StoreMgrActivity extends Addition {


    private static final String TAG ="StoreMgrActivity" ;
    private MainApp mainApp ;
    private Context context;
    View v;
    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgr_activity_store_mgr);

        mainApp = (MainApp)getApplicationContext();
        context = getApplicationContext();

        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(" 상점 관리");
        headerCloseBtn =findViewById(R.id.header_closeBtn);
        setHeaderBlue( (LinearLayout)findViewById(R.id.header) );


        dataRV = (RecyclerView) findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(context));
        dataRV.setAdapter(dataListAdapter);

        getData();
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
        RequestQueue queue = Volley.newRequestQueue(mainApp.getApplicationContext());
        queue.add(dataListRequest);
    }


    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;
        public DataListRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/store/store.php", listener, null);
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
            protected TextView levelTv, idTv, nameTv, phoneTv, taxTv, mgrPriceTv;
            public DataListHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                levelTv = (TextView) itemView.findViewById(R.id.level_tv);
                idTv = (TextView) itemView.findViewById(R.id.id_tv);
                nameTv = (TextView) itemView.findViewById(R.id.name_tv);
                phoneTv = (TextView) itemView.findViewById(R.id.phone_tv);
                taxTv = (TextView) itemView.findViewById(R.id.tax_tv);
                mgrPriceTv = (TextView) itemView.findViewById(R.id.mgr_price_tv);
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
            v = LayoutInflater.from(mContext).inflate(R.layout.item_store, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListAdapter.DataListHolder(v);
            return holder;

        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) itemList.get(i) ;

            dataListHolder.levelTv.setText(itemData.level);
            dataListHolder.idTv.setText(itemData.id);
            dataListHolder.nameTv.setText(itemData.name);
            dataListHolder.phoneTv.setText(itemData.phone);
            dataListHolder.taxTv.setText(itemData.tax);
            dataListHolder.mgrPriceTv.setText(itemData.mgrPrice);


        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }



    public class ItemData {
        public String key;
        public String name;
        public String id;
        public String level ;
        public String phone;
        public String phone2;
        public String tax;
        public String mgrPrice;

        public ItemData(JSONObject json){
            try {
                this.key = json.getString("id");
                this.name = json.getString("sname");
                this.id = json.getString("sid");
                this.level= json.getString("slevel");
                this.phone = json.getString("sphone");

                this.phone2 = json.getString("sphone2");
                this.tax = json.getString("stax");
                this.mgrPrice = json.getString("smgrPrice");

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }





}

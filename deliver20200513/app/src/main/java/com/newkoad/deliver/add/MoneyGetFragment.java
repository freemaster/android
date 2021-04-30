package com.newkoad.deliver.add;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MoneyGetFragment extends Fragment {

    private MainApp mainApp ;
    private Context context;
    View v;
    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();
    private Dialog myDialog ;



    LinearLayout resultGetLay ;
    LinearLayout firstGetLay;

    public MoneyGetFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = (MainApp) getActivity().getApplication();
        getData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {


        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_money_get,container,false);





        Button refundBtn = (Button) v.findViewById(R.id.refund_btn);
        refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, MoneyGetActivity.class);
                startActivity(intent);
            }
        });
        return v;


    }




    private void getData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);

                        dataList.add(  new ItemData(json )   );
                    }

                    dataListAdapter.notifyDataSetChanged();
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

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit_lsit.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
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
            protected TextView indate,  amount ;
            protected Button resultCancel, resultTrans;

            public DataListHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                indate = (TextView) itemView.findViewById(R.id.indate_tv);
                amount = (TextView) itemView.findViewById(R.id.amount_tv);
                resultCancel = (Button) itemView.findViewById(R.id.result_cancel);
                resultTrans = (Button) itemView.findViewById(R.id.result_trans);
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
            v = LayoutInflater.from(mContext).inflate(R.layout.item_money_get_result, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;

        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) itemList.get(i) ;

            dataListHolder.indate.setText(itemData.indate);
            dataListHolder.amount.setText(itemData.amount);


            dataListHolder.resultCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { doResultCancel();  }
            });

            dataListHolder.resultTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { doResultTran() ;   }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }






    }




    public class ItemData {







        public String indate;
        public String amount ;




        public ItemData(JSONObject json){
            try {
                this.indate = json.getString("indate");
                this.amount = json.getString("amount");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }




    private void doResultTran(){






        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {





                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);




                        dataList.add(  new ItemData(json )   );
                    }




                    dataListAdapter.notifyDataSetChanged();
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


    class TransRequest extends StringRequest {

        private Map<String, String> parameters;
        public TransRequest(Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/5add/deposit_lsit.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("groupKey", String.valueOf( mainApp.getGroupKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }



    private void doResultCancel(){

        firstGetLay.setVisibility(View.VISIBLE);
        resultGetLay.setVisibility(View.GONE);

    }




}

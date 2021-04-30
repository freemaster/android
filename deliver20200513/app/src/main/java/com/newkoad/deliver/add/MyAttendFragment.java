package com.newkoad.deliver.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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


public class MyAttendFragment extends Fragment {

    private static final String TAG ="MyAttendFragment" ;
    private MainApp mainApp ;
    private Context context;
    View v;
    private DataListAdapter dataListAdapter;
    private RecyclerView dataRV;
    private List<ItemData> dataList = new LinkedList<>();

    private int cursor = 0;
    private String searchDate ="";
    TextView searchDateTv;


    public MyAttendFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApp = (MainApp) getActivity().getApplication();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_my_attend,container,false);

        dataRV = (RecyclerView) v.findViewById(R.id.data_RV);
        dataListAdapter = new DataListAdapter(context, dataList);
        dataRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataRV.setAdapter(dataListAdapter);



        searchDateTv = (TextView) v.findViewById(R.id.search_date_tv);
        searchDateTv.setText(getCurrentDateMonth(0));

        ImageButton prevBtn = (ImageButton) v.findViewById(R.id.prev_month_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor--;
                searchDate = getCurrentDateMonth(cursor);
                searchDateTv.setText(searchDate);
                getData();
            }
        });
        ImageButton nextBtn = (ImageButton) v.findViewById(R.id.next_month_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor++;
                searchDate = getCurrentDateMonth(cursor);
                searchDateTv.setText(searchDate);
                getData();

            }
        });
        Button thisBtn = (Button) v.findViewById(R.id.this_month_btn);
        thisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDateTv.setText(getCurrentDateMonth(0));
            }
        });

        getData();
        return v;
    }



    public static Date getCurrentDatePlusMonth(int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, month);
        Date newDate = calendar.getTime();
        return newDate;
    }


    public static String getCurrentDateMonth(int month)  {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, month);
        Date newDate = calendar.getTime();

        String dateFormat = "yyyy-MM";
        String yearMonth = new SimpleDateFormat(dateFormat).format(newDate);
        return yearMonth;
    }



    private void getData(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataList.clear();


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {

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

            super(Method.POST, mainApp.getRootURL() + "/5add/my/attend_list.php", listener, null);
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



    class DataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<ItemData> itemList;
        ItemData itemData;

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
            this.itemList = mList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_attend_c5, viewGroup, false);
            RecyclerView.ViewHolder holder =  new DataListHolder(v);
            return holder;

        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DataListHolder dataListHolder = (DataListHolder) holder;
            itemData = (ItemData) itemList.get(i) ;

            dataListHolder.cell1.setText(itemData.day);
            dataListHolder.cell2.setText(itemData.sTime);
            dataListHolder.cell3.setText(itemData.eTime);
            dataListHolder.cell4.setText(itemData.wTime);
            dataListHolder.cell5.setText(itemData.count);



        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }



    public class ItemData {
        public String day;
        public String sTime;
        public String eTime;
        public String wTime ;
        public String count;
        public ItemData(JSONObject json){
            try {
                this.day = json.getString("day");
                this.sTime = json.getString("s_time");
                this.eTime = json.getString("e_time");
                this.wTime= json.getString("w_time");
                this.count = json.getString("dly_count");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }




}

package com.newkoad.deliver;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.common.Adver;
import com.newkoad.deliver.common.Item;
import com.newkoad.deliver.common.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReceiveFragment extends MyFragment {



    View v;
    private ReceiveAdapter receiveAdapter;
    private RecyclerView receiveRV ;
    private ArrayList<Item>  itemList = new ArrayList<>();

    protected  int option ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainApp = (MainApp) getActivity().getApplicationContext();

        mainActivity = mainApp.mainActivity;

        getData();

    }
    @Override
    public void onStart() {
        super.onStart();

    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();

        v = inflater.inflate(R.layout.fragment_receive,container,false);
        receiveRV = (RecyclerView) v.findViewById(R.id.receive_recyclerview);
        receiveAdapter = new ReceiveAdapter(context, itemList);

        receiveRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        receiveRV.setAdapter(receiveAdapter);


        option  = 0;


        return v;

    }

    protected void getData(){
        itemList.clear();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int cnt = 0, o_cnt =0, a_cnt =0;

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {

                            mainApp.mainActivity.openAlert(jsonObject.getString("msg"));

                            itemList.clear();

                            receiveAdapter.notifyDataSetChanged();

                        }
                    }else {

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        itemList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);

                            Log.d(TAG, "[" + i +"]" + json.toString() );
                            if (json.getString("type").equals("O")) {
                                itemList.add(new Order(json));
                                o_cnt++;
                            } else if (json.getString("type").equals("A")) {
                                itemList.add(new Adver(json));
                                a_cnt++;
                            }
                            cnt++;
                        }

                        mainApp.re_cnt = cnt;
                        mainApp.mainActivity.putBadge(0, cnt);
                        receiveAdapter.notifyDataSetChanged();
                   }
                }catch(Exception e){
                    e.printStackTrace();
                }
                receiveAdapter.notifyDataSetChanged();
            }
        };
        ReceiveListRequest receiveListRequest = new ReceiveListRequest( responseListener);
        receiveListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(receiveListRequest);
    }


    class ReceiveListRequest extends StringRequest {
        private Map<String, String> parameters;
        public ReceiveListRequest(  Response.Listener<String> listener) {
            super(Method.POST,  mainApp.getRootURL() +"/1receive/receive_list.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("token", mainApp.getTokenp());
            parameters.put("aID", mainApp.getAndroidID());

        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }




    class ReceiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        char type ;
        Context mContext;
        ArrayList<Item> mItemList = null;
        ArrayList<Item> copyList = null;


        public Order order;
        public Adver adver;
        Intent intent;




        class OrderHolder extends RecyclerView.ViewHolder{
            private LinearLayout item_layout;
            ImageView item_head_image ;
            private TextView store , datetime, distance;
            private TextView dlyPay , payType;
            private TextView eaddr , rtime, ptime;
            private TextView eRoad, eLand;



            public OrderHolder(@NonNull View itemView) {
                super(itemView);
                item_layout  = (LinearLayout) itemView.findViewById(R.id.item_layout);
                item_head_image = (ImageView) itemView.findViewById(R.id.itme_head_image);
                store = (TextView) itemView.findViewById(R.id.order_store);
                datetime =(TextView) itemView.findViewById(R.id.order_datetime);

                distance = (TextView) itemView.findViewById(R.id.order_distance);
                dlyPay = (TextView) itemView.findViewById(R.id.order_delivery_pay);
                payType = (TextView) itemView.findViewById(R.id.order_pay_type);


                eRoad = (TextView) itemView.findViewById(R.id.addr_eroad);
                eLand = (TextView) itemView.findViewById(R.id.addr_eland);

                rtime = (TextView) itemView.findViewById(R.id.require_time_tv);
                ptime = (TextView) itemView.findViewById(R.id.pass_time_tv);


            }
        }


        public ReceiveAdapter(Context mContext, ArrayList<Item> itemList) {
            this.mContext = mContext;
            this.mItemList = itemList;
            copyList = new ArrayList<>() ;
            type = ' ';
        }



        @Override
        public int getItemCount() {

            return mItemList.size();

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DecimalFormat formatter = new DecimalFormat("###,###");
            LinearLayout linearLayout = null;
            long cStemp  = System.currentTimeMillis()/1000;
            long diff = 0 ;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            Date currentDate, orderDate;
            SimpleDateFormat dsdf = new SimpleDateFormat("mm분 ss초");
            String str = "";



            OrderHolder orderHolder = (OrderHolder) holder;
            final Order order = (Order) mItemList.get(i);
            orderHolder.store.setText( order.getsName());

            orderHolder.datetime.setText( order.getDatetime() ) ;


            orderHolder.distance.setText(String.valueOf( order.getDistance()) +"km");
            orderHolder.dlyPay.setText(formatter.format(order.getdPrice()) + "원");
            orderHolder.payType.setText( order.getPayTypeStr() );


            orderHolder.eRoad.setText(order.geteRoad());
            orderHolder.eLand.setText(order.geteLand());

            orderHolder.rtime.setText( String.valueOf(order.getrTime()) + "분");
            try {
                currentDate = sdf.parse(currentDateandTime);
                orderDate = sdf.parse(order.getDatetime());
                diff = currentDate.getTime() - orderDate.getTime();
                Date d = new Date(diff);
                str = dsdf.format(d);
            }catch (Exception e){

            }

            orderHolder.ptime.setText( str );
            orderHolder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    intent = new Intent(mContext, ReceiveActivity.class);
                    intent.putExtra("item", order);

                    mContext.startActivity(intent);
                }
            });



        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup , int viewType) {
            View v;
            RecyclerView.ViewHolder holder = null;

            v = LayoutInflater.from(mContext).inflate(R.layout.item_order, viewGroup, false);
            holder = new OrderHolder(v);


            return holder;
        }
    }



    public void listUpdate(){

        itemList.clear();

        mainApp.getSoundManager().play("new");

        getData();

    }



}





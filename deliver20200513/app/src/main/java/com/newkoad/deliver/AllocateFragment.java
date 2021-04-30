package com.newkoad.deliver;


import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AllocateFragment extends MyFragment {


    View v;
    private AllocateAdapter allocateAdapter;
    private RecyclerView allocateRV;

    private ArrayList<Item> lstItem = new ArrayList<>();
    private int al_cnt =0;
    protected TextView alCntTv ;

    protected  int option ;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApp = (MainApp) getActivity().getApplication();
        mainApp.allocateList = lstItem;


            getData();


    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_allocate, container, false);
        allocateRV = (RecyclerView) v.findViewById(R.id.allocate_recyclerview);
        allocateAdapter = new AllocateAdapter(context, lstItem);
        allocateRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        allocateRV.setAdapter(allocateAdapter);


        option  = 0;



        return v;


    }



    class AllocateListRequest extends StringRequest {

        private Map<String, String> parameters;

        public AllocateListRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/2allocate/allocate_list.php", listener, null);

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



    protected void getData() {

        lstItem.clear();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {
                            mainApp.mainActivity.openAlert(jsonObject.getString("msg"));
                            lstItem.clear();

                        }
                    }else {
                        lstItem.clear();
                        al_cnt = 0;
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            if (json.getString("type").equals("A")) {

                                lstItem.add(new Adver(json));
                            } else {

                                lstItem.add(new Order(json));
                            }
                            al_cnt++;
                        }


                        mainApp.setAllocateCnt(al_cnt);
                        mainApp.mainActivity.putBadge(1, al_cnt);
                        allocateAdapter.notifyDataSetChanged();

                    }

                    allocateAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        };
        AllocateListRequest allocateListRequest = new AllocateListRequest(responseListener);
        allocateListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(allocateListRequest);
    }



    class AllocateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        Context mContext;
        ArrayList<Item> mItemList;
        ArrayList<Item> copyList = null;

        Item item ;
        public Order order;
        public Adver adver;
        Intent intent;

        class OrderHolder extends RecyclerView.ViewHolder{
            private LinearLayout item_layout, item_back_lay;
            ImageView item_head_image ;
            ImageView cart;

            private TextView store , datetime , distance;
            private TextView dlyPay ,  payType;
            private TextView eaddr;
            private TextView eRoad, eLand;

            private ImageButton mapBtn;
            public OrderHolder(@NonNull View itemView) {
                super(itemView);
                item_layout  = (LinearLayout) itemView.findViewById(R.id.item_layout);
                item_back_lay = (LinearLayout) itemView.findViewById(R.id.item_back_lay);

                item_head_image = (ImageView) itemView.findViewById(R.id.itme_head_image);
                cart = (ImageView) itemView.findViewById(R.id.cart_ic);

                store = (TextView) itemView.findViewById(R.id.order_store);

                datetime =(TextView) itemView.findViewById(R.id.order_datetime) ;

                distance = (TextView) itemView.findViewById(R.id.order_distance);

                dlyPay = (TextView) itemView.findViewById(R.id.order_delivery_pay);

                payType = (TextView) itemView.findViewById(R.id.order_pay_type);


                eRoad = (TextView) itemView.findViewById(R.id.addr_eroad);

                eLand = (TextView) itemView.findViewById(R.id.addr_eland);

                mapBtn = (ImageButton) itemView.findViewById(R.id.map_btn);
            }
        }



        public AllocateAdapter(Context mContext, ArrayList<Item> mItemList) {
            this.mContext = mContext;
            this.mItemList = mItemList;
            copyList = new ArrayList<>() ;

        }


        @Override
        public int getItemCount() {

            return mItemList.size();

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

            DecimalFormat formatter = new DecimalFormat("###,###");
            LinearLayout linearLayout = null;

            final OrderHolder orderHolder = (OrderHolder) holder;
            final Order order = (Order) mItemList.get(i);

            orderHolder.store.setText( order.getsName()   );

            orderHolder.datetime.setText( order.getDatetime()  );

            orderHolder.distance.setText(String.valueOf(order.getDistance()));

            orderHolder.dlyPay.setText(formatter.format(order.getdPrice()) + "원");

            orderHolder.payType.setText( order.getPayTypeStr() );

            orderHolder.eRoad.setText(order.geteRoad());

            orderHolder.eLand.setText(order.geteLand());

            orderHolder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(mContext, AllocateActivity.class);
                    intent.putExtra("item", order);
                    mContext.startActivity(intent);
                }
            });



            linearLayout = orderHolder.item_layout;

            if (order.status == 5) {
                orderHolder.cart.setVisibility(View.VISIBLE);
                orderHolder.store.setText( order.getsName()  );
                orderHolder.store.setTextColor(Color.YELLOW);
            } else {
                orderHolder.cart.setVisibility(View.GONE);
                orderHolder.store.setTextColor(Color.WHITE);
            }



        }



        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup , int viewType) {
            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_order, viewGroup, false);
            RecyclerView.ViewHolder holder = null;
            LinearLayout linearLayout = null;

            holder = new OrderHolder(v);
            linearLayout = ((OrderHolder) holder).item_layout;



            return holder;
        }
    }


    public void listUpdate(){
        getData();

    }


    public void listUpdate2(){
        getData();

    }




}








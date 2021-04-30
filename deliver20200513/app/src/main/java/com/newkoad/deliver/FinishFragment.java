package com.newkoad.deliver;



import android.app.DatePickerDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;
import java.util.GregorianCalendar;




public class FinishFragment extends MyFragment {


    View v;
    private FinishAdapter finishAdapter;
    private RecyclerView finishRV ;

    private List<Item>  lstItem = new LinkedList<>();


    protected  int option ;


    int mYear, mMonth, mDay;

    TextView sDate, eDate;
    Button searchBtn ;
    String startDate = "", endDate ="";

    Calendar calendar;
    private long minTime =0 , maxTime =0;
    private Date minDate, maxDate;
    DatePicker sDatePicker, eDatePicker;
    DatePickerDialog datePickerDialog;
    String endDateStr;



    DatePickerDialog.OnDateSetListener sDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    mYear = year; mMonth = monthOfYear;  mDay = dayOfMonth;

                    sDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
                }
            };


    DatePickerDialog.OnDateSetListener eDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    mYear = year;  mMonth = monthOfYear; mDay = dayOfMonth;

                    eDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));

                }
            };




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainApp = (MainApp) getActivity().getApplication();

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public static String getCurrentDate(int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, month);
        Date newDate = calendar.getTime();

        String dateFormat = "yyyy-MM-dd";
        String yearMonth = new SimpleDateFormat(dateFormat).format(newDate);
        return yearMonth;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState){

        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_finish,container,false);
        finishRV = (RecyclerView) v.findViewById(R.id.finish_recyclerview);
        finishAdapter = new FinishAdapter(context, lstItem);
        finishRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        finishRV.setAdapter(finishAdapter);


        option  = 0;




        sDate = (TextView) v.findViewById(R.id.s_date_et);
        eDate = (TextView) v.findViewById(R.id.e_date_et);


        calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH)  ;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        sDate.setText(String.format("%d-%d-%d", mYear, mMonth +1 , mDay));
        eDate.setText(String.format("%d-%d-%d", mYear, mMonth +1 , mDay));


        sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DatePickerDialog.OnDateSetListener sDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                mYear = year; mMonth = monthOfYear ;  mDay = dayOfMonth;

                                sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1 , mDay));


                            }
                        };


                datePickerDialog = new DatePickerDialog(context, sDateSetListener, mYear, mMonth, mDay);



                datePickerDialog.show();
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener eDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                mYear = year;  mMonth = monthOfYear; mDay = dayOfMonth;

                                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));



                            }
                        };


                datePickerDialog = new DatePickerDialog(context, eDateSetListener, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });



        Button todayBtn= v.findViewById(R.id.today_btn);
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = new GregorianCalendar();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));

            }
        });



        Button oneweekBtn= v.findViewById(R.id.week_btn);
        oneweekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = new GregorianCalendar();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);


                sDate.setText(get7DayAgoDate(mYear, mMonth, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));


            }
        });

        Button onemonthBtn= v.findViewById(R.id.month_btn);
        onemonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = new GregorianCalendar();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                sDate.setText(String.format("%d-%d-%d", mYear, (mMonth + 1) -1, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));


            }
        });




        searchBtn = (Button) v.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate = sDate.getText().toString();
                endDate = eDate.getText().toString();
                getData();
            }
        });


        getData();


        return v;
    }


    class FinishListRequest extends StringRequest {

        private Map<String, String> parameters;
        public FinishListRequest(  Response.Listener<String> listener) {

            super(Method.POST, mainApp.getRootURL() + "/3finish/finish_list",listener, null);
            parameters = new HashMap<>();
            parameters.put("cKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());
            parameters.put("token", mainApp.getTokenp());
            parameters.put("aID", mainApp.getAndroidID());


            parameters.put("s_date", sDate.getText().toString() );


            parameters.put("startDate", sDate.getText().toString() );

            if( calDateBetweenAandB(  eDate.getText().toString(), sDate.getText().toString() ) > 90 ){

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate;
                try {
                    startDate = format.parse(sDate.getText().toString());
                    calendar.setTime(startDate);
                    calendar.add(Calendar.MONTH, +4);
                    endDateStr =  calendar.get(Calendar.YEAR) +"-" + calendar.get(Calendar.MONTH) + "-"+calendar.get(Calendar.DAY_OF_MONTH);

                }catch (Exception e){
                    e.printStackTrace();
                }
                parameters.put("endDate", endDateStr);
            }else{
                parameters.put("endDate", eDate.getText().toString() );
            }


        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


    private String get7DayAgoDate(int year , int month , int day) {

        Calendar cal = Calendar.getInstance();

        cal.set(year, month, day);
        cal.add(Calendar.DATE, -7);
        java.util.Date weekago = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(weekago);
    }



    public void getData(){
        lstItem.clear();



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                int fi_cnt = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {
                            mainApp.mainActivity.openAlert(jsonObject.getString("msg"));
                            lstItem.clear();

                        }
                    }else {
                        lstItem.clear();
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            if (json.getString("type").equals("A")) {

                                lstItem.add(new Adver(json));
                            } else {

                                lstItem.add(new Order(json));
                            }
                            fi_cnt++;
                        }

                        mainApp.mainActivity.putBadge(2, fi_cnt);

                    }

                finishAdapter.notifyDataSetChanged();
                }catch(Exception e){
                    e.printStackTrace();

                }


            }
        };

        FinishListRequest finishListRequest =new FinishListRequest(responseListener);
        finishListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(finishListRequest);
    }



    class FinishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<Item> mItemList;
        ArrayList<Item> copyList = null;

        public Order order;
        public Adver adver;
        Intent intent;

        class OrderHolder extends RecyclerView.ViewHolder{
            private LinearLayout item_layout;
            ImageView item_head_image ;
            private TextView store, datetime;
            private TextView distance;
            private TextView dlyPay;
            private TextView payType;

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

            }
        }



        public FinishAdapter(Context mContext, List<Item> mItemList) {
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

            OrderHolder orderHolder = (OrderHolder) holder;
            final Order order = (Order) mItemList.get(i) ;

            orderHolder.store.setText( order.getsName());

            orderHolder.distance.setText( order.distance + "km" );
            orderHolder.dlyPay.setText( formatter.format(order.getdPrice()) + "원" );
            orderHolder.payType.setText( order.getPayTypeStr() );


            orderHolder.eRoad.setText(order.geteRoad());
            //orderHolder.eLand.setText(order.geteLand());

            orderHolder.datetime.setText( order.getDatetime() + "[" + order.getKey()  +"]" );

            orderHolder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(mContext, FinishKSActivity.class);
                    intent.putExtra("item", order);
                    mContext.startActivity(intent);
                }
            });


        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup , int viewType) {
            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_order, viewGroup, false);

            RecyclerView.ViewHolder holder = null;
            holder = new OrderHolder(v);


            return holder;
        }
    }


    public void listUpdate(){


        getData();
    }



    public int calDateBetweenAandB( String dateA, String dateB)
    {
        String date1 = dateA;
        String date2 = dateB;

        int returnVal = 0;

        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date FirstDate = format.parse(date1);
            Date SecondDate = format.parse(date2);


            long calDate = FirstDate.getTime() - SecondDate.getTime();

            long calDateDays = calDate / ( 24*60*60 *1000);

            returnVal = (int) Math.abs(calDateDays) ;


        } catch(Exception e) {

        }

        return returnVal;
    }

}



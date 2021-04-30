package com.newkoad.deliver.add;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class MyBoxFragment extends Fragment {

    private static final String TAG ="MyBoxFragment" ;
    private MainApp mainApp ;
    private Context context;
    View v;

    TextView boxIDTv;
    TextView contractDayTv;
    TextView rentPayTv, paymentDaytv;

    DecimalFormat decimalFormat ;

    public MyBoxFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = (MainApp) getActivity().getApplication();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        v = inflater.inflate(R.layout.add2_my_box_frag,container,false);

        boxIDTv = (TextView) v.findViewById(R.id.box_id_tv) ;
        contractDayTv = (TextView) v.findViewById(R.id.contract_day) ;
        rentPayTv = (TextView) v.findViewById(R.id.rent_pay) ;
        paymentDaytv = (TextView) v.findViewById(R.id.payment_day) ;

        decimalFormat = new DecimalFormat("###,###");

        getData();
        return v;

    }


    public void getData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if( jsonObject.has("aidresult") ) {
                        if (jsonObject.getString("aidresult").equals("fail")) {

                        }
                    }else {
                        boxIDTv.setText(jsonObject.getString("boxId"));
                        contractDayTv.setText(jsonObject.getString("contractDay"));
                        rentPayTv.setText("월 " + decimalFormat.format(Integer.parseInt(jsonObject.getString("rent_pay"))) + " 원");
                        paymentDaytv.setText(jsonObject.getString("paymentDay") + " 일");
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

            super(Method.POST, mainApp.getRootURL() + "/5add/my/box.php", listener, null);
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




}

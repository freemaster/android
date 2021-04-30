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

import java.util.HashMap;
import java.util.Map;


public class MyCountFragment extends Fragment {

    private static final String TAG ="MYCOUNT" ;
    private MainApp mainApp ;
    private Context context;
    View v;

    TextView depositTv;
    TextView cBankTv, cAccountTv, cOwnerTv;
    TextView requestBankTv, requestAccountTv, ownerTv;

    public MyCountFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = (MainApp) getActivity().getApplication();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        v = inflater.inflate(R.layout.add2_my_count_frag,container,false);

        depositTv = (TextView) v.findViewById(R.id.deposit_tv);

        cBankTv = (TextView) v.findViewById(R.id.c_bank_tv);
        cAccountTv = (TextView) v.findViewById(R.id.c_account_tv);
        cOwnerTv = (TextView) v.findViewById(R.id.c_owner_tv);

        requestBankTv= (TextView) v.findViewById(R.id.request_bank_tv);
        requestAccountTv= (TextView) v.findViewById(R.id.request_account_tv);
        ownerTv = (TextView) v.findViewById(R.id.owner_tv);

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

                        depositTv.setText(jsonObject.getString("deposit"));
                        cBankTv.setText(jsonObject.getString("c_bank"));
                        cAccountTv.setText(jsonObject.getString("c_account"));
                        cOwnerTv.setText(jsonObject.getString("c_owner"));
                        requestBankTv.setText(jsonObject.getString("request_bank"));
                        requestAccountTv.setText(jsonObject.getString("request_account"));
                        ownerTv.setText(jsonObject.getString("owner"));
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

            super(Method.POST, mainApp.getRootURL() + "/5add/my/account.php", listener, null);
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

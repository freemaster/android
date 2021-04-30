package com.newkoad.deliver.add;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.R;
import com.newkoad.deliver.add.Addition;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DriverPenaltyActivity extends Addition {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_penalty);

        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("패널티");
        headerCloseBtn =findViewById(R.id.header_closeBtn);


    }


    class DataListRequest extends StringRequest {

        private Map<String, String> parameters;

        public DataListRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/5add_mgr/list_mgr_driver_attend.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf(mainApp.getUserKey()));
            parameters.put("parentKey", String.valueOf(mainApp.getParentKey()));
            parameters.put("groupKey", String.valueOf(mainApp.getGroupKey()));
            parameters.put("aID", mainApp.getAndroidID());
        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }


    private void getData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject json = jsonArray.getJSONObject(0);

                    progressDialog.dismiss();

                } catch (Exception e) {
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


}
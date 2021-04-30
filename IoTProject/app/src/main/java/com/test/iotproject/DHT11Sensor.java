package com.test.iotproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DHT11Sensor extends StringRequest {
    //node.js 에 요청하는 URL
    final static String URL = "http://210.183.87.114:3000/devices/device";
    private Map<String, String> parameters;

    public DHT11Sensor(String sensor, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);    // 요청되는 부분
        // sensor=dht11 식으로 작성해야함
        // sensor=mq2
        parameters = new HashMap<String, String>();
        parameters.put("sensor", sensor);
    }

    @Override
    protected Map<String, String> getParams(){
        return parameters;
    }
}

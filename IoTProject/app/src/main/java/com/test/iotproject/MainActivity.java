package com.test.iotproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] sensors={"dht11", "mq2"};
        ArrayAdapter<String> spinnerAdapter=
                new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        sensors);
        Spinner spinner =(Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                // 다이얼로그
                final ProgressDialog dialog=new ProgressDialog(MainActivity.this);
                dialog.setMessage("센서 로그 정보 수신 중...");
                dialog.show();
                // node.js서버로 dht11 센서의 온도,습도,날짜 데이터를 요청

                //자료구조에서 먼저 실행해주는
                // 2. 받는 방법 -> listener
                Response.Listener<String> listener=new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) { // [{     },{     }.{     },,,,]
                        dialog.dismiss();
                        try {
                            JSONArray array=new JSONArray(response); //서버로 부터 받을 배열 값을 제이슨 방식으로 받는 것
                            items.clear();
                            for(int i=0; i<array.length();i++){
                                //받은 jSON형식의 배열 값을 뽑아서 ITEM이라는 VO방식의 형태로 묶고 ArrayList로 담아야한다.
                                JSONObject obj = array.getJSONObject(i);
                                // Item(묶고) -> ArrayList(담고)
                                items.add(new Item(obj.getInt("tmp"),
                                        obj.getInt("hum"),
                                        obj.getString("create_at")));
                            }//_for
                            // DATA-> Adapter(제공,커스텀) -> ListView에 연결
                            ItemAdapter adapter = new ItemAdapter(MainActivity.this);
                            ListView listView=findViewById(R.id.listview);
                            listView.setAdapter(adapter);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                // 1. 요청하는 방법 -> API(Volley) : 별도의 class - DHT11Sensor
                StringRequest dht11 = new DHT11Sensor(sensors[i], listener);
                dht11.setShouldCache(false); // 새로운 데이터를 항상 받겠다.
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(dht11); //실행
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); //선택된 아이템이 이벤트가 발생되는데 이것을 처리할 리스너를 발생

        //받은 jSON형식의 배열 값을 뽑아서 ITEM이라는 VO방식의 형태로 묶고 ArrayList로 담아야한다.
    }// _onCreate

    public void clickLedOnButton(View view) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), "LED가 켜짐"+obj.get("led"), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest led = new LEDSensor("on", listener);
        led.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(led);
    }

    public void clickLedOffButton(View view) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), "LED가 꺼짐"+obj.get("led"), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        StringRequest led = new LEDSensor("off", listener);
        led.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(led);
    }


    class Item{
        int temp, humidity; String created_at;
        Item(int temp, int humidity, String created_at){
            this.temp=temp;
            this.humidity=humidity;
            this.created_at=created_at;
        }
    }//_Item
    ArrayList<Item> items=new ArrayList<Item>();
    // 커스텀뷰
    class ItemAdapter extends ArrayAdapter{
        public ItemAdapter(Context context) {
            super(context, R.layout.list_sensor_item, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=convertView;
            if(view==null){
                LayoutInflater inflater=
                        (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view=inflater.inflate(R.layout.list_sensor_item, null);
            }
            TextView tempText=view.findViewById(R.id.temp);
            TextView humidityText=view.findViewById(R.id.humidity);
            TextView createdAtText=view.findViewById(R.id.created_at);
            tempText.setText("온도:"+items.get(position).temp);
            humidityText.setText("습도:"+items.get(position).humidity);
            createdAtText.setText("수집정보(날짜/시간)"+items.get(position).created_at);
            return view;
        }
    }
}// _class
package com.newkoad.deliver;

import android.app.ProgressDialog;
import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgActivity extends AppCompatActivity {

    private MainApp mainApp ;
    private Context context;

    private MsgAdapter msgAdapter;
    private RecyclerView msgRV ;
    private List<Msg> lstMsg = new ArrayList<>();
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_msg);
        mainApp = (MainApp) getApplication();
        context = getApplicationContext();
        getData();


        msgRV = (RecyclerView) findViewById(R.id.msg_recyclerview);
        msgAdapter = new MsgAdapter(lstMsg);
        msgRV.setLayoutManager(new LinearLayoutManager(   this  ));
        msgRV.setAdapter(msgAdapter);


        EditText msgInput = (EditText) findViewById(R.id.msg_input);
        final Button msgSend = (Button) findViewById(R.id.msg_send);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(msgInput, 0);






        msgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 전송후 응답
                Response.Listener<String>  responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(" ::: msg send :::", response );


                    }
                };

                MsgSendRequest msgSendRequest = new MsgSendRequest(responseListener);
                msgSendRequest.setShouldCache(false);
                RequestQueue queue = Volley.newRequestQueue(MsgActivity.this);
                queue.add(msgSendRequest);
            }
        });


    }



    class MsgSendRequest extends StringRequest {

        private Map<String, String> parameters;
        public MsgSendRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/4message/msg_send.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("fromKey", String.valueOf( mainApp.getUserKey() )); // from
            parameters.put("toKey", "5"); // to (82)
            parameters.put("text", "Hello 82"); // to
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }






    class MsgDataRequest extends StringRequest {

        private Map<String, String> parameters;
        public MsgDataRequest(Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() + "/4message/msgdata_list.php", listener, null);
            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ));
        }
        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }

    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);

                        lstMsg.add(  new Msg( json )   );
                    }


                    msgAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                }catch(Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        };
        MsgDataRequest msgDataRequest =new MsgDataRequest( responseListener);
        msgDataRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue( this);
        queue.add(msgDataRequest);

    }



    class MsgAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<Msg> mMsgList;
        Msg msg;

        class MyHolder extends RecyclerView.ViewHolder{
            private LinearLayout myLayout;
            private TextView msgTitle, msgText , msgTime;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                LinearLayout myLayout = itemView.findViewById(R.id.msg_my_layout);
                msgTitle  = (TextView) itemView.findViewById(R.id.msg_title);
                msgText = (TextView) itemView.findViewById(R.id.msg_text);
                msgTime = (TextView) itemView.findViewById(R.id.msg_time);
            }
        }

        class OthersHolder extends RecyclerView.ViewHolder{
            private LinearLayout othersLayout;
            private TextView msgTitle , msgText , msgTime;
            public OthersHolder(@NonNull View itemView) {
                super(itemView);
                LinearLayout othersLayout = itemView.findViewById(R.id.msg_others_layout);
                msgTitle  = (TextView) itemView.findViewById(R.id.msg_title);
                msgText = (TextView) itemView.findViewById(R.id.msg_text);
                msgTime = (TextView) itemView.findViewById(R.id.msg_time);
            }
        }



        public MsgAdapter( List<Msg> mMsgList) {
            // this.mContext = mContext;
            this.mMsgList = mMsgList;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View v;

            RecyclerView.ViewHolder holder = null;


            switch (viewType){
                case 0 :
                    v = LayoutInflater.from(context).inflate(R.layout.item_msg_my, viewGroup, false);
                    holder = new MyHolder(v);
                    break;
                case 1:
                    v = LayoutInflater.from(context).inflate(R.layout.item_msg_others, viewGroup, false);
                    holder = new OthersHolder(v);
                    break;
            }
            return holder;

        }



        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


            switch (holder.getItemViewType()) {
                case 0:
                    MyHolder myHolder = (MyHolder) holder;
                    msg = (Msg) mMsgList.get(i);

                    myHolder.msgText.setText(msg.getText() );
                    myHolder.msgTime.setText(msg.getTime() );
                    break;
                case 1:
                    OthersHolder othersHolder = (OthersHolder) holder;
                    msg = (Msg) mMsgList.get(i);

                    othersHolder.msgText.setText(msg.getText() );
                    othersHolder.msgTime.setText(msg.getTime() );
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if( mMsgList.get(position).type == 'M' ){

                return 0 ;
            } else {

                return 1 ;
            }

        }

        @Override
        public int getItemCount() {

            return mMsgList.size();
        }
    }


}





package com.newkoad.deliver;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.MsgActivity;
import com.newkoad.deliver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MsgNodeFragment extends Fragment {

    private MainApp mainApp ;
    private Context context;

    View v;
    private MsgNodeAdapter msgNodeAdapter;
    private RecyclerView msgNodeRV ;
    private List<MsgNode> lstMsgNode = new ArrayList<>();


    public void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);

                        lstMsgNode.add(  new MsgNode( json )   );
                    }


                    msgNodeAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                }catch(Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        };

        MsgNodeListRequest msgNodeListRequest =new MsgNodeListRequest("start", responseListener);
        msgNodeListRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(msgNodeListRequest);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApp = (MainApp) getActivity().getApplication();
        getData();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_msg_node,container,false);
        msgNodeRV = (RecyclerView) v.findViewById(R.id.msgNode_recyclerview);
        msgNodeAdapter = new MsgNodeAdapter(context, lstMsgNode);
        msgNodeRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        msgNodeRV.setAdapter(msgNodeAdapter);

        return  v;

    }











    class MsgNodeListRequest extends StringRequest {


        private Map<String, String> parameters;
        public MsgNodeListRequest(String play,  Response.Listener<String> listener) {
            super(Method.POST, mainApp.getRootURL() +"/4message/msgnode.php", listener, null);

            parameters = new HashMap<>();
            parameters.put("userKey", String.valueOf( mainApp.getUserKey() ) );
            parameters.put("parentKey", String.valueOf( mainApp.getParentKey() ) );
            parameters.put("userID", mainApp.getUserID() );
            parameters.put("userGroup", mainApp.getUserGroup());

        }

        @Override
        public Map<String, String> getParams() {
            return parameters;
        }
    }






    class MsgNodeAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<MsgNode> mNodeList;
        Dialog mDialog;
        MsgNode msgNode;

        class MsgNodeHolder extends RecyclerView.ViewHolder {
            private LinearLayout nodeLayout;
            private TextView nodeTitle, nodeTime, nodeText;
            public MsgNodeHolder(@NonNull View itemView) {
                super(itemView);
                nodeLayout = (LinearLayout) itemView.findViewById(R.id.node_layout);
                nodeTitle = (TextView) itemView.findViewById(R.id.node_title);
                nodeTime = (TextView) itemView.findViewById(R.id.node_time);
                nodeText = (TextView) itemView.findViewById(R.id.node_text);
            }

        }

        public MsgNodeAdapter(Context mContext, List<MsgNode> mNodeList) {
            this.mContext = mContext;
            this.mNodeList = mNodeList;
        }


        @Override
        public int getItemCount() {
            return mNodeList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
            MsgNodeHolder msgNodeHolder = (MsgNodeHolder)holder;
            final MsgNode msgNode = (MsgNode) mNodeList.get(i);
            msgNodeHolder.nodeTitle.setText(msgNode.title);
            msgNodeHolder.nodeTime.setText(msgNode.time);
            msgNodeHolder.nodeText.setText(msgNode.text);
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v;
            v = LayoutInflater.from(mContext).inflate(R.layout.item_msgnode, viewGroup, false);
            final MsgNodeHolder vHolder = new MsgNodeHolder(v);


            // item click ////////////////////////////////
            vHolder.nodeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MsgActivity.class);
                    //intent.putExtra("order", response);  // JSON  문자열 직접 넘기자 !!
                    mContext.startActivity(intent);
                }
            });
            return vHolder;

        }
    } ////////////






    public class MsgNode {

        public String title;
        public String text;
        public String time;


        public MsgNode(JSONObject json) {

            try {
                this.title = json.getString("title");
                this.text = json.getString("text");
                this.time = json.getString("time");

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }



}














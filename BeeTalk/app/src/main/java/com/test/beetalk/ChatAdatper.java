package com.test.beetalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdatper extends BaseAdapter {
    Context c;
    int layout;
    ArrayList<ChatVO> data;
    LayoutInflater inflater;
    String id; // 현재 로그인한 아이디

    public ChatAdatper(Context c, int layout, ArrayList<ChatVO> data, String id) {
        this.c = c;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id = id;  // 현재 로그인한 아이디
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }

        //로그인 한사람(본인)의 msg
        TextView tv_mymsg = convertView.findViewById(R.id.tv_mymsg);
        TextView tv_mytime = convertView.findViewById(R.id.tv_mytime);

        //다른사람이 보낸 msg
        TextView tv_othermsg = convertView.findViewById(R.id.tv_othermsg);
        TextView tv_othername = convertView.findViewById(R.id.tv_othername);
        TextView tv_otherTime = convertView.findViewById(R.id.tv_othertime);
        ImageView imageView = convertView.findViewById(R.id.img);

        //만약 현제 메세지가 내가 보낸 메시지라면~
        //오른쪽 말풍선만 Visible, 왼쪽 말풍선은 gone
        if(data.get(position).getName().equals(id)){
            tv_mymsg.setVisibility(View.VISIBLE);
            tv_mytime.setVisibility(View.VISIBLE);

            tv_othermsg.setVisibility(View.GONE);
            tv_othername.setVisibility(View.GONE);
            tv_otherTime.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);

            tv_mymsg.setText(data.get(position).getMsg());
            tv_mytime.setText(data.get(position).getTime());
        } else {
            tv_mymsg.setVisibility(View.GONE);
            tv_mytime.setVisibility(View.GONE);

            tv_othermsg.setVisibility(View.VISIBLE);
            tv_othername.setVisibility(View.VISIBLE);
            tv_otherTime.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);

            tv_othermsg.setText(data.get(position).getMsg());
            tv_othername.setText(data.get(position).getName());
            tv_otherTime.setText(data.get(position).getTime());
            imageView.setImageResource(data.get(position).getImg());
        }




        return convertView;
    }
}

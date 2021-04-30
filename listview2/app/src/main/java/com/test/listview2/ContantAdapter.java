package com.test.listview2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ContantAdapter extends BaseAdapter {
    private Context c;
    private int layout;
    private ArrayList<ContentListVO> data;
    private LayoutInflater inflater;

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

    public ContantAdapter(Context c, int layout, ArrayList<ContentListVO> data) {
        this.c = c;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder; //텅빈 케이스

        if(convertView == null) { //최초실행 이라면
            convertView = inflater.inflate(layout, parent, false);

            //최초 convertView가 inflate 될 때 !! (첫 항목이 만들어 질 때)
            holder = new ViewHolder(); //새 holder를 만든다.
            //holder에 TxtView와 Button을 저장해 준다.
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_link = convertView.findViewById(R.id.tv_link);
            holder.btn = convertView.findViewById(R.id.btn);
            convertView.setTag(holder); //완성된 Holder를 converView에 달아줍니다.
        } else { //최초 실행이 아닐 때!(최초 한번이 아닐 때)
            holder = (ViewHolder)convertView.getTag();
        }

        //getView 항목하나가 만들어 질때마다 호출이 되는데
        //항목을 만들때마다 findViewById를 하면 굉장히 부하가 걸림
        //ViewHolder 패턴을 이용하여 효율적으로 View 들을 관리해보다!!
        //viewHolder란? View를 저장해둔 객체!(VO랑 비슷)

        //제목 추가
        holder.tv_name.setText(data.get(position).getName());

        //URL 추가
        holder.tv_link.setText(data.get(position).getLink());

        //바로가기 제어
        holder.btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //버튼 누를때 url 바로가기
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getLink()));
                //ActivityClass가 아니기 때문에 (AppCompatActivity 클래스를 상속하지 않았기 때문에)
                //바로 startAcitivity 메소드를 사용할 수 없습니다.
                //생성자를 통해 전달받은 Context의 도움을 받아 startActivity를 실행~

                //startActivity를 수행하면 task에  process가 쌓이는데
                //이곳은 Activity가 아니기 때문에 task에 접근 할 수가 없음.
                //그래서!!! intent에 옵션(flag)을 설정하여 새로운 task를 생성!!!!!!!
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//새로운 task에서 이 Activity를 실행하겠다 라면 구문(중요도★★★★★★★★★)
                c.startActivity(intent);
            }
        });

        return convertView;
    }

    //ViewHolder 설계
    public class ViewHolder{
        TextView tv_name;
        TextView tv_link;
        Button btn;
    }
}

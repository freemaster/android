package com.test.exam_listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//1. BaseAdapter를 상속받는다.
//2. BaseAdapter는 추상클래스(추상메소드(몸체가 없는 메소드)가 하나 이상 포함되어 있는 클래스)
//이기 때문에 내장되어 있는 추상메소드를 반드시 Overriding 해주어야함
// BaseAdapter위에 마우스 올리고 Alt+enter >  implements method 클릭 > 모두선택하고 OK
//3. Adapter 작업에 필요한 도구를 전달(생성자)받아야 함
public class ContantAdapter extends BaseAdapter {
    private Context c;
    private int layout;
    private ArrayList<ContantVO> data;
    private LayoutInflater inflater; //주소값을(보통은 int형태로 들어가있음) -> view

    public ContantAdapter(Context c, int layout, ArrayList<ContantVO> data) {
        this.c = c;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate에 필요한 객체 inflater를 Context에서 추출한다
    }

    @Override
    public int getCount() { //listview에 보여질 항목의 개수를 결정!
        return data.size();
    }

    @Override
    public Object getItem(int position) { //매개변수로 주어진 position번째의 데이터(항목)를 반환
        return data.get(position);
    }

    @Override
    public long getItemId(int position) { //position번째의 id값을 반환
        return position;
    }

    //adapter의 핵심 *** 모든일은 이곳에서 처리됨.
    //항목하나를 만들때 마나 getView가 하나씩 호출 된다.
    //position = 지금 inflate 하고 있는 view의 index
    //convertView = 이전에 만들어진 view , 이전에 inflate된 view
   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       //getView의 역할
       //1. layout을 inflate시켜 view로 만든다.
       //2. arraylist에 들어있는 데이터로 꾸며준다.
       //3. 완성된 view를 return하여 listview에 한칸씩 추가한다.
       //getView는 항목하나를 만들때마다 한번씩 호출!
       //항목을 만들때마다 템플릿을 inflate 해버리면 부하가 걸려서 버벅거릴 수 있음.
       //최초 1번만 inflate하고 만들어진 view를 재활용!

       if(convertView == null) { //최초실행 이라면
           convertView = inflater.inflate(layout, parent, false);
           //layout => view로 만들고 싶은 xml의 주소값
           //parent => view가 적재될 listview
           //false => parent에 대한 종속성
       }

       //이미지 추가
       ImageView img = convertView.findViewById(R.id.imageView);
       img.setImageResource(data.get(position).getImgID());

       //이름 추가
       TextView tv_name = convertView.findViewById(R.id.tv_name);
       tv_name.setText(data.get(position).getName());

       //연락처 추가
       TextView tv_phone = convertView.findViewById(R.id.tv_phone);
       tv_phone.setText(data.get(position).getPhone());

        return convertView;
    }
}

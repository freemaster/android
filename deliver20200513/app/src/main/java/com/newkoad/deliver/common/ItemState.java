package com.newkoad.deliver.common;

import org.json.JSONException;
import org.json.JSONObject;


public class ItemState  {

    public String state;
    public String name;
    public String id;
    public String addr;
    public String datetime;
    public int deliy_price;
    public int cash_price ;

    public int adver_price;
    public int move_min;

    public ItemState(JSONObject json){

        try {
            this.state = json.getString("state");
            this.name = json.getString("name");
            this.id = json.getString("id");
            this.addr= json.getString("addr");
            this.datetime= json.getString("datetime");
            this.deliy_price = json.getInt("deliy_price");
            this.cash_price = json.getInt("cash_price");
            this.adver_price = json.getInt("adver_price");
            this.move_min= json.getInt("move_min");

        }catch (JSONException e){
            e.printStackTrace();
        }

    }



}

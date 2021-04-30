package com.newkoad.deliver.common;

import org.json.JSONException;
import org.json.JSONObject;


public class DepoDriverItem {

    public String datetime;
    public String name;
    public String id ;
    public int price;
    public int kind;

    public DepoDriverItem(JSONObject json) {
        try {

            this.datetime = json.getString("datetime") ;
            this.name = json.getString("name") ;
            this.id = json.getString("id") ;
            this.price = json.getInt("price");
            this.kind = json.getInt("kind");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}

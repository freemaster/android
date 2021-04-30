package com.newkoad.deliver.common;

import org.json.JSONException;
import org.json.JSONObject;


public class ItemRider {

    int key;
    int level;
    String id;
    String name;
    String lastlogin;
    String tax ;
    String dd_id;
    String dd_rent_type;
    int dd_payment_day;
    int dd_total_payment;
    int dd_pre_payment;
    int dd_months_payment;
    String dd_contract_date;
    String att ;

    public ItemRider(JSONObject json){
        try {
            key = json.getInt("mem_id");
            level = json.getInt("level");
            id = json.getString("id");
            name = json.getString("name");
            lastlogin = json.getString("lastlogin");
            tax = json.getString("tax");
            dd_id = json.getString("dd_id");
            dd_rent_type = json.getString("dd_rent_type");
            dd_payment_day = json.getInt("dd_payment_day");
            dd_total_payment = json.getInt("dd_total_payment");
            dd_pre_payment = json.getInt("dd_pre_payment");
            dd_months_payment = json.getInt("dd_months_payment");
            dd_contract_date = json.getString("dd_contract_date");
            att = json.getString("att");

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}

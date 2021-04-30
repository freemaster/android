package com.newkoad.deliver;

import org.json.JSONException;
import org.json.JSONObject;

public class Msg {

    public char type;
    int key;
    int fromKey;
    int toKey;

    String title;
    String text ;
    String time;

    public Msg(char type, int key, int from, int to,  String text, String time){
        this.type = type;
        this.key = key;
        this.fromKey = from;
        this.toKey = to;
        this.text = text;
        this.time = time;

    }

    public Msg(JSONObject json){

        try {

            this.type = json.getString("type").charAt(0);
            this.key = json.getInt("key");

            this.fromKey = json.getInt("from");
            this.toKey = json.getInt("to");

            this.text = json.getString("text");
            this.time = json.getString("time");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public char getType() {
        return type;
    }
    public int getKey() {
        return key;
    }

    public int getFromKey() {
        return fromKey;
    }
    public int getToKey() {
        return toKey;
    }

    public String getText() {
        return text;
    }
    public String getTime() {
        return time;
    }
}

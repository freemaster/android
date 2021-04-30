package com.newkoad.deliver.common;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;



public class Driver  implements Parcelable {

    public int key;

    public String level;
    public String name;
    public String id;
    public String state;
    public String attend;

    public Driver(JSONObject json){
        try{
            this.key = json.getInt("key");
            this.level = json.getString("level");
            this.name = json.getString("name");
            this.id = json.getString("id");
            this.state = json.getString("state");
            this.attend = json.getString("attend");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    protected Driver(Parcel in) {
        key = in.readInt();
        level = in.readString();
        name = in.readString();
        id = in.readString();
        state = in.readString();
        attend = in.readString();
    }

    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(key);
        dest.writeString(level);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(state);
        dest.writeString(attend);
    }
}

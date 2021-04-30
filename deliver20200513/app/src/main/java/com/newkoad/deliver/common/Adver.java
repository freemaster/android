package com.newkoad.deliver.common;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Adver extends Item implements Parcelable {


    String title ;
    String receiver ;
    String phone;
    String addr;

    int ad_price ;

    int adver_time ;
    int pass_time;




    public Adver(){


    }


    public Adver(JSONObject json) {

        try {
            super.key = json.getInt("key");
            super.type = json.getString("type");
            super.status = json.getInt("dly_status");

            this.title = json.getString("title");
            this.receiver = json.getString("receiver");
            this.phone = json.getString("phone");
            this.addr = json.getString("addr");

            this.ad_price = json.getInt("ad_price");
            this.adver_time = json.getInt("adver_time");
            this.pass_time = json.getInt("pass_time");


        }catch (JSONException e){
            e.printStackTrace();
        }
    }





    public Adver(Parcel parcel){
        super.key = parcel.readInt();
        super.status = parcel.readInt();
        super.type = parcel.readString();

        this.title = parcel.readString();
        this.receiver = parcel.readString();
        this.phone = parcel.readString();
        this.addr = parcel.readString();

        this.ad_price = parcel.readInt();

        this.adver_time = parcel.readInt();
        this.pass_time = parcel.readInt();

    }


    public static final Parcelable.Creator<Adver> CREATOR = new Parcelable.Creator<Adver>(){

        @Override
        public Adver createFromParcel(Parcel source) {    return  new Adver(source);       }

        @Override
        public Adver[] newArray(int size) {
            return new Adver[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(key);
        dest.writeInt(status);
        dest.writeString(type);

        dest.writeString(this.title);
        dest.writeString(this.receiver);
        dest.writeString(this.phone);
        dest.writeInt(this.ad_price);

        dest.writeInt(this.adver_time);
        dest.writeInt(this.pass_time);

    }




    public String getTitle() {        return title;    }
    public String getReceiver() {        return receiver;    }
    public String getPhone() {        return phone;    }
    public String getAddr() {    return addr;    }
    public int getAdPrice() {        return ad_price;    }
    public int getAdver_time() {        return adver_time;    }
    public int getPass_time() {        return pass_time;    }




    public void setTitle(String title) {
        this.title = title;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }


    public void setAdver_time(int adver_time) {
        this.adver_time = adver_time;
    }

    public void setPass_time(int pass_time) {
        this.pass_time = pass_time;
    }

    public void setNow_dist(float now_dist) {
        //this.now_dist = now_dist;
    }

    public void setMove_dist(float move_dist) {
       // this.move_dist = move_dist;
    }

    public void setAdPrice(int price) {
        this.ad_price = price;
    }
}

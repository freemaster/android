package com.newkoad.deliver.common;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Order extends Item implements Parcelable{


    private int sKey;
    private String sTid;
    private String sId;
    private String sName ;
    private String sPhone ;
    private String sTel ;
    private String sRoad ;
    private String sLand ;

    public String sZipcode;
    public String sPoints;


    public String sOwner;
    public String sNumber;


    private String ePhone ;
    private String eTel ;
    private String eRoad ;
    private String eLand ;
    private String eMemo ;

    public String eZipcode;
    public String ePoints;



    private String color;
    private String payTypeStr ;
    private int payType  ;

    private int dPrice;
    private int gPrice;
    private int rTime;
    private int tPrice;


    public Order() {

    }



    public Order(JSONObject json){
        try {
            super.key = json.getInt("key");
            super.type = json.getString("type");
            super.status = json.getInt("dly_status");
            super.datetime = json.getString("cdatetime");
            super.startTime = json.getLong("dis_stime");

            try{
                super.distance = Float.parseFloat( json.getString("distance"));
            }catch (NumberFormatException e){
                e.printStackTrace();
            }


            this.sId =  json.getString("s_id");
            this.sName =  json.getString("s_name");
            this.sKey =   Integer.parseInt( json.getString("s_key") );

            this.sPhone = json.getString("s_phone");
            this.sTel = json.getString("s_tel");
            this.sRoad = json.getString("s_road");
            this.sLand = json.getString("s_land");
            this.sZipcode = json.getString("s_zipcode");
            this.sPoints =  json.getString("s_points");

            this.sOwner = json.getString("s_ceo_name");
            this.sNumber = json.getString("s_reg_number");
            this.sTid = json.getString("s_tid");

            this.ePhone = json.getString("e_phone");
            this.eTel = json.getString("e_tel");
            this.eRoad =  json.getString("e_road");
            this.eLand =  json.getString("e_land");
            this.eMemo =  json.getString("e_memo");
            this.eZipcode = json.getString("e_zipcode");
            this.ePoints =  json.getString("e_points");

            this.color = "";


            this.payType = json.getInt("dly_pay_type");
            if(payType ==1) {
                this.payTypeStr = "현금";
            }else if(payType == 2) {
                this.payTypeStr = "카드";
            }else if(payType == 3) {
                this.payTypeStr = "선불";
            }else{
                this.payTypeStr = "";
            }

            this.rTime =  json.getInt("rtime");

            this.dPrice = json.getInt("d_price");
            this.gPrice = json.getInt("g_price");
            this.tPrice = json.getInt("t_price");


        }catch (JSONException e){
            e.printStackTrace();
        }

    }


    public void setSPoint(String  spoints){
        this.sPoints = spoints;

    }

    public void setEPoint(String  epoints){
        this.ePoints = epoints;

    }




    public Order(Parcel parcel){
        super.key = parcel.readInt();
        super.status = parcel.readInt();
        super.type = parcel.readString();
        super.distance = parcel.readFloat(); // Float
        super.datetime = parcel.readString();

        super.startTime = parcel.readLong();

        this.sId = parcel.readString();
        this.sName = parcel.readString();
        this.sKey = parcel.readInt();

        this.sPhone = parcel.readString();
        this.sTel = parcel.readString();
        this.sRoad = parcel.readString();
        this.sLand = parcel.readString();
        this.sZipcode = parcel.readString();
        this.sPoints = parcel.readString();


        this.ePhone = parcel.readString();
        this.eTel = parcel.readString();
        this.eRoad = parcel.readString();
        this.eLand = parcel.readString();
        this.eMemo = parcel.readString();
        this.eZipcode = parcel.readString();
        this.ePoints = parcel.readString();


        this.color = parcel.readString();
        this.payTypeStr = parcel.readString();
        this.payType = parcel.readInt();

        this.dPrice = parcel.readInt();
        this.gPrice = parcel.readInt();
        this.tPrice = parcel.readInt();

        this.sOwner = parcel.readString();
        this.sNumber = parcel.readString();
        this.sTid = parcel.readString();

        this.rTime = parcel.readInt();

    }


    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
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
        dest.writeFloat(distance);


        dest.writeString(datetime);
        dest.writeLong(startTime);


        dest.writeString(this.sId);
        dest.writeString(this.sName);
        dest.writeInt(this.sKey);

        dest.writeString(this.sPhone);
        dest.writeString(this.sTel);
        dest.writeString(this.sRoad);
        dest.writeString(this.sLand);
        dest.writeString(this.sZipcode);
        dest.writeString(this.sPoints);

        dest.writeString(this.ePhone);
        dest.writeString(this.eTel);
        dest.writeString(this.eRoad);
        dest.writeString(this.eLand);
        dest.writeString(this.eMemo);
        dest.writeString(this.eZipcode);
        dest.writeString(this.ePoints);

        dest.writeString(this.color);
        dest.writeString(this.payTypeStr);
        dest.writeInt(this.payType);

        dest.writeInt(this.dPrice);
        dest.writeInt(this.gPrice);
        dest.writeInt(this.tPrice);

        dest.writeString(this.sOwner);
        dest.writeString(this.sNumber);
        dest.writeString(this.sTid);

        dest.writeInt(this.rTime);

    }

    public String getsTid(){ return  sTid; }

    public String getsId() {
        return sId;
    }
    public String getsName() {
        return sName;
    }
    public int getsKey() {
        return sKey;
    }
    public String getsPhone() {
        return sPhone;
    }
    public String getsTel() { return sTel; }
    public String getsRoad() {
        return sRoad;
    }
    public String getsLand() {
        return sLand;
    }
    public String getsPoints() {
        return sPoints;
    }
    public String getsZipcode() {
        return sZipcode;
    }
    public String getePhone() {
        return ePhone;
    }
    public String geteTel() {
        return eTel;
    }
    public String geteRoad() {
        return eRoad;
    }
    public String geteLand() {return eLand;}
    public String geteMemo() {return eMemo;}
    public String getePoints() {
        return ePoints;
    }
    public String geteZipcode() {
        return eZipcode;
    }

    public void setColor( String color ){     this.color = color ;     }
    public String getColor(){   return  this.color ;    }

    public int getPayType(){
        return payType;
    }
    public String getPayTypeStr(){
        return payTypeStr;
    }

    public int getdPrice() {
        return dPrice;
    }
    public int getgPrice() {
        return gPrice;
    }
    public int gettPrice() {
        return tPrice;
    }

    public String getsOwner(){
        return sOwner;
    }
    public String getsNumber(){
        return  sNumber;
    }


    public int getrTime() { return  rTime; }


}

package com.newkoad.deliver;


import android.app.Application;
import android.location.Location;

import com.newkoad.deliver.common.Item;
import com.newkoad.deliver.add.SoundManager;

import java.util.ArrayList;

public class MainApp extends Application {

    public String appName;
    public int versionCode = 0;
    public String versionName = "";
    private String useStorage ="";
    public LoginActivity loginActivity = null;
    public MainActivity mainActivity;
    public ReceiveActivity receiveActivity = null;
    public AllocateActivity allocateActivity = null ;
    private boolean branch = false;
    private boolean addBranch = false;
    private String androidID;
    private int actionKey = 0;
    public int userKey =0 ;
    public int parentKey = 0 ;
    public int groupKey = 0;
    private int orderKey =  0 ;
    public String boxID ;
    public String parentName = "";
    private int savePointTime = 10;
    public String userGroup = "" ;
    private String userID = "";
    private String userPW = "";
    public String userName = "";
    public String userPhoto ="";
    public void setUseStorage(String useStorage) {
        this.useStorage = useStorage;
    }
    public String getUseStorage(){ return  useStorage; }
    public int getSavePointTime(){ return  savePointTime;}
    public void setSavePointTime(int savePointTime){ this.savePointTime = savePointTime; }
    public void setBranch(boolean branch) { this.branch = branch; }
    public boolean getBranch() { return  branch;}
    public void setAddBranch(boolean addBranch) { this.addBranch = addBranch; }
    public boolean getAddBranch() { return  addBranch;}
    public void setActionKey(int actionKey) { this.actionKey = actionKey;  }
    public int getActionKey(){ return  this.actionKey; }
    public void  setOrderKey(int orderKey){ this.orderKey = orderKey; }
    public int getOrderKey(){ return  orderKey; }
    public void setAndroidID( String id){   this.androidID = id;    }
    public String getAndroidID(){ return  androidID; }
    public String getUserPhoto() { return  userPhoto;}
    public void setUserPhoto(String url){ this.userPhoto = "http://ddbox.biz/uploads/member_photo/" + url; }
    public String macAddr;
    public String ipAddr ;
    public String phone;

    public boolean login = false;
    public boolean sound = false;
    public boolean attend = false;
    public boolean rest = false;
    public boolean land = false;
    public String dateTime;
    public String loginTime;
    private String attendTime;
    final static private  String rootURL = "https://ddbox.biz/web_app";
    public ArrayList<Item> allocateList = null ;
    public ArrayList<Location> locationList = new ArrayList<>();
    public ArrayList<String> xList = new ArrayList<>();
    public ArrayList<String> yList = new ArrayList<>();
    public double latitude = 0.0;
    public double longitude = 0.0;
    public double distance  =0.0;
    public Location location = null;
    public int gpsUpdateCnt = 0;
    public boolean traceUpdate = false;
    private String tokenp;
    public boolean act;
    public int re_cnt =0;
    public int allocateCnt = 0 ;
    private int callLimit = 0;
    private int dayLimit  = 0 ;
    public int getAllocateCnt(){ return  allocateCnt ; }
    public void setAllocateCnt(int allocateCnt ) {  this.allocateCnt = allocateCnt;   }
    public int getCallLimit(){ return  callLimit ; }
    public void setCallLimit(int callLimit){ this.callLimit = callLimit ;}
    public int getDayLimit(){ return  dayLimit ; }
    public void setDayLimit(int dayLimit){ this.dayLimit = dayLimit ;}
    public boolean addrRoad = true ;
    public boolean addrLand = false ;
    public int  route_dis = 0 ;
    public int route_time = 0 ;


    @Override
    public void onCreate() {
        super.onCreate();

    }
    final static public String getRootURL(){ return  rootURL; }
    public int getParentKey() { return parentKey;    }
    public int getGroupKey() { return groupKey;    }
    public String getBoxID() { return boxID;    }
    public String getParentName(){ return parentName;}
    public String getPW(){return userPW; }
    public String getUserGroup() {
        return userGroup;
    }
    public String getUserID() {
        return userID;
    }
    public String getUserPW() {
        return userPW;
    }
    public String getUserName() {
        return userName;
    }
    public String getMACPAddr (){ return macAddr;}
    public String getIPAddr (){ return ipAddr;}
    public String getPhone() {return phone;}
    public String getDateTime() {return dateTime;}
    public String getLoginTime() {return loginTime;}
    public String getAttendTime() {return attendTime;}
    public boolean getLogin() { return login; }
    public boolean getAttend() { return  attend; }
    public boolean getRest() { return  rest; }
    public boolean getLand() { return  land; }
    public String getTokenp() { return  tokenp; }
    public void setTokenp(String tokenp){ this.tokenp = tokenp; }
    public void setUserKey(int userKey) { this.userKey = userKey; }
    public int getUserKey() { return this.userKey ; }
    public void setParentKey(int parentKey) {  this.parentKey = parentKey;  }
    public void setGroupKey(int groupKey) {  this.groupKey = groupKey;  }
    public void setBoxID(String boxID) {  this.boxID = boxID;  }
    public void setParentName(String parentName) { this.parentName = parentName ;}
    public void setPW(String pw) {this.userPW = pw; }
    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setMACAddr(String macAddr){ this.macAddr = macAddr; }
    public void setIPAddr(String ipAddr){ this.ipAddr = ipAddr; }
    public void setPhone(String phone){ this.phone = phone; }
    public void setDateTime(String datetime){ this.dateTime = datetime; }
    public void setLoginTime(String logingTime){ this.loginTime = logingTime;}
    public void setAttendTime(String attendtime){ this.attendTime = attendtime; }
    public void setLogin(boolean login){this.login =login;}
    public void setAttend(boolean attend){ this.attend = attend ;}
    public void setRest(boolean rest){ this.rest = rest ;}
    public void setLand(boolean land){ this.land = land ;}
    public void setRoute_dis (int dis) { this.route_dis = dis; }
    public void setRoute_time (int time) { this.route_time = time; }
    public int getRoute_dis(){ return this.route_dis ; }
    public int getRoute_time(){ return this.route_time ; }

    public void logout(){
        this.userKey =  0;
        this.groupKey = 0 ;
        this.parentKey = 0;
        this.boxID = "";

        this.login = false;
        this.attend = false;
        this.rest =false;

    }
    public void setSound(boolean sound){
        this.sound = sound;
    }
    public boolean getSound(){
        return this.sound;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    private SoundManager soundManager = null;
    public void setSoundManager(SoundManager soundManager){
        this.soundManager = soundManager;
    }
    public SoundManager getSoundManager(){ return  soundManager; }
    public void setAllocateActivity(AllocateActivity allocateActivity) {
        this.allocateActivity = allocateActivity;
    }
    public AllocateActivity getAllocateActivity(){
        return this.allocateActivity;
    }

}

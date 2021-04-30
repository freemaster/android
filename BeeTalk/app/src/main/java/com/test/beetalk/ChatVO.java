package com.test.beetalk;

public class ChatVO {
    private int img;
    private String name;
    private String msg;
    private String time;

    public ChatVO(){

    }

    public ChatVO(int img, String name, String msg, String time) {
        this.img = img;
        this.name = name;
        this.msg = msg;
        this.time = time;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }
}

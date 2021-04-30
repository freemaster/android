package com.test.exam_listview;

public class ContantVO {
    //1. 필드
    //2. 생성자
    //3. get/set
    //4. toString

    private int imgID;
    private String name;
    private String phone;

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContantVO() {
    }

    public ContantVO(int imgID, String name, String phone) {
        this.imgID = imgID;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ContantVO{" +
                "imgID=" + imgID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

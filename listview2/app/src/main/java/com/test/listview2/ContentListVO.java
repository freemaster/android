package com.test.listview2;

public class ContentListVO {
    private String name;
    private String link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ContentListVO() {
    }

    public ContentListVO(String name, String link) {
        this.name = name;
        this.link = link;
    }

    @Override
    public String toString() {
        return "ContentListVO{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}

package com.newkoad.deliver.common;

import android.location.Location;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Item {
    public int key = 0;

    public int status  = 0;
    public String type  ="";
    public LinearLayout layout = null;
    public float distance = 0;

    public  String datetime;

    public float getDistance() { return distance;   }

    public int getKey(){ return key ; }
    public String getDatetime(){ return  datetime; }

    public long startTime  = 0;
    public long endTime   =0 ;

}

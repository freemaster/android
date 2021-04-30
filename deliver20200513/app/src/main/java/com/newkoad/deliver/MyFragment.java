package com.newkoad.deliver;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

public class MyFragment extends Fragment {


    protected MainApp mainApp ;
    protected MainActivity mainActivity;
    protected Context context;
    protected String TAG;


    protected  String id;
    protected  String name;
    protected  int dlyCount;
    protected  int revenue ;
    protected  int deposit ;
    protected boolean uppaid ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();

    }


    @Override
    public void onStart() {
        super.onStart();


    }
}

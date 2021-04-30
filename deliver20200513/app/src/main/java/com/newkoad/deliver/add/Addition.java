package com.newkoad.deliver.add;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newkoad.deliver.MainActivity;
import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;

public class Addition extends AppCompatActivity  {

    protected MainApp mainApp ;
    protected Context context;
    protected Intent intent;
    protected TextView headerTitle;
    protected ImageButton headerCloseBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = (MainApp) getApplication();
        context = getApplicationContext();

    }

    protected void setHeaderBlue(LinearLayout header){


        header.setBackgroundResource(R.color.color_blue);

    }

    public void onHeaderCloseBtn(View v){

        finish();
    }



    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    public void openAlert(String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("")
                .setMessage( msg )
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainApp.mainActivity.finish();
                    }
                })
                .create()
                .show();


    }




}

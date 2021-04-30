package com.newkoad.deliver.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.newkoad.deliver.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public  class MyCalendar {

    Context context;
    View v;


    int mYear, mMonth, mDay, mHour, mMinute;
    EditText sDate, eDate;


    public MyCalendar( Context context ,View v){
        this.v  = v;
        this.context = context;

        setCalendar();
    }


    public void setCalendar(){

        sDate = (EditText) v.findViewById(R.id.s_date);
        eDate = (EditText) v.findViewById(R.id.e_date);

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
        eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));

        ImageButton sCalendarBtn = (ImageButton) v.findViewById(R.id.s_calendar_btn);
        sCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, sDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        ImageButton eCalendarBtn = (ImageButton) v.findViewById(R.id.e_calendar_btn);
        eCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, eDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        Button todayBtn= v.findViewById(R.id.today_btn);  // 당일
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));

            }
        });

        Button yesterdayBtn= v.findViewById(R.id.yesterday_btn); // 어제
        yesterdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay -1));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));

            }
        });

        Button oneweekBtn= v.findViewById(R.id.oneweek_btn); // 일주일
        oneweekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay-7));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));


            }
        });

        Button onemonthBtn= v.findViewById(R.id.onemonth_btn); // 한달
        onemonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate.setText(String.format("%d-%d-%d", mYear, (mMonth + 1) -1, mDay));
                eDate.setText(String.format("%d-%d-%d", mYear, mMonth + 1, mDay));


            }
        });


    }

    DatePickerDialog.OnDateSetListener sDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    mYear = year; mMonth = monthOfYear;  mDay = dayOfMonth;

                    sDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
                }
            };


    DatePickerDialog.OnDateSetListener eDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    mYear = year;  mMonth = monthOfYear; mDay = dayOfMonth;

                    eDate.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));

                }
            };


}

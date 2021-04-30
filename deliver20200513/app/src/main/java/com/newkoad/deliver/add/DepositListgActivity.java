package com.newkoad.deliver.add;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.newkoad.deliver.R;
import com.newkoad.deliver.add.DepositActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DepositListgActivity extends AppCompatActivity {

    private static final String TAG ="DepositActivity" ;

    private RecyclerView dataRV;
    private List<DepositActivity.ItemData> dataList = new LinkedList<>();

    Dialog conditionDialog;


    int mYear, mMonth, mDay, mHour, mMinute;
    TextView sDate, eDate;

    Calendar calendar;
    private long minTime =0 , maxTime =0;
    private Date minDate, maxDate;
    DatePicker sDatePicker, eDatePicker;
    DatePickerDialog datePickerDialog;
    String endDateStr;



    TextView addCountTv, addPriceTv, subCcountTv, subPpriceTv;
    String search_type, search_class;

    CheckBox allTypeCk, addCk, subCk;
    CheckBox allClassCk, chargeCk, withdrawCk, adverCk, partCk, deliveryCk, lteCk, mgrCk;

    String resultType = "";
    String resultClass = "";
    String resultTypeStr = "";
    String resultClassStr = "";

    TextView depositTv;
    TextView conditionRefTv, conditionRef2Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_listg);
    }
}

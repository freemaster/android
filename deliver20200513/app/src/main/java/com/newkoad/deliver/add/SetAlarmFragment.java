package com.newkoad.deliver.add;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;


import java.util.ArrayList;


public class SetAlarmFragment extends Fragment {

    private static final String TAG ="SetAlarmFragment" ;
    private MainApp mainApp ;
    private Context context;

    View v;
    View vi;


    SoundManager soundManager = null;
    TextView snameTv;
    CheckBox soundCk, popupCk;
    Button onAlarmBtn, offAlarmBtn;

    private ArrayList<CheckBox> soundCkList  = new ArrayList<>();
    private ArrayList<CheckBox> popupCkList  = new ArrayList<>();

    public SetAlarmFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainApp = (MainApp) getActivity().getApplication();

        soundManager = mainApp.getSoundManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        v = inflater.inflate(R.layout.fragment_set_alarm,container,false);

        onAlarmBtn = (Button) v.findViewById(R.id.on_alarm_btn);
        offAlarmBtn = (Button) v.findViewById(R.id.off_alarm_btn);


        if(soundManager.getSoundUse()){
            onAlarmBtn.setBackgroundResource(R.drawable.select_blue_navy);
            offAlarmBtn.setBackgroundResource(R.drawable.select_navy_blue);
        }else {
            offAlarmBtn.setBackgroundResource(R.drawable.select_blue_navy);
            onAlarmBtn.setBackgroundResource(R.drawable.select_navy_blue);
        }

        onAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlarmBtn.setBackgroundResource(R.drawable.select_blue_navy);
                offAlarmBtn.setBackgroundResource(R.drawable.select_navy_blue);
                soundManager.setSoundUse(true);
            }
        });

        offAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offAlarmBtn.setBackgroundResource(R.drawable.select_blue_navy);
                onAlarmBtn.setBackgroundResource(R.drawable.select_navy_blue);
                soundManager.setSoundUse(false);
            }
        });





        return v; 
    }













}

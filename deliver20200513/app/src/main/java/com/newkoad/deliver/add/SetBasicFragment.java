package com.newkoad.deliver.add;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.newkoad.deliver.MainApp;
import com.newkoad.deliver.R;


public class SetBasicFragment extends Fragment {

    private MainApp mainApp ;
    private Context context;
    View v;

    private SoundManager soundManager;

    Button onAlarmBtn, offAlarmBtn, addrRoadBtn, addrLandBtn, mapNaverBtn, mapDaumBtn;
    Button naviTmapBtn, naviKakaoBtn ;

    public SetBasicFragment() {

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
        v = inflater.inflate(R.layout.fragment_set_basic, container, false);
        onAlarmBtn = (Button) v.findViewById(R.id.on_alarm_btn);
        offAlarmBtn = (Button) v.findViewById(R.id.off_alarm_btn);
        addrRoadBtn = (Button) v.findViewById(R.id.addr_road_btn);
        addrLandBtn = (Button) v.findViewById(R.id.addr_land_btn);




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

        if(mainApp.addrRoad){
            addrRoadBtn.setBackgroundResource(R.drawable.select_blue_navy);
            addrLandBtn.setBackgroundResource(R.drawable.select_navy_blue);
        }else{
            addrLandBtn.setBackgroundResource(R.drawable.select_blue_navy);
            addrRoadBtn.setBackgroundResource(R.drawable.select_navy_blue);

        }

        addrRoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrRoadBtn.setBackgroundResource(R.drawable.select_blue_navy);
                addrLandBtn.setBackgroundResource(R.drawable.select_navy_blue);
                mainApp.addrRoad = true;
                mainApp.addrLand = false;
            }
        });

        addrLandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrRoadBtn.setBackgroundResource(R.drawable.select_navy_blue);
                addrLandBtn.setBackgroundResource(R.drawable.select_blue_navy);
                mainApp.addrRoad = false;
                mainApp.addrLand = true;
            }
        });







        return v;
    }

}

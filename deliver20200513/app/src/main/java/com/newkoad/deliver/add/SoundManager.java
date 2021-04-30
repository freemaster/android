package com.newkoad.deliver.add;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;


import com.newkoad.deliver.R;

public class SoundManager {

    private static final String TAG ="SoundManager" ;
    private static SoundManager sManager;
    private SoundPool soundPool;

    private Context context;
    private boolean soundUse = true ;
    private boolean saved = false;


    int newid = 0;
    int a_ok = 0;

    private SoundManager(){}

    public static SoundManager getInstance(){
        if(sManager==null){
            sManager = new SoundManager();
        }
        return sManager;
    }



    public void setSoundUse(boolean soundUse){

        this.soundUse = soundUse;

    }
    public boolean getSoundUse(){
        return soundUse;
    }


    public void init(Context context){
        this.context=context;

        soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

    }


    public void addAll(){

        newid = soundPool.load(context, R.raw.order_new, 1);
        a_ok = soundPool.load(context, R.raw.allocate_ok, 1);
    }


    public void play(String str) {

        if (str.equals("new")) {
            soundPool.play(newid, 1, 1, 1, 0, 1);

        } else if (str.equals("a_ok")) {
            soundPool.play(a_ok, 1, 1, 1, 0, 1);

        }

    }


}

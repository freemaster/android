package com.newkoad.deliver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmRecever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, SocketIOService.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, SocketIOService.class);
            context.startService(in);
        }
    }

}

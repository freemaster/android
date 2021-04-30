package com.newkoad.deliver;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class SocketIOService extends Service {
    private MainApp mainApp;
    private static String LOG_TAG = "BoundService";
    private String TAG = "SocketIOService";
    private Socket mSocket;
    private IBinder mBinder = new SocketIOService.MyBinder();
    private String tokenp;
    private int userKey;
    private int parentKey;
    private Thread mainThread;
    public static Intent serviceIntent = null;


    public SocketIOService() {
    }




    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");

        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {

        public SocketIOService getService(){
            return SocketIOService.this;
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceIntent = intent;
        showToast(getApplication(), "Start Service");

        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");
                boolean run = true;
                while (run) {
                    try {
                        Thread.sleep(1000 * 60 * 1); // 1 minute
                        Date date = new Date();
                        //showToast(getApplication(), sdf.format(date));
                        sendNotification(sdf.format(date));
                    } catch (InterruptedException e) {
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();


        return super.onStartCommand(intent, flags, startId);
    }

    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_icon)//drawable.splash)
                        .setContentTitle("디디박스가 실행되고 있습니다.")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH);
                        //.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmRecever.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mainApp = (MainApp) getApplication();


        tokenp = (String) mainApp.getTokenp();

        userKey =  mainApp.getUserKey();

        parentKey =  mainApp.getParentKey();


        try {

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, trustAllCerts, null);


            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder().hostnameVerifier(hostnameVerifier).sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            IO.Options opts = new IO.Options();

            opts.callFactory = okHttpClient;

            opts.webSocketFactory = okHttpClient;

            opts.query ="token=" + tokenp  + "&i=" + userKey;

            mSocket = IO.socket("https://redish.tech:8881" , opts );
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on("login", onLogin);
            mSocket.on("msg", onMsg);
            mSocket.on("newcall", onNewcall);
            mSocket.on("allocate", onAllocate);
            mSocket.on("allocate2", onAllocate2);
            mSocket.on("cancelcall", onCancelcall);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);

        } catch(URISyntaxException e) {
            e.printStackTrace();

        }
        catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }



    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            try{

                String obj = " {'id':'"+ userKey +"','room':'b"+ parentKey +"'}" ;

                JSONObject json = new JSONObject( obj);

                mSocket.emit("join", json.toString()  );

            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


        }
    };


    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };


    private Emitter.Listener onMsg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };


    private Emitter.Listener onNewcall = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            Intent intent = new Intent();
            intent.putExtra("Data", "Broadcast Data");
            intent.setAction("receive_change");
            sendBroadcast(intent);

        }
    };


    private Emitter.Listener onCancelcall = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            try {
                String str  = new String( args[0] +"" ) ;

                JSONObject json = new JSONObject( str );

                JSONObject json2 = new JSONObject( json.getString("data").toString() );
                Intent intent = new Intent();

                intent.putExtra("userKey", json2.getInt("userKey") );
                intent.putExtra("orderKey", json2.getInt("orderKey") );

                intent.setAction("cancel_call");
                sendBroadcast(intent);

                Intent intent2 = new Intent();
                intent2.putExtra("Data", "Broadcast Data");
                intent2.setAction("receive_change");
                sendBroadcast(intent2);

            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    };



    private Emitter.Listener onAllocate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            try {
                String str  = new String( args[0] +"" ) ;

                JSONObject json = new JSONObject( str );

                Intent intent = new Intent();
                intent.putExtra("from", json.getInt("from") );
                intent.setAction("allocate_change");
                sendBroadcast(intent);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private Emitter.Listener onAllocate2 = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            try {
                String str  = new String( args[0] +"" ) ;

                JSONObject json = new JSONObject( str );

                Intent intent = new Intent();
                intent.putExtra("forcing", "1" );

                intent.putExtra("from", json.getInt("from") );
                intent.setAction("allocate_change");
                sendBroadcast(intent);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private Emitter.Listener onPickup = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            Intent intent = new Intent();
            intent.putExtra("Data", "Broadcast Data");
            intent.setAction("pickup_change");
            sendBroadcast(intent);

        }
    };


    private Emitter.Listener onFinish = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            Intent intent = new Intent();
            intent.putExtra("Data", "Broadcast Data");
            intent.setAction("finish_change");
            sendBroadcast(intent);

        }
    };





    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            try {
                JSONObject receivedData = (JSONObject) args[0];
                receivedData.getString("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    private Emitter.Listener onMessageReceived2 = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            try {
                JSONObject receivedData = (JSONObject) args[0];
                receivedData.getString("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    public void sendMsg(String msg){
        mSocket.emit("send", msg);
        mSocket.emit("newcall", msg);

    }


    public void sendMsg2(String msg){
        mSocket.emit("go");
        mSocket.emit("send", msg);
        mSocket.emit("newcall", msg);
    }


}

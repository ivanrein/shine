package com.shineapptpa.rei.shine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

//import com.pusher.client.Pusher;
//import com.pusher.client.channel.Channel;
//import com.pusher.client.channel.SubscriptionEventListener;

/**
 * Created by rei on 12/28/2015.
 */
public class NotifPoolService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("notif", "on create notifpool");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   channel = pusher.subscribe("test_channel");
        Log.d("notif", "on start cmd notif pool");

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

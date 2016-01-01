package com.shineapptpa.rei.shine;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.JsonObject;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionState;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rei on 12/28/2015.
 */
public class NotifPoolService extends Service {
    Pusher mPusher;
    Channel channel;
    int mId;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("notif", "on create notifpool");
        mId = 3;
        if(mPusher == null){
            mPusher = new Pusher("2e9ea453cdbd232244a9");
            channel = mPusher.subscribe("test_channel");

            channel.bind("my_event", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    PowerManager mgr = (PowerManager) getApplicationContext()
                            .getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
                    wakeLock.acquire();
                    Log.d("notif", "yey!!");

                    JSONObject jsonData;

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ladies);

                    try {
                        jsonData = new JSONObject(data);
                        builder.setContentTitle(jsonData.getString("title"));
                        builder.setContentText(jsonData.getString("content"));
                    } catch (JSONException e) {
                        builder.setContentTitle("Notif");
                        builder.setContentText("Message not available");
                    }

                    Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addParentStack(HomeActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    NotificationManager notificationManager =
                            (NotificationManager)getSystemService
                                    (getApplicationContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(mId, builder.build());
                    wakeLock.release();

                }
            });
        }
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
        if(mPusher.getConnection().getState() == ConnectionState.DISCONNECTED) {
            mPusher.connect();
        }

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

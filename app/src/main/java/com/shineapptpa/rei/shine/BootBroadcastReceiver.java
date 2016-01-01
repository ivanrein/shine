package com.shineapptpa.rei.shine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rei on 12/28/2015.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NotifPoolService.class);
        Log.d("notif", "boot broadcast started");
        context.startService(serviceIntent);

    }
}

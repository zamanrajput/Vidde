package com.devphics.vidde;


import static android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.devphics.vidde.activities.MainActivity;

public class OnDownloadNotificationClickListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ACTION_NOTIFICATION_CLICKED)){
            context.startActivity(new Intent(context,MainActivity.class));
        }

    }
}

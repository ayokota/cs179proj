package com.jusu.hangout;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by ayoko001 on 2/22/16.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    String TAG="pavan";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType();
        Log.d("pavan", "in gcm intent message " + messageType);
        Log.d("pavan", "in gcm message bundle " + extras);
        if(!extras.isEmpty()) {

        }
    }
}

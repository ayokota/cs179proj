package com.jusu.hangout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jusu.hangout.MainActivity;
import com.jusu.hangout.R;
import com.jusu.hangout.bean.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ayoko001 on 2/24/16.
 */
public class GcmMessageReceiver extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private Map<String, List<Pair>> chatHistory;
    private String friendname;
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage

        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        Pair p =  new Gson().fromJson(message, Pair.class);
        Log.i("gcm", p.getFirst());
        Log.i("gcm", p.getSecond());

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        if(p.getFirst().equals("message") && accountInfo.getString("msg","").equals("0")) {
            System.out.println("here");
            Map<String, String> msg = new Gson().fromJson(p.getSecond(), Map.class);
            friendname = msg.get("user");
            String key = "chat:" + accountInfo.getString("username", "");

            String historyJson = "";
            if(accountInfo.contains(key)) {
                historyJson = accountInfo.getString(key, "");
                System.out.println(historyJson);
                Type type = new TypeToken<Map<String, List<Pair>>>(){}.getType();

                chatHistory = new Gson().fromJson(historyJson, type);
            } else {
                chatHistory = new HashMap<String, List<Pair>>();
            }
            sendNotification(msg.get("message"));

            recordChat(friendname,msg.get("message"));
            System.out.println(new Gson().toJson(chatHistory));
            accountInfo.edit().putString(key, new Gson().toJson(chatHistory)).apply();
            //accountInfo.edit().putString("msg", "0").apply();
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainContent.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Hangout Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void recordChat(String user, String message) {
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage
        String username = accountInfo.getString("username", "");
        List<Pair> history = null;
        Pair p = new Pair (user, message);
        System.out.println(user + message);
        if(chatHistory.containsKey(friendname)) {
            history = chatHistory.get(friendname);
        } else {
            history = new LinkedList<Pair>();
        }
        history.add(p);
        chatHistory.put(friendname, history);
    }
}

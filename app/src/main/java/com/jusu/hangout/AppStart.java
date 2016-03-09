package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppStart extends AppCompatActivity {

    private static final String[] TOPICS = {"global"};

    /**************Handler update UI:start********************/
    void midToast(String str, int showTime)
    {
        Toast toast = Toast.makeText(getApplicationContext(), str, showTime);
        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL , 0, 300);  //set display location
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);     //set font color
        toast.show();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                   //Toast.makeText(getApplicationContext(),"Log In Success!",Toast.LENGTH_SHORT).show();
                    midToast("Log In Success!", Toast.LENGTH_SHORT);
                    break;
                case 2:
                    midToast("Server connected fail, please log in again", Toast.LENGTH_LONG);
                    break;
            }
        }
    };
    /**************Handler update UI:end********************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appstart);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);

        final InstanceID instanceID = InstanceID.getInstance(this);

        new Thread(new Runnable() {
            public void run() {
                try {
                    String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    subscribeTopics(token);
                    accountInfo.edit().putString("gcmtoken", token).apply();
                    //intent.putExtra("gcmToken", token);
                    Log.i("main", token);


//                    /*---------------- add gcm token to pubnub ------------------*/
//                    String url = "";
//                    String status = "";
//
//                    //http://pubsub.pubnub.com/v1/push/sub-key/<SUB_KEY>/devices/<REG_ID>?<OPERATION>=<CHANNEL_LIST>&type=<TYPE>
//
//                    url += "http://pubsub.pubnub.com/v1/push/sub-key/";
//                    url += "sub-c-caec8254-d91f-11e5-8758-02ee2ddab7fe"; //subkey
//                    url += "/devices/";
//                    url += token; //gcm token
//                    url += "?add=";
//                    url += ""; //channel list
//                    url += "&type=gcm";
//
//
//                    status = new httpClient().Post(url, "");
                    /*---------------- add gcm token to pubnub ------------------*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}).start();




        final String uName = accountInfo.getString("username", "");
        final String pWord = accountInfo.getString("password", "");

//        Log.i("usernameeeee", uName);
//        Log.i("passworddddd", pWord);


        Map<String, String> params = new HashMap<String, String>();
        params.put("username", uName);
        params.put("password", pWord);
        params.put("gcmtoken", accountInfo.getString("gcmtoken",""));

        final String json = new Gson().toJson(params);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/userAuthentication",json);
                    //String result = new httpClient().Post("http://localhost:8080/main_server/userAuthentication",json);
                    /***************************************************************/
                    if(uName.equals("")==true && pWord.equals("")==true)    //this should be add into server
                        result = "0";
                    /***************************************************************/


                   if(result.equals("")==false && result.equals("0")==false) {
//                        Toast.makeText(getApplicationContext(),"Log In Success!",Toast.LENGTH_LONG).show();
                        System.out.println("log in successssss");
                        Thread.currentThread().sleep(1000);//Delay ms
                        Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                        message.what = 1;
                        handler.sendMessage(message);
                        accountInfo.edit().putString("fullname", result).apply();        //password
                        Thread.currentThread().sleep(1500);
                        Intent intent = new Intent(AppStart.this, MainContent.class);
                        startActivity(intent);
                        finish();
                    } else if (result.equals("0")) {
                        System.out.println("log in failedddddd");
                        Thread.currentThread().sleep(2000);
                        Intent intent = new Intent(AppStart.this, LoginPage.class);
                        startActivity(intent);
                        finish();
                        return;
                    } else {
                       System.out.println("server no response");
                       Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                       message.what = 2;
                       handler.sendMessage(message);
                       Thread.currentThread().sleep(2000);
                       Intent intent = new Intent(AppStart.this, LoginPage.class);
                       startActivity(intent);
                       finish();
                       return;
                   }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(AppStart.this, LoginPage.class);
//                startActivity(intent);
//                AppStart.this.finish();
//            }
//            //check if the user has login in?
//            //........
//            //Yes-->chat window
//            //No--->login page
//        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy","!!!!!!!!!!!!");
    }

    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        Log.i("subscribing", "subscribing!!@!!!");
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

}

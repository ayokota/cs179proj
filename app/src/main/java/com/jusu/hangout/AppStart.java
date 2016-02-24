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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AppStart extends AppCompatActivity {

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
                    accountInfo.edit().putString("gcmtoken", token).apply();
                    //intent.putExtra("gcmToken", token);
                    Log.i("main", token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}).start();
        Intent intent = new Intent(AppStart.this, LoginPage.class);
        startActivity(intent);

//        intent = new Intent(AppStart.this, LoginPage.class);
//        startActivity(intent);
//
//        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);
//
//        final String uName = accountInfo.getString("username", "");
//        final String pWord = accountInfo.getString("password", "");
////        Log.i("usernameeeee", uName);
////        Log.i("passworddddd", pWord);
//
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("username", uName);
//        params.put("password", pWord);
//        final String json = new Gson().toJson(params);
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/userAuthentication",json);
//                    //String result = new httpClient().Post("http://localhost:8080/main_server/userAuthentication",json);
//                    /***************************************************************/
//                    if(uName.equals("")==true && pWord.equals("")==true)    //this should be add into server
//                        result = "0";
//                    /***************************************************************/
//
//
//                    if (result.equals("0")) {
//                        System.out.println("log in failedddddd");
//                        Thread.currentThread().sleep(2000);
//                        Intent intent = new Intent(AppStart.this, LoginPage.class);
//                        //Intent intent = new Intent(AppStart.this, getGcmToken.class);
//                        startActivity(intent);
//                        finish();
//                        return;
//                        //} else if(result.equals("1")) {
//                    } else {
////                        Toast.makeText(getApplicationContext(),"Log In Success!",Toast.LENGTH_LONG).show();
//                        System.out.println("log in successssss");
//                        Thread.currentThread().sleep(1000);//Delay ms
//                        Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
//                        message.what = 1;
//                        handler.sendMessage(message);
//                        Thread.currentThread().sleep(1500);
//                        Intent intent = new Intent(AppStart.this, MainContent.class);
//                        //Intent intent = new Intent(AppStart.this, getGcmToken.class);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


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

}

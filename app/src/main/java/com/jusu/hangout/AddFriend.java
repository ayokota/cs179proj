package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.lang.reflect.Type;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tridolphan on 2/28/2016.
 */

public class AddFriend extends AppCompatActivity {



    /**************Handler update UI:end********************/
    String addusername= "";
    boolean userName = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriends);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage


        final EditText username = (EditText) findViewById(R.id.AddUser);
        final Button SubmitButton = (Button) findViewById(R.id.Submit);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    userName = true;
                } else
                    userName = false;

                addusername = s.toString();
                if (userName == true) {
                    SubmitButton.setEnabled(true);
                } else {
                   SubmitButton.setEnabled(false);

                }
            }
        });


        SubmitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("check", addusername);
                final String json = new Gson().toJson(params);
                System.out.println(json);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json);
                            System.out.println(result);
                            if (result.equals("0")){
                                System.out.println("FAILED");}
                            else {
                                System.out.println("GOOD");
                                Map<String, String> param1 = new HashMap<String, String>();
                                param1.put("user1", accountInfo.getString("username", ""));
                                String json1 = new Gson().toJson(param1);
                                System.out.println(json1);
                                result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json1);
                                System.out.println(result);
                                Type type = new TypeToken<Map<String, String>>(){}.getType();
                                Map<String, String> input = new Gson().fromJson(result, Map.class);
                                if (input.containsKey(addusername))
                                {
                                    System.out.println("Already In Friend List");
                                }
                                else{
                                    System.out.println("Not in Friend List");
                                    Map<String, String> param2 = new HashMap<String, String>();
                                    param2.put("user1", accountInfo.getString("username", ""));
                                    param2.put("user2", addusername);
                                    param2.put("status", "requested");
                                    String json2 = new Gson().toJson(param2);
                                    System.out.println(json2);
                                    result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json2);
                                    Intent intent = new Intent(AddFriend.this, MainContent.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            /*if (result.equals("0")) {
                                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                message.what = 0;
                                handler.sendMessage(message);
                                return;
                            } else if (result.equals("1")) {
//                                accountInfo.edit().putString("password", newpassword).apply();        //update new password into local storage
                                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                message.what = 1;
                                handler.sendMessage(message);
                                Intent intent = new Intent(AccountSettings.this, LoginPage.class);
                                Thread.currentThread().sleep(2000);
                                startActivity(intent);
                                finish();
                            }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }
}

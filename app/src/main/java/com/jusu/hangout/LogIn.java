package com.jusu.hangout;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class LogIn extends AppCompatActivity {


    //EditText userNameInput = (EditText) findViewById(R.id.login_user_edit);
    //EditText passwordInput = (EditText) findViewById(R.id.login_passwd_edit);
    //Button logInButton = (Button) findViewById(R.id.loginbutton);

    boolean userName = false;
    boolean userPassword = false;

    String uName = "";
    String pWord = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void clickFunction(View view) {

        System.out.println(uName);
        System.out.println(pWord);
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        startActivity(intent);

    }

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, LoginPage.class);
        //Log.i("Login page", "finish");
        startActivity(intent);

        //this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /***************************************************************/
//        Intent intent = new Intent(LogIn.this, MainContent.class);      //just for test, delete in later version.
//        startActivity(intent);
        /***************************************************************/

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);



        EditText userNameInput = (EditText) findViewById(R.id.login_user_edit);
        EditText passwordInput = (EditText) findViewById(R.id.login_passwd_edit);

        final Button logInButton = (Button) findViewById(R.id.loginbutton);

        logInButton.setEnabled(false);
        userNameInput.addTextChangedListener(new TextWatcher() {
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

                uName = s.toString();
                if (userName == true && userPassword == true) {
                    logInButton.setEnabled(true);
                } else {
                    logInButton.setEnabled(false);

                }
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    userPassword = true;
                } else
                    userPassword = false;

                pWord = s.toString();
                if (userName == true && userPassword == true) {
                    logInButton.setEnabled(true);
                } else {
                    logInButton.setEnabled(false);

                }
            }
        });


        logInButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", uName);
                params.put("password", pWord);
                final String json = new Gson().toJson(params);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/userAuthentication",json);
                            //String result = new httpClient().Post("http://localhost:8080/main_server/userAuthentication",json);

                            if (result.equals("0")) {
                                System.out.println("log in failed");
                                return;
                            } else  {
                                /************storage the log in account info on local:start****************/
                                accountInfo.edit().putString("username", result).apply();        //username
                                accountInfo.edit().putString("password", pWord).apply();        //password
                                /************storage the log in account info on local:end******************/
                                //Toast.makeText(getApplicationContext(), "Log In Success!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(LogIn.this, MainContent.class);
                                intent.putExtra("username",uName);
                                startActivity(intent);
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });


    }
//
//    private void sendGet(String url) throws Exception {
//
//        try {
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//            // optional default is GET
//            con.setRequestMethod("GET");
//
//            //add request header
//            con.setRequestProperty("User-Agent", "Mozilla/5.0");
//
//            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'GET' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            //print result
//            System.out.println(response.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy","!!!!!!!!!!!!");
    }

}

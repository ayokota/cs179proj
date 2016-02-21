package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AccountSettings extends AppCompatActivity {

    RelativeLayout passwordLayout,fullNameLayout;

    boolean oldpasswordBool = false;
    boolean newpasswordBool = false;
    boolean renewpasswordBool = false;
    boolean newfullnameBool = false;

    String username = "";

    String oldpassword = "";
    String newpassword = "";
    String repassword = "";
    String oldfullname = "";
    String newfullname = "";

    private GoogleApiClient client;


    boolean passwordDisplayFlag = false;

    private boolean canSubmit() {
        return oldpasswordBool && newpasswordBool && renewpasswordBool;
    }

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //To load and update the account info
        accountInfo.edit().putString("fromsetting", "true").apply();
        startActivity(intent);
        finish();
    }

    /**************Handler update UI:start********************/
    void midToast(String str, int showTime)
    {
        Toast toast = Toast.makeText(getApplicationContext(), str, showTime);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);  //set display location
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.parseColor("#5c81c7"));     //set font color
        toast.show();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Toast.makeText(getApplicationContext(),"Log In Success!",Toast.LENGTH_SHORT).show();
                    midToast("Update failure!", Toast.LENGTH_LONG);
                    break;
                case 1:
                    //Toast.makeText(getApplicationContext(),"Log In Success!",Toast.LENGTH_SHORT).show();
                    midToast("Password has updated. Please re log in!", Toast.LENGTH_LONG);
                    break;
                case 2:
                    //Toast.makeText(getApplicationContext(),"Log In Success!",Toast.LENGTH_SHORT).show();
                    midToast("Full name has updated!", Toast.LENGTH_SHORT);
                    break;
            }
        }
    };
    /**************Handler update UI:end********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //To load and update the account info

        username = accountInfo.getString("username", "");
        oldfullname = accountInfo.getString("fullname", "");

        TextView settingsTitle = (TextView) findViewById(R.id.settingsTitle);

        passwordLayout = (RelativeLayout) findViewById(R.id.passwordLayout);
        fullNameLayout = (RelativeLayout) findViewById(R.id.fullNameLayout);

        final EditText oldPassword = (EditText) findViewById(R.id.oldPassword);
        final EditText newPassword = (EditText) findViewById(R.id.newPassword);
        final EditText reNewPassword = (EditText) findViewById(R.id.reNewPassword);
        final EditText newFullName = (EditText) findViewById(R.id.newFullName);

        final TextView currentFullName = (TextView) findViewById(R.id.currentFullName);

        final Button passwordSubmitButton = (Button) findViewById(R.id.passwordsubmit);

        final Button fullNameSubmitButton = (Button) findViewById(R.id.fullnamesubmit);

        final ImageButton passwordVisibleButton = (ImageButton) findViewById(R.id.passwordVisibleButton);

        passwordSubmitButton.setEnabled(false);
        fullNameSubmitButton.setEnabled(false);

        Intent i = getIntent();
        settingsTitle.setText("Change" + i.getStringExtra("settings"));

        if(i.getStringExtra("settings").equals("password") == true) {


            passwordLayout.setVisibility(View.VISIBLE);
            fullNameLayout.setVisibility(View.INVISIBLE);
        } else {
            currentFullName.setText("Current full name: "+ oldfullname);
            passwordLayout.setVisibility(View.INVISIBLE);
            fullNameLayout.setVisibility(View.VISIBLE);
        }

        /*------- show/hide password button -------*/


        passwordVisibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordDisplayFlag) {
                    oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordVisibleButton.setImageDrawable(getResources().getDrawable(R.drawable.password_show));
                } else {
                    oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordVisibleButton.setImageDrawable(getResources().getDrawable(R.drawable.password_hide));
                }
                passwordDisplayFlag = !passwordDisplayFlag;
                oldPassword.postInvalidate();
            }
        });
        /*------- show/hide password button -------*/


        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    oldpasswordBool = true;
                } else
                    oldpasswordBool = false;

                oldpassword = s.toString();
                if (canSubmit()) {
                    passwordSubmitButton.setEnabled(true);
                } else {
                    passwordSubmitButton.setEnabled(false);
                }
            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    newpasswordBool = true;
                } else
                    newpasswordBool = false;

                newpassword = s.toString();
                if (canSubmit()) {
                    passwordSubmitButton.setEnabled(true);
                } else {
                    passwordSubmitButton.setEnabled(false);
                }
            }
        });
        reNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    renewpasswordBool = true;
                } else
                    renewpasswordBool = false;

                repassword = s.toString();
                if (canSubmit()) {
                    passwordSubmitButton.setEnabled(true);
                } else {
                    passwordSubmitButton.setEnabled(false);
                }
            }
        });

                /******************* Change Full user name **********************/

        newFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    newfullnameBool = true;
                } else
                    newfullnameBool = false;

                newfullname = s.toString();
                if (newfullnameBool) {
                    fullNameSubmitButton.setEnabled(true);
                } else {
                    fullNameSubmitButton.setEnabled(false);
                }
            }
        });

        passwordSubmitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(username);
//                System.out.println(password);
//                System.out.println(repassword);
//                System.out.println(fullname);
//                System.out.println(birthdate);

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("newpassword", newpassword);
//                params.put("repassword", repassword);
                params.put("oldpasword", oldpassword);

                final String json = new Gson().toJson(params);
                System.out.println(json);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/profUpdate", json);
                            System.out.println(result);
                            if (result.equals("0")) {
                                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                message.what = 0;
                                handler.sendMessage(message);
                                //someshit went wrong
                                return;
                            } else if (result.equals("1")) {
                                accountInfo.edit().putString("password", newpassword).apply();        //update new password into local storage
                                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                message.what = 1;
                                handler.sendMessage(message);
                                Intent intent = new Intent(AccountSettings.this, LoginPage.class);
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
        fullNameSubmitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(username);
//                System.out.println(password);
//                System.out.println(repassword);
//                System.out.println(fullname);
//                System.out.println(birthdate);

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("newfullname", newfullname);

                final String json = new Gson().toJson(params);
                System.out.println(json);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/profUpdate",json);
                            System.out.println(result);
                            if (result.equals("0")) {
                                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                message.what = 0;
                                handler.sendMessage(message);
                                //someshit went wrong
                                return;
                            } else if(result.equals("1")) {
                                accountInfo.edit().putString("fullname", newfullname).apply();        //update new fullname into local storage
//                                currentFullName.setText("Current full name: "+ newfullname);
                                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                message.what = 2;
                                handler.sendMessage(message);
                                Intent intent = new Intent(AccountSettings.this, MainContent.class);
                                accountInfo.edit().putString("fromsetting", "true").apply();
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SignUp Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jusu.hangout/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SignUp Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jusu.hangout/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}


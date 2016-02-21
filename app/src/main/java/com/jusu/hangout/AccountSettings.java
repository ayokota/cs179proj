package com.jusu.hangout;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    String newfullname = "";

    private GoogleApiClient client;


    boolean passwordDisplayFlag = false;

    private boolean canSubmit() {
        return oldpasswordBool && newpasswordBool && renewpasswordBool;
    }

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        //Log.i("Login page", "finish");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        TextView settingsTitle = (TextView) findViewById(R.id.settingsTitle);

        passwordLayout = (RelativeLayout) findViewById(R.id.passwordLayout);
        fullNameLayout = (RelativeLayout) findViewById(R.id.fullNameLayout);

        final EditText oldPassword = (EditText) findViewById(R.id.oldPassword);
        final EditText newPassword = (EditText) findViewById(R.id.newPassword);
        final EditText reNewPassword = (EditText) findViewById(R.id.reNewPassword);
        final EditText newFullName = (EditText) findViewById(R.id.newFullName);


        final Button passwordSubmitButton = (Button) findViewById(R.id.passwordsubmit);

        final Button fullNameSubmitButton = (Button) findViewById(R.id.fullnamesubmit);


        final ImageButton passwordVisibleButton = (ImageButton) findViewById(R.id.passwordVisibleButton);


        Intent i = getIntent();
        settingsTitle.setText("Change" + i.getStringExtra("settings"));
        username = i.getStringExtra("username");

        if(i.getStringExtra("settings") == "password") {


            passwordLayout.setVisibility(View.VISIBLE);
            fullNameLayout.setVisibility(View.INVISIBLE);
        } else {
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
                params.put("username", "owen");
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
                                //someshit went wrong
                                return;
                            } else if (result.equals("1")) {
                                Intent intent = new Intent(AccountSettings.this, LoginPage.class);
                                startActivity(intent);
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
                                //someshit went wrong
                                return;
                            } else if(result.equals("1")) {
//                                Intent intent = new Intent(AccountSettings.this, LoginPage.class);
//                                startActivity(intent);
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
}


package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {


    String username = "";
    String password = "";
//    String repassword = "";
    String fullname = "";
    String sex = "";
//    String birthdate = "";

    boolean usernameBool = false;
    boolean passwordBool = false;
    boolean repasswordBool = false;
    boolean fullnameBool = false;
    boolean sexBool = false;
//    boolean birthdateBool = false;


    boolean passwordDisplayFlag = false;

    private RadioGroup sexInput;
    private RadioButton radioSexButton;
    private RadioButton maleInput, femaleInput;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, LoginPage.class);
        //Log.i("Login page", "finish");
        startActivity(intent);
    }

    private boolean canSubmit() {
        return usernameBool && passwordBool && fullnameBool && sexBool;
    }

//    private boolean canSubmit() {
//        return usernameBool && passwordBool && repasswordBool &&
//                fullnameBool && sexBool;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);


        final EditText usernameInput = (EditText) findViewById(R.id.username);
        final EditText passwordInput = (EditText) findViewById(R.id.password);
//        EditText repasswordInput = (EditText) findViewById(R.id.repassword);
        EditText fullnameInput = (EditText) findViewById(R.id.fullname);

        final Button submitButton = (Button) findViewById(R.id.passwordsubmit);

        final ImageButton passwordVisibleButton = (ImageButton) findViewById(R.id.pwVisibleButton);

        submitButton.setEnabled(false);

        /*-------- sex radio button group ---------*/
        sexInput = (RadioGroup) findViewById(R.id.sex);
        maleInput = (RadioButton) findViewById(R.id.m);
        femaleInput = (RadioButton) findViewById(R.id.f);

        sexInput.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.m) {
                    //Toast.makeText(getApplicationContext(), "m", Toast.LENGTH_SHORT).show();
                    sex = "m";
                    sexBool = true;
                    maleInput.setTextColor(0xFF000000);
                    femaleInput.setTextColor(0xFFA6B1C8);
                    if (canSubmit()) {
                        submitButton.setEnabled(true);
                    } else {
                        submitButton.setEnabled(false);
                    }
                } else if (checkedId == R.id.f) {
                    //Toast.makeText(getApplicationContext(), "f", Toast.LENGTH_SHORT).show();
                    sex = "f";
                    sexBool = true;
                    maleInput.setTextColor(0xFFA6B1C8);
                    femaleInput.setTextColor(0xFF000000);
                    if (canSubmit()) {
                        submitButton.setEnabled(true);
                    } else {
                        submitButton.setEnabled(false);
                    }
                }
            }
        });

        /*-------- sex radio button group ---------*/


        /*------- show/hide password button -------*/


        passwordVisibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordDisplayFlag) {
                    passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordVisibleButton.setImageDrawable(getResources().getDrawable(R.drawable.password_show));
                } else {
                    passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordVisibleButton.setImageDrawable(getResources().getDrawable(R.drawable.password_hide));
                }
                passwordDisplayFlag = !passwordDisplayFlag;
                passwordInput.postInvalidate();
            }
        });
        /*------- show/hide password button -------*/





        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    usernameBool = true;
                } else
                    usernameBool = false;

                username = s.toString();
                if (canSubmit()) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }

                Intent intent = new Intent(SignUp.this, AccountSettings.class);
                intent.putExtra("username", "username");
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
                    passwordBool = true;
                } else
                    passwordBool = false;

                password = s.toString();
                if (canSubmit()) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }
            }
        });

//        repasswordInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    repasswordBool = true;
//                } else
//                    repasswordBool = false;
//
//                repassword = s.toString();
//                if (canSubmit()) {
//                    submitButton.setEnabled(true);
//                } else {
//                    submitButton.setEnabled(false);
//                }
//            }
//        });

        fullnameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    fullnameBool = true;
                } else
                    fullnameBool = false;

                fullname = s.toString();
                if (canSubmit()) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }
            }
        });



        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(username);
//                System.out.println(password);
//                System.out.println(repassword);
//                System.out.println(fullname);
//                System.out.println(birthdate);

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
//                params.put("repassword", repassword);
                params.put("fullname", fullname);
                params.put("sex", sex);

                final String json = new Gson().toJson(params);
                System.out.println(json);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/userSignUp",json);
                            System.out.println(result);
                            if (result.equals("0")) {
                                //someshit went wrong
                                return;
                            } else if(result.equals("1")) {
                                /************storage the log in account info on local:start****************/
                                accountInfo.edit().putString("username", username).apply();        //username
                                accountInfo.edit().putString("password", password).apply();        //password
                                accountInfo.edit().putString("fullname", fullname).apply();        //fullname
                                accountInfo.edit().putString("sex", sex).apply();                  //sex
                                /************storage the log in account info on local:end******************/
                                Intent intent = new Intent(SignUp.this, LoginPage.class);
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

package com.jusu.hangout;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {


    String username = "";
    String password = "";
    String repassword = "";
    String fullname = "";
    String sex = "";
    String birthdate = "";

    boolean usernameBool = false;
    boolean passwordBool = false;
    boolean repasswordBool = false;
    boolean fullnameBool = false;
    boolean sexBool = false;
    boolean birthdateBool = false;

    private RadioGroup sexInput;
    private RadioButton radioSexButton;
    private RadioButton maleInput, femaleInput;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    private boolean canSubmit() {
        return usernameBool && passwordBool && repasswordBool &&
                fullnameBool && sexBool;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText usernameInput = (EditText) findViewById(R.id.username);
        EditText passwordInput = (EditText) findViewById(R.id.password);
        EditText repasswordInput = (EditText) findViewById(R.id.repassword);
        EditText fullnameInput = (EditText) findViewById(R.id.fullname);

        final Button submitButton = (Button) findViewById(R.id.submit);

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
                    if (canSubmit()) {
                        submitButton.setEnabled(true);
                    } else {
                        submitButton.setEnabled(false);
                    }
                } else if (checkedId == R.id.f) {
                    //Toast.makeText(getApplicationContext(), "f", Toast.LENGTH_SHORT).show();
                    sex = "f";
                    sexBool = true;
                    if (canSubmit()) {
                        submitButton.setEnabled(true);
                    } else {
                        submitButton.setEnabled(false);
                    }
                }
            }
        });

        /*-------- sex radio button group ---------*/







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

        repasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    repasswordBool = true;
                } else
                    repasswordBool = false;

                repassword = s.toString();
                if (canSubmit()) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }
            }
        });

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
                params.put("repassword", repassword);
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
                                Intent intent = new Intent(SignUp.this, LoginPage.class);
                                startActivity(intent);
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

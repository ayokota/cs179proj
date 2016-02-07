package com.jusu.hangout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends AppCompatActivity {


    //EditText userNameInput = (EditText) findViewById(R.id.login_user_edit);
    //EditText passwordInput = (EditText) findViewById(R.id.login_passwd_edit);

    //Button logInButton = (Button) findViewById(R.id.loginbutton);

    boolean userName = false;
    boolean userPassword = false;

    String uName = "";
    String pWord = "";

    public void clickFunction(View view){

        Log.i("Login button", "Enable");//recordtest

    }

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this,LoginPage.class);
        //Log.i("Login page", "finish");
        startActivity(intent);

        //this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
                if (s.length()>0) {
                    userName = true;
                }
                else
                    userName = false;

                uName = s.toString();
                if(userName==true && userPassword==true) {
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
                if (s.length()>0) {
                    userPassword = true;
                }
                else
                    userPassword = false;

                pWord = s.toString();
                if(userName==true && userPassword==true) {
                    logInButton.setEnabled(true);
                } else {
                    logInButton.setEnabled(false);

                }
            }
        });



        logInButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(uName);
                System.out.println(pWord);
            }
        }) ;


    }
}

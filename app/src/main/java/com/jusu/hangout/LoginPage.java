package com.jusu.hangout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
    }

    public void logIn(View view) {
        Intent intent = new Intent();
        intent.setClass(this,LogIn.class);
        //Log.i("Login page", "finish");
        startActivity(intent);

        //this.finish();
    }
    public void SignUp(View v) {
        Intent intent = new Intent();
        intent.setClass(this, SignUp.class);
        startActivity(intent);
        //this.finish();
    }


}

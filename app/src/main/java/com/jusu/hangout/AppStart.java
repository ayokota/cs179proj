package com.jusu.hangout;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AppStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appstart);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AppStart.this, LoginPage.class);
                startActivity(intent);
                AppStart.this.finish();
            }

            //check if the user has login in?
            //........
            //Yes-->chat window
            //No--->login page



        }, 2000);
    }
}

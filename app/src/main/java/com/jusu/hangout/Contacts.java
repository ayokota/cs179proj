package com.jusu.hangout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sivart Nahp on 2/22/2016.
 */
public class Contacts extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_page);

        TextView chatTitle = (TextView) findViewById(R.id.chatTitle);


        Intent i = getIntent();
        chatTitle.setText("(" + i.getStringExtra("numberid") + ")" + i.getStringExtra("name"));

        //InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);

    }
}

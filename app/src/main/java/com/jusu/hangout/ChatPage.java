package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatPage extends AppCompatActivity {

    private RelativeLayout DialogueLayout;
    private ImageButton functionButton;
    private RelativeLayout bottomNavi;
    private EditText editChatText;
    private RelativeLayout functionNavi;

    public void moveUp(View view) {


        if(functionNavi.getVisibility() == functionNavi.VISIBLE) {

            DialogueLayout.animate().translationYBy(152f).setDuration(200);
            bottomNavi.animate().translationYBy(152f).setDuration(200);
            functionNavi.setVisibility(View.INVISIBLE);
        } else {

            DialogueLayout.animate().translationYBy(-152f).setDuration(200);
            bottomNavi.animate().translationYBy(-152f).setDuration(200);
            functionNavi.setVisibility(View.VISIBLE);

        }
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
    }

    public void moveDown(View view) {
        if (functionNavi.getVisibility() == functionNavi.VISIBLE) {

            DialogueLayout.animate().translationYBy(152f).setDuration(200);
            bottomNavi.animate().translationYBy(152f).setDuration(200);

            functionNavi.setVisibility(View.INVISIBLE);

//            Log.i("moveDown", "actived");
        }
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
    }

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        //Log.i("Login page", "finish");
        startActivity(intent);
        finish();
    }

    public void mapFunction(View view){

        Intent i = getIntent();

        Intent intent = new Intent(ChatPage.this, MapsActivity.class);

        intent.putExtra("numberid", i.getStringExtra("numberid"));

        intent.putExtra("name", i.getStringExtra("name"));

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        TextView chatTitle = (TextView) findViewById(R.id.chatTitle);

        editChatText = (EditText) findViewById(R.id.EditChatText);

        functionButton = (ImageButton) findViewById(R.id.FunctionButton);
        DialogueLayout = (RelativeLayout) findViewById(R.id.DialogueLayout);
        bottomNavi = (RelativeLayout) findViewById(R.id.bottom_navi);
        functionNavi = (RelativeLayout) findViewById(R.id.function_navi);

        functionButton.setClickable(true);
        DialogueLayout.setClickable(true);

        functionNavi.setVisibility(View.INVISIBLE);

        Intent i = getIntent();
        chatTitle.setText("(" + i.getStringExtra("numberid") + ")" + i.getStringExtra("name"));

        //InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);

        editChatText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(functionNavi.getVisibility() == functionNavi.VISIBLE) {

                    DialogueLayout.animate().translationYBy(152f).setDuration(200);
                    bottomNavi.animate().translationYBy(152f).setDuration(200);
                    functionNavi.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy","!!!!!!!!!!!!");
    }
}

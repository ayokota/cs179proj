package com.jusu.hangout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Sivart Nahp on 3/1/2016.
 */
public class EditEvent extends AppCompatActivity {

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //To load and update the account info
        accountInfo.edit().putString("fromsetting", "true").apply();
        startActivity(intent);
        finish();
    }
    String time;
    Date date;
    SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
    String Keyword, Datetime, Topic, Location;

    boolean KeyBool = false;
    boolean DateBool = false;
    boolean TopicBool = false;
    boolean LocBool = false;
    private boolean canEdit() {
        return KeyBool && DateBool && TopicBool && LocBool;
    }

    protected void onCreate(Bundle savedInstanceState) {
        final View dialogView;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        dialogView = layoutInflater.inflate(R.layout.choosetime, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hangout);
        //setContentView(R.layout.choosetime);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage


        final EditText keyworded = (EditText) findViewById(R.id.keywordedit);
        final EditText topiced = (EditText) findViewById(R.id.topicedit);
        final EditText locationed = (EditText) findViewById(R.id.locationedit);
        final Button updatebutton = (Button) findViewById(R.id.updatebutton);
        final TextView timeedit=(TextView) this.findViewById(R.id.timeedit);
        updatebutton.setEnabled(false);
        keyworded.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    KeyBool = true;
                } else
                    KeyBool = false;

                Keyword = s.toString();
                if (canEdit()) {
                    updatebutton.setEnabled(true);
                } else {
                    updatebutton.setEnabled(false);
                }

                }
        });
        topiced.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    TopicBool = true;
                } else
                    TopicBool = false;

                Topic = s.toString();
                if (canEdit()) {
                    updatebutton.setEnabled(true);
                } else {
                    updatebutton.setEnabled(false);
                }

            }
        });
        locationed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    LocBool = true;
                } else
                    LocBool = false;

                Location = s.toString();
                if (canEdit()) {
                    updatebutton.setEnabled(true);
                } else {
                    updatebutton.setEnabled(false);
                }
            }
        });


        updatebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Keyword: " + Keyword + " Topic:" + Topic + " Time" + Datetime + "Loc: " + Location);
                Intent intent = new Intent();
                intent.setClass(EditEvent.this, MainContent.class);
                startActivity(intent);
                finish();
            }
        });


        timeedit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogView.findViewById(R.id.date_time_set).

                        setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {

                                                   DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                                                   TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                                                   Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                                           datePicker.getMonth(),
                                                           datePicker.getDayOfMonth(),
                                                           timePicker.getCurrentHour(),
                                                           timePicker.getCurrentMinute());
                                                   date = calendar.getTime();
                                                   time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                                                   alertDialog.dismiss();
                                                   timeedit.setText(sdfr.format(date) + " " + time, TextView.BufferType.EDITABLE);
                                                   Datetime = sdfr.format(date) + " " + time;
                                                   DateBool = true;
                                                   if (canEdit()) {
                                                       updatebutton.setEnabled(true);
                                                   } else {
                                                       updatebutton.setEnabled(false);
                                                   }
                                               }
                                           }

                        );
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });


    };

}

package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventInfo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView keyText, topicText, timeText, locationText;
    String str="testtest";
    String[] latlng= {};

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        //Log.i("Login page", "finish");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        //EventInfoMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //To load and update the account info

        keyText = (TextView) findViewById(R.id.textViewKey);
        topicText = (TextView) findViewById(R.id.textViewTopic);
        timeText = (TextView) findViewById(R.id.textViewTime);
        locationText = (TextView) findViewById(R.id.textViewLocation);


        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("attendee", accountInfo.getString("username", ""));
        final Map<String, String> eventInfoMap = new HashMap <String, String> ();
        final String json = new Gson().toJson(params1);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    String result2 = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/events", json);
                    Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
                    List<Map<String, String>> input = new Gson().fromJson(result2, type);
                    Map<String, String> IDmap = input.get(0);
                    eventInfoMap.putAll(IDmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            t.start();
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        keyText.setText("key: " + eventInfoMap.get("keyword"));
        topicText.setText("Topic: " + eventInfoMap.get("topic"));
        timeText.setText("Time: " + eventInfoMap.get("time"));
        locationText.setText("Location: " + eventInfoMap.get("location"));

        Log.i("eventInfoMap: ", eventInfoMap.get("keyword"));
        latlng = eventInfoMap.get("location").split(" ");
        Log.i("eventInfoMap: ",eventInfoMap.get("location"));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]))).title("HangOut Place!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1])), 15));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

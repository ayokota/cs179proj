package com.jusu.hangout;

import android.content.Intent;
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

public class EventInfo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView keyText, topicText, timeText, locationText;
    String str="testtest";

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

        keyText = (TextView) findViewById(R.id.textViewKey);
        topicText = (TextView) findViewById(R.id.textViewTopic);
        timeText = (TextView) findViewById(R.id.textViewTime);
        locationText = (TextView) findViewById(R.id.textViewLocation);

        keyText.setText("key: " + str);
        topicText.setText("Topic: " + str);
        timeText.setText("Time: " + str);
        locationText.setText("Location: " + str);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(new LatLng(33.975619, -117.326361)).title("HangOut Place!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.975619, -117.326361), 15));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

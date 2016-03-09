package com.jusu.hangout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class EventTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String str="testtest";
    ListView trackListView;
    boolean listFlag=true;
    RelativeLayout attendLayout;
    Button hideListButton;

    Handler handler = new Handler();


    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        startActivity(intent);
        finish();
    }

    public void hideList(View view) {
        if (listFlag) {
            attendLayout.setVisibility(View.INVISIBLE);
            hideListButton.setText("Show");
            listFlag = false;
        } else {
            attendLayout.setVisibility(View.VISIBLE);
            hideListButton.setText("Hide");
            listFlag = true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_tracking);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        attendLayout = (RelativeLayout) findViewById(R.id.attendeesLayout);
        hideListButton = (Button) findViewById(R.id.hideList);



        trackListView = (ListView) findViewById(R.id.listViewAttendees);
        final ArrayList<String> eventAttendees = new ArrayList<String>();
        eventAttendees.add("aa");
        eventAttendees.add("bb");
        eventAttendees.add("ee");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,eventAttendees);
        trackListView.setAdapter(arrayAdapter);

        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"Tapped"+eventAttendees.get(position), Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(EventTracking.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Send A Request...")
                        .setMessage("Making friend with "+ eventAttendees.get(position)+" ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Button Tapped","Yes");
                                //Todo ---------------------------------------------------------------------------link with Database to add friend
                                Toast.makeText(getApplicationContext(),"Request has been sent successfully!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                updateLocation();
            }
        });
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(new LatLng(33.975619, -117.326361)).title("HangOut Place!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.975619, -117.326361), 15));

    }

    public void updateLocation() {                      // Todo UpdateLocation----------------------------------------!
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        ArrayList<Marker> markers = new ArrayList<Marker>();

        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.9774069, -117.3509356))                 //.position(new LatLng(latFriend, lngFriend))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("me")));

        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.9774069, -117.3609356))                             //.position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Friend1")));

        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.9774069, -117.3409356))                             //.position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Friend2")));

        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int padding = 200;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.animateCamera(cu);

        Log.i("Friends loc info Running","work");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        }, 7000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

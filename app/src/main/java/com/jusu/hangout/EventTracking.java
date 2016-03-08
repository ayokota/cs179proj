package com.jusu.hangout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class EventTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String str="testtest";
    ListView trackListView;
    boolean listFlag=false;
    RelativeLayout attendLayout;
    Button hideListButton;
    Double mylat,mylng;

    String addusername = "";

//    boolean mapLoadFlag =false;

    Handler handler = new Handler();

    /**************Handler update UI:start********************/
    void midToast(String str, int showTime)
    {
        Toast toast = Toast.makeText(getApplicationContext(), str, showTime);
        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL , 0, 300);  //set display location
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);     //set font color
        toast.show();
    }

    private Handler handlerAddFriend = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //Toast.makeText(getApplicationContext(),"Log In Success!",Toast.LENGTH_SHORT).show();
                    midToast("Request has been sent successfully!", Toast.LENGTH_SHORT);
                    break;
                case 2:
                    midToast("Already In Friend List!", Toast.LENGTH_LONG);
                    break;
            }
        }
    };
    /**************Handler update UI:end********************/




    final Map<String, String> attendeeLocation = new HashMap<String, String>();


    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        startActivity(intent);
        finish();
    }

    public void hideList(View view) {
        if (listFlag) {
            attendLayout.setVisibility(View.INVISIBLE);
            hideListButton.setText("List");
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
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage
        mylat = Double.parseDouble(accountInfo.getString("mylat", ""));
        mylng = Double.parseDouble(accountInfo.getString("mylng", ""));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        attendLayout = (RelativeLayout) findViewById(R.id.attendeesLayout);
        hideListButton = (Button) findViewById(R.id.hideList);
        trackListView = (ListView) findViewById(R.id.listViewAttendees);
        final ArrayList<String> eventAttendees = new ArrayList<String>();

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {

                    /*-------- obtaining attendeeList --------*/
                    Map<String, String> getAttendeeMap = new HashMap<String, String>();
                    getAttendeeMap.put("host", accountInfo.getString("username", ""));
                    String getAttendeeMapJson = new Gson().toJson(getAttendeeMap);

                    String eventResponse = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/events", getAttendeeMapJson);
                    System.out.println(eventResponse);

                    Map<String, String> eventMap = new Gson().fromJson(eventResponse, Map.class);

                    /*
                    eventMap.get("id");
                    eventMap.get("host");
                    eventMap.get("keyword");
                    eventMap.get("topic");
                    eventMap.get("time");
                    */
                    List<String> attendeeList = Arrays.asList(eventMap.get("attendees").split(" "));
                    for(String attendee: attendeeList) {
                        Map<String, String> attendee1Map = new HashMap<String, String>();
                        attendee1Map.put("id", eventMap.get("id"));
                        attendee1Map.put("attendee", attendee);
                        String attendee1Json = new Gson().toJson(attendee1Map);
                        String attendee1Response = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/tracking", attendee1Json);
                        System.out.println("attendee1Response:"+ attendee1Response);
                        attendeeLocation.put(attendee,attendee1Response);
                        eventAttendees.add(attendee);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,eventAttendees);
        trackListView.setAdapter(arrayAdapter);

        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"Tapped"+eventAttendees.get(position), Toast.LENGTH_LONG).show();
                addusername = eventAttendees.get(position);
                new AlertDialog.Builder(EventTracking.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Send A Request...")
                        .setMessage("Making friend with "+ eventAttendees.get(position)+" ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Button Tapped", "Yes");
                                //Todo ---------------------------------------------------------------------------link with Database to add friend
//                                Toast.makeText(getApplicationContext(),"Request has been sent successfully!", Toast.LENGTH_LONG).show();
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("check", addusername);
                                final String json = new Gson().toJson(params);
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json);
                                            System.out.println(result);
                                            if (result.equals("0")){
                                                System.out.println("FAILED");}
                                            else {
                                                System.out.println("GOOD");
                                                Map<String, String> param1 = new HashMap<String, String>();
                                                param1.put("user1", accountInfo.getString("username", ""));
                                                String json1 = new Gson().toJson(param1);
                                                System.out.println(json1);
                                                result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json1);
                                                System.out.println(result);
                                                Type type = new TypeToken<Map<String, String>>(){}.getType();
                                                Map<String, String> input = new Gson().fromJson(result, Map.class);
                                                if (input.containsKey(addusername))
                                                {
                                                    System.out.println("Already In Friend List");
                                                    Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                                    message.what = 2;
                                                    handlerAddFriend.sendMessage(message);
                                                    Toast.makeText(getApplicationContext(),"Already In Friend List!", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    System.out.println("Not in Friend List");
                                                    Map<String, String> param2 = new HashMap<String, String>();
                                                    param2.put("user1", accountInfo.getString("username", ""));
                                                    param2.put("user2", addusername);
                                                    param2.put("status", "requested");
                                                    String json2 = new Gson().toJson(param2);
                                                    System.out.println(json2);
                                                    result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json2);
                                                    Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                                    message.what = 1;
                                                    handlerAddFriend.sendMessage(message);
//                                                    Toast.makeText(getApplicationContext(),"Request has been sent successfully!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
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
//                mapLoadFlag = true;
            }
        });
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(new LatLng(33.975619, -117.326361)).title("HangOut Place!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.975619, -117.326361), 15));

    }

    public void updateLocation() {                                                              // Todo UpdateLocation----------------------------------------!
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        ArrayList<Marker> markers = new ArrayList<Marker>();


        for (String attendee : attendeeLocation.keySet()) {
            String[] latlng = attendeeLocation.get(attendee).split(" ");
            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1])))                             //.position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(attendee)));

        }



        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(mylat, mylng))                 //.position(new LatLng(latFriend, lngFriend))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("me")));

//        markers.add(mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(33.9774069, -117.3609356))                             //.position(new LatLng(lat, lng))
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                .title("Friend1")));
//
//        markers.add(mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(33.9774069, -117.3409356))                             //.position(new LatLng(lat, lng))
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                .title("Friend2")));

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
        }, 10000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

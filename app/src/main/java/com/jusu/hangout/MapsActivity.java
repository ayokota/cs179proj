package com.jusu.hangout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements LocationListener {

    //pubnub init string
    String PUBLISH_KEY = "pub-c-6e01a57a-404e-4fdd-9283-5fd4782be8f5";
    String SUBSCRIBE_KEY = "sub-c-caec8254-d91f-11e5-8758-02ee2ddab7fe";
    String CIPHER_KEY = "";
    String SECRET_KEY = "";
    String ORIGIN = "pubsub";
    String AUTH_KEY;
    String UUID;
    Boolean SSL = false;
    Pubnub pubnub;

    boolean handler_flag = false;

    Handler handler = new Handler();

    private Handler pubnubHandler=null;

    JSONObject jsonObjectLocation;  //Location info transfered from Friend

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LocationManager locationManager;
    String provider;

    //Button returnBtn;

    public void returnButton(View view) {
        //Log.i("Login page", "finish");
        handler_flag = true;


        Intent i = getIntent();

        Intent intent = new Intent(MapsActivity.this, ChatPage.class);

        intent.putExtra("numberid", i.getStringExtra("numberid"));

        intent.putExtra("name", i.getStringExtra("name"));

        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {                                        // OnCreate----------------------------------------!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage

        Handler pubnubHandler = new Handler();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);    //Pubnub Setup
        subscribeMessage(accountInfo.getString("username", ""));                                 //Pubnub subscribe function-------------!!Pubnub Subscribe

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            try {
                updateLocation(location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //returnBtn = (Button) findViewById(R.id.ReturnBtn);



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {

    }

    @Override
    public void onLocationChanged(Location location) {

        mMap.clear();
        try {
            updateLocation(location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);

        handler_flag = true;

        finish();
    }



    //Pubnub Subscribe function
    private void subscribeMessage(String subscribeChannel) {

        try {
            pubnub.subscribe(subscribeChannel, new Callback() {

                        @Override
                        public void connectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                            try {
                                jsonObjectLocation = new JSONObject(message.toString());
                                //pubnubHandler.post(runnableUi);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );

        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }

    //Pubnub Publish function
    public void publishMessage(String publishChannel, String latitude, String longitude) {
        /* Publish a simple message to the demo_tutorial channel */
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("locationlat",latitude);
            jsonObject.put("locationlng",longitude);
            System.out.println(jsonObject);

            pubnub.publish(publishChannel, jsonObject, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateLocation(final Location location) throws JSONException {                      // updateLocation----------------------------------------!



        mMap.clear();
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();


        if((jsonObjectLocation != null) && (jsonObjectLocation != null)) {

            Double latFriend = Double.valueOf(jsonObjectLocation.getString("locationlat"));
            Double lngFriend = Double.valueOf(jsonObjectLocation.getString("locationlng"));


//            Log.i("AppInfo", driverLocation.toString());
//
//
//            location.distanceBetween();
//
//            Double distanceInMiles = location.distanceTo(Location.);
//
//            Double distanceOneDP = (double) Math.round(distanceInMiles * 10) / 10;
//
//            infoTextView.setText("Your driver is " + distanceOneDP.toString() + " miles away ");



            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            ArrayList<Marker> markers = new ArrayList<Marker>();

            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latFriend, lngFriend))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Your Friend")));

            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("Me")));

            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }

            LatLngBounds bounds = builder.build();

            int padding = 200;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mMap.animateCamera(cu);

        } else {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title("Me"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
        }

//        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//        try {
//
//            List<Address> listAddress = geocoder.getFromLocation(lat, lng, 1);
//
//            if (listAddress != null && listAddress.size() > 0) {
//                //Log.i("PlaceInfo", listAddress.get(0).toString());
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        publishMessage("locationtest", Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if(handler_flag == false){
                        updateLocation(location);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 5000);

    }


//    // 构建Runnable对象，在runnable中更新界面
//    Runnable   runnableUi=new  Runnable(){
//        @Override
//        public void run() {
//            //更新界面
//
//        }
//    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

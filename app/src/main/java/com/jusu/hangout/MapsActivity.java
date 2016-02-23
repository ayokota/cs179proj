package com.jusu.hangout;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LocationManager locationManager;
    String provider;

    //Button returnBtn;

    public void returnButton (View view){
        //Log.i("Login page", "finish");

        Intent i = getIntent();

        Intent intent = new Intent(MapsActivity.this, ChatPage.class);

        intent.putExtra("numberid", i.getStringExtra("numberid"));

        intent.putExtra("name", i.getStringExtra("name"));

        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

        //returnBtn = (Button) findViewById(R.id.ReturnBtn);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

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

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Your location"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            List<Address> listAddress = geocoder.getFromLocation(lat, lng, 1);

            if(listAddress != null && listAddress.size() > 0) {
                //Log.i("PlaceInfo", listAddress.get(0).toString());
            }

        } catch (IOException e) {
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

        locationManager.removeUpdates(this);

    }

}

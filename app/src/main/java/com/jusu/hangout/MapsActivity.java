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
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements LocationListener {

    Button talkButton;

    SharedPreferences accountInfo;

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

    boolean handler_flag = true;

    Handler handler = new Handler();

//    private Handler pubnubHandler=null;

    JSONObject jsonObjectMessage;  //Location info transfered from Friend

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Location location;
    LocationManager locationManager;
    String provider;


    /************Variables for Walkie Talkie:start**************/
    private static final String TAG = "RecordPlayActivity";
    private Button bt_exit;
    protected int m_in_buf_size;
    private AudioRecord m_in_rec;
    private byte[] m_in_bytes;
    private LinkedList<byte[]> m_in_q;
    private int m_out_buf_size;
    private AudioTrack m_out_trk;
    private byte[] m_out_bytes;
    private Thread record;
    private Thread play;
    private boolean voice_flag = false;
    /************Variables for Walkie Talkie:end****************/

    /************Variables for MapUpdate Talkie:start**************/

    private Thread mapUpdater;
    Double latFriend = null;
    Double lngFriend = null;
    Double lat = null;
    Double lng = null;
    /************Variables for MapUpdate Talkie:end****************/



    public void returnButton(View view) {
        //Log.i("Login page", "finish");
        handler_flag = false;
        voice_flag = false;
        Intent i = getIntent();
        Intent intent = new Intent(MapsActivity.this, ChatPage.class);
        intent.putExtra("numberid", i.getStringExtra("numberid"));
        intent.putExtra("name", i.getStringExtra("name"));
        startActivity(intent);
        finish();
    }

    private Handler uihandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        updateLocation();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    System.out.println("handler back!");
                    break;
                case 2:
                    try {
                        updateMyLocation();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                        // TODO OnCreate----------------------------------------V
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage

//        Handler pubnubHandler = new Handler();

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

        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);                                        //Pubnub Setup
        subscribeMessage(accountInfo.getString("username", ""));                                //Pubnub subscribe function-------------!!Pubnub Subscribe
        //subscribeMessage("locationtest");

        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            try {
                updateMyLocation();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        voiceBufferInit();
        record = new Thread(new recordSound());
        play = new Thread(new playRecord());
        record.start();             // Start Record Thread
        play.start();               // Start Play Thread

        mapUpdater = new Thread(new updateMap()); //Todo Thread for map udate
        mapUpdater.start();                     //Todo Thread for map udate

        talkButton = (Button) findViewById(R.id.starttalkbutton);
//        ButtonListener b = new ButtonListener();
//        talkButton.setOnTouchListener(b);

        talkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (v.getId() == R.id.starttalkbutton) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                Log.d("test", "cansal button ---> cancel");
                                voice_flag = false;
//                        record.interrupt();

//                    try {
//                        record.join();
//                        System.out.println("Join is actived!");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                            }
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                Log.d("test", "cansal button ---> down");
                                voice_flag = true;
//                        record.run();
                                record.run();
                            }
                        }
                    }
                }).start();
                return false;

            }
        });

    }                                                                                           // TODO OnCreate----------------------------------------^

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
        lat = location.getLatitude();
        lng = location.getLongitude();
        try {
            updateMyLocation();
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
        handler_flag = false;
        voice_flag = false;
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
                                jsonObjectMessage = new JSONObject(message.toString());
                                m_out_bytes = hexStringToByteArray(jsonObjectMessage.getString("voc"));
                                jsonObjectMessage.remove("voc");
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

    public void updateLocation() throws JSONException {                      // Todo UpdateLocation----------------------------------------!
        mMap.clear();
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
    }

    public void updateMyLocation() throws JSONException {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("Me"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
    }

    private void voiceBufferInit() {
//        bt_exit = (Button) findViewById(R.id.exitbutton);
        Log.i(TAG, "bt_exit====" + bt_exit);

//        bt_exit.setOnClickListener(this);

        // AudioRecord 得到录制最小缓冲区的大小
        m_in_buf_size = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        // 实例化播放音频对象
        m_in_rec = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, m_in_buf_size);
        // 实例化一个字节数组，长度为最小缓冲区的长度
        m_in_bytes = new byte[m_in_buf_size];
        // 实例化一个链表，用来存放字节组数
        m_in_q = new LinkedList<byte[]>();

        // AudioTrack 得到播放最小缓冲区的大小
        m_out_buf_size = AudioTrack.getMinBufferSize(8000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        // 实例化播放音频对象
        m_out_trk = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, m_out_buf_size,
                AudioTrack.MODE_STREAM);
        // 实例化一个长度为播放最小缓冲大小的字节数组
        m_out_bytes = new byte[m_out_buf_size];
    }

    class updateMap implements Runnable {
        @Override
        public void run() {
            if(jsonObjectMessage != null) {
                try {
                    latFriend = Double.valueOf(jsonObjectMessage.getString("locationlat"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    lngFriend = Double.valueOf(jsonObjectMessage.getString("locationlng"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                message.what = 1;
                uihandler.sendMessage(message);

            } else {
                Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                message.what = 2;
                uihandler.sendMessage(message);
            }
            //publishMessage("locationtest", Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            publishMessage("locationtest", Double.toString(lat), Double.toString(lng));
            //publishMessage("reno", Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    class recordSound implements Runnable{
//        Handler recordhandler = new Handler();

        @Override
        public void run() {
            Log.i(TAG, "........startRecordSound run()......");
            byte[] bytes_pkg;
            // 开始录音
            m_in_rec.startRecording();

            while (voice_flag) {
                m_in_rec.read(m_in_bytes, 0, m_in_buf_size);
                bytes_pkg = m_in_bytes.clone();
//                Log.i(TAG, "........recordSound bytes_pkg==" + bytes_pkg.length);
                if (m_in_q.size() >= 2) {
                    m_in_q.removeFirst();
                }
                m_in_q.add(bytes_pkg);

                String voiceString = bytesToHexString(m_in_q.getFirst());

                publishVoice(accountInfo.getString("username", ""),voiceString);
            }
            m_in_rec.startRecording();
        }
    }

    class playRecord implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.i(TAG, "........playRecordSound run()......");
            byte[] bytes_pkg = null;
            // 开始播放
            m_out_trk.play();

           //while (voice_flag) {
            while (handler_flag) {
                try {
                    //m_out_bytes = m_in_q.getFirst();
//                    bytes_pkg = m_out_bytes.clone();
                    bytes_pkg = m_out_bytes;
                    m_out_trk.write(bytes_pkg, 0, bytes_pkg.length);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    //Pubnub Publish voice function
    public void publishVoice(String publishChannel, String voicemessage) {
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
            jsonObject.put("voc",voicemessage);
            System.out.println(jsonObject);

            pubnub.publish(publishChannel, jsonObject, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Todo Press to talk button
//    class ButtonListener implements View.OnTouchListener{
//        public boolean onTouch(View v, MotionEvent event) {
//            if (v.getId() == R.id.starttalkbutton) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    Log.d("test", "cansal button ---> cancel");
//                    voice_flag = false;
//                    record.interrupt();
//
////                    try {
////                        record.join();
////                        System.out.println("Join is actived!");
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                }
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    Log.d("test", "cansal button ---> down");
//                    voice_flag = true;
//                    record.run();
//                    //record.run();
//                }
//            }
//            return false;
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

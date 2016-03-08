package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.reflect.TypeToken;
import com.pubnub.api.Pubnub;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainContent extends AppCompatActivity implements LocationListener {


    Double lat = null;
    Double lng = null;
    Location location;
    LocationManager locationManager;
    String provider;

    String PUBLISH_KEY = "pub-c-6e01a57a-404e-4fdd-9283-5fd4782be8f5";
    String SUBSCRIBE_KEY = "sub-c-caec8254-d91f-11e5-8758-02ee2ddab7fe";
    Pubnub pubnub;

    ListView chatListView;
    ListView contactsListView;

    ImageView tapTab0;
    ImageView tapTab1;
    ImageView tapTab2;
    ImageView tapTab3;


    String username = "";
    TextView changeFullName, changePassword, userName, fullName;


    RelativeLayout chatLayout, contactsLayout, hangOutLayout, meLayout;

    ArrayList<HashMap<String, Object>> contactsData;
    public static int contactcount = 0;
    public static String contacthold = "";
    public static String status = "";
    public static int contactstart = 0;
    //final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);

    ArrayList<HashMap<String, Object>> hashData;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void clickFunction(View view) {

        ImageView counter = (ImageView) view;

//        System.out.println(counter.getTag().toString());
        int tappedTag = Integer.parseInt(counter.getTag().toString());

        if (tappedTag == 1) {
            tapTab1.setImageResource(R.mipmap.tabbar_contacts_hl);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            contactsLayout.setVisibility(View.VISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            meLayout.setVisibility(View.INVISIBLE);

        } else if (tappedTag == 2) {
            tapTab2.setImageResource(R.mipmap.tabbar_discover_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            hangOutLayout.setVisibility(View.VISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            meLayout.setVisibility(View.INVISIBLE);
        } else if (tappedTag == 3) {
            tapTab3.setImageResource(R.mipmap.tabbar_me_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            meLayout.setVisibility(View.VISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
        } else {
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            chatLayout.setVisibility(View.VISIBLE);
            meLayout.setVisibility(View.INVISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void hostEventView(View view) {
        Intent intent = new Intent(MainContent.this, EventTracking.class);
//        intent.putExtra("settings", "password");
        contactstart = 2;
        startActivity(intent);
        finish();
    }

    public void AttendEventView(View view) {
        Intent intent = new Intent(MainContent.this, EventInfo.class);
//        intent.putExtra("settings", "password");
        contactstart = 2;
        startActivity(intent);
        finish();
    }


    public void AddFriend(View view) {
        Intent intent = new Intent(MainContent.this, AddFriend.class);
        intent.putExtra("settings", "password");
        startActivity(intent);
        finish();
    }

    public void EditEvent(View view) {
        contactstart = 2;
        Intent intent = new Intent(MainContent.this, EditEvent.class);
        startActivity(intent);
        finish();
    }

    public void CreateEvent(View view) {
        contactstart = 2;
        Intent intent = new Intent(MainContent.this, CreateEvent.class);
        startActivity(intent);
        finish();
    }


    public void pressToBroadcastLocation(View view) {
        Intent intent = new Intent(MainContent.this, LocationBroadcastActivity.class);
        startActivity(intent);
        finish();
    }

    public void changeAccountPassword(View view) {
        Intent intent = new Intent(MainContent.this, AccountSettings.class);
        intent.putExtra("settings", "password");
        startActivity(intent);
        finish();
    }

    public void changUserFullName(View view) {
        Intent intent = new Intent(MainContent.this, AccountSettings.class);
        intent.putExtra("settings", "fullname");
        startActivity(intent);
        finish();
    }

    public void logOut(View view) {

        SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage

        accountInfo.edit().putString("username", "").apply();        //CLean up user info
        accountInfo.edit().putString("password", "").apply();
        accountInfo.edit().putString("fullname", "").apply();
        accountInfo.edit().putString("sex", "").apply();

        Intent intent = new Intent(MainContent.this, LoginPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincontent);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage
        /*----- maps -------*/

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();

            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("attendee", accountInfo.getString("username", ""));// change later rn only for ayoko001
                        String json = new Gson().toJson(params);

                        String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/events", json);
                        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
                        List<Map<String, String>> input = new Gson().fromJson(result, type);
                        List<String> eventList = new LinkedList<String>();
                        for(Map<String, String>m : input) {
                            eventList.add(m.get("id"));
                        }
                        for (String s : eventList) {
                            Map<String, String> locMap = new HashMap<String, String>();
                            locMap.put("id", s);// change later rn only for ayoko001
                            locMap.put("attendee", accountInfo.getString("username", ""));// change later rn only for ayoko001
                            locMap.put("location", lat + " " + lng);
                            String locMapJson = new Gson().toJson(locMap);

                            String locMapResult = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/tracking", locMapJson);

                        }
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
        }




        /*------ maps -------- */

//        /*---------------- add gcm token to pubnub ------------------*/
        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);
        pubnub.enablePushNotificationsOnChannel(
                accountInfo.getString("username", ""),
                accountInfo.getString("gcmtoken", ""));
//        String username = accountInfo.getString("username", "");                                                   //get account info in local storage
        userName = (TextView) findViewById(R.id.userNameTextView);
        fullName = (TextView) findViewById(R.id.fullNameTextView);
        userName.setText("username: " + accountInfo.getString("username", ""));
        fullName.setText(accountInfo.getString("fullname", ""));                //this has logic error, we need get the fullname from server


        tapTab0 = (ImageView) findViewById(R.id.img_mainframe);
        tapTab1 = (ImageView) findViewById(R.id.img_contacts);
        tapTab2 = (ImageView) findViewById(R.id.img_discover);
        tapTab3 = (ImageView) findViewById(R.id.img_me);

        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);
        contactsLayout = (RelativeLayout) findViewById(R.id.contactsLayout);
        hangOutLayout = (RelativeLayout) findViewById(R.id.hangOutLayout);
        meLayout = (RelativeLayout) findViewById(R.id.meLayout);


        chatListView = (ListView) findViewById(R.id.chatListView);
        contactsListView = (ListView) findViewById(R.id.contactsListView);

        changeFullName = (TextView) findViewById(R.id.changfullname);
        changePassword = (TextView) findViewById(R.id.changepassword);
        if (accountInfo.getString("fromsetting", "").equals("true") == true) {
            tapTab3.setImageResource(R.mipmap.tabbar_me_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            meLayout.setVisibility(View.VISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            accountInfo.edit().putString("fromsetting", "").apply();
        }
        if (contactstart == 1) {
            tapTab1.setImageResource(R.mipmap.tabbar_contacts_hl);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            contactsLayout.setVisibility(View.VISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            meLayout.setVisibility(View.INVISIBLE);
        }
        if (contactstart == 2) {
            tapTab2.setImageResource(R.mipmap.tabbar_discover_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            hangOutLayout.setVisibility(View.VISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            meLayout.setVisibility(View.INVISIBLE);
        }
        final CustomSimpleAdapter customSimpleAdapter = new CustomSimpleAdapter(MainContent.this, getHashMapData(), R.layout.custom_list_layout);
        final ContactsAdapter customContactsAdapter = new ContactsAdapter(MainContent.this, getContactMapData(), R.layout.custom_contact_layout);
        changeFullName = (TextView) findViewById(R.id.changfullname);

        changePassword = (TextView) findViewById(R.id.changepassword);

        if (accountInfo.getString("fromsetting", "").equals("true") == true) {
            tapTab3.setImageResource(R.mipmap.tabbar_me_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab2.setImageResource(R.mipmap.tabbar_discover);
            meLayout.setVisibility(View.VISIBLE);
            hangOutLayout.setVisibility(View.INVISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            accountInfo.edit().putString("fromsetting", "").apply();
        }


        chatListView.setAdapter(customSimpleAdapter);

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("ID: ", String.valueOf(hashData.get(position).get("id")));
                Intent intent = new Intent(MainContent.this, ChatPage.class);
                intent.putExtra("numberid", String.valueOf(hashData.get(position).get("id")));
                intent.putExtra("name", String.valueOf(hashData.get(position).get("name")));
                startActivity(intent);
                finish();
            }
        });

        contactsListView.setAdapter(customContactsAdapter);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("ID: ", String.valueOf(hashData.get(position).get("id")));
                Intent intent = new Intent(MainContent.this, Contacts.class);
                intent.putExtra("numberid", String.valueOf(contactsData.get(position).get("id")));
                intent.putExtra("name", String.valueOf(contactsData.get(position).get("name")));
                startActivity(intent);
                finish();
            }
        });
       /* AddFriends.setOnClickListener(new Button.OnClickListener()) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainContent.this, AddFriend.class);
                intent.putExtra("settings", "fullname");
                startActivity(intent);
                finish();

        /*------------ meLayout--------------*/

        //EditText username = (EditText) findViewById(R.id.username);
        System.out.println("debug");

        /*------------ meLayout--------------*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainContent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jusu.hangout/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainContent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jusu.hangout/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    // Define a Adapter by myself
    private class CustomSimpleAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, Object>> data;
        private int layoutResource;

        //@param context
        //@param data
        //@param resource
        //@param from
        //@param to 构造函数

        public CustomSimpleAdapter(Context context,
                                   ArrayList<HashMap<String, Object>> data, int resource) {
            super(context, data, resource, null, null);
            this.context = context;
            this.data = data;
            this.layoutResource = resource;
        }

        class ViewHolder {
            ImageView picture;
            TextView number;
            TextView name;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.SimpleAdapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View layoutView = convertView;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            layoutView = layoutInflater.inflate(layoutResource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) layoutView.findViewById(R.id.imageViewLayout);
            viewHolder.number = (TextView) layoutView.findViewById(R.id.number);
            viewHolder.name = (TextView) layoutView.findViewById(R.id.name);

            viewHolder.picture.setImageResource(Integer.parseInt(data.get(position).get("imageView").toString()));
            viewHolder.number.setText(data.get(position).get("id").toString());
//            Log.e("id", data.get(position).get("name").toString());
            viewHolder.name.setText(data.get(position).get("name").toString());
            return layoutView;
        }
    }

    private class ContactsAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, Object>> data;
        private int layoutResource;


        public ContactsAdapter(Context context,
                               ArrayList<HashMap<String, Object>> data, int resource) {
            super(context, data, resource, null, null);
            this.context = context;
            this.data = data;
            this.layoutResource = resource;
        }

        class ViewHolder {
            ImageView picture;
            TextView number;
            TextView name;
            Button add;
            Button delete;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.SimpleAdapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override


        public View getView(final int position, View convertView, ViewGroup parent) {
            final SharedPreferences accountInfo = context.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layoutView = layoutInflater.inflate(layoutResource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) layoutView.findViewById(R.id.imageViewLayout);
            viewHolder.number = (TextView) layoutView.findViewById(R.id.number);
            viewHolder.name = (TextView) layoutView.findViewById(R.id.name);
            viewHolder.add = (Button) layoutView.findViewById(R.id.accept_btn);
            viewHolder.delete = (Button) layoutView.findViewById(R.id.reject_btn);

            //TextView listItemText = (TextView) layoutView.findViewById(R.id.list_item_string);
            //listItemText.setText(data.get(position));

            viewHolder.picture.setImageResource(Integer.parseInt(data.get(position).get("imageView").toString()));
            viewHolder.number.setText(data.get(position).get("id").toString());
//            Log.e("id", data.get(position).get("name").toString());
            viewHolder.name.setText(data.get(position).get("name").toString());
            if (data.get(position).get("name").toString().equals("accepted")) {
                viewHolder.add.setVisibility(View.INVISIBLE);
                viewHolder.delete.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.add.setVisibility(View.VISIBLE);
                viewHolder.delete.setVisibility(View.VISIBLE);
            }
            Button rejectButton = (Button) layoutView.findViewById(R.id.reject_btn);
            Button acceptButton = (Button) layoutView.findViewById(R.id.accept_btn);
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    contactstart = 1;
                    contactsData = new ArrayList<HashMap<String, Object>>();
                    Map<String, String> params = new HashMap<String, String>();
                    // final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);
                    username = accountInfo.getString("username", "");
                    System.out.println(username);
                    params.put("user1", accountInfo.getString("username", ""));// change later rn only for ayoko001
                    params.put("user2", data.get(position).get("id").toString()); // gets users of current spot
                    params.put("status", "rejected");
                    final String json = new Gson().toJson(params);
                    System.out.println(json);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    notifyDataSetChanged();
                    Intent intent = new Intent(MainContent.this, MainContent.class);
                    startActivity(intent);
                    finish();
                }
            });
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    contactstart = 1;
                    contactsData = new ArrayList<HashMap<String, Object>>();
                    Map<String, String> params = new HashMap<String, String>();
                    // final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);
                    username = accountInfo.getString("username", "");
                    System.out.println(username);
                    params.put("user1", accountInfo.getString("username", ""));// change later rn only for ayoko001
                    params.put("user2", data.get(position).get("id").toString());
                    params.put("status", "accepted");
                    final String json = new Gson().toJson(params);
                    System.out.println(json);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    notifyDataSetChanged();
                    Intent intent = new Intent(MainContent.this, MainContent.class);
                    startActivity(intent);
                    finish();
                }
            });
            return layoutView;
        }
    }

    //binding the data and images use the custom adapter defined by myself
    private ArrayList<HashMap<String, Object>> getHashMapData() {
        hashData = new ArrayList<HashMap<String, Object>>();
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);

        Map<String, String> params = new HashMap<String, String>();
        params.put("user1", accountInfo.getString("username", ""));// change later rn only for ayoko001
        final String json = new Gson().toJson(params);

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json);
                    //System.out.println("healdsfjalsjfdlsafasdf!!!!!!!!!!!!!!!!!!!" + json);
                    Log.i("main content", result);

                    Type type = new TypeToken<Map<String, String>>() {
                    }.getType();
                    Map<String, String> resultMap = new Gson().fromJson(result, Map.class);
                    int i = 0;
                    for (String user : resultMap.keySet()) {
                        if (resultMap.get(user).equals("accepted")) {
                            HashMap<String, Object> mItem = new HashMap<String, Object>();
                            mItem.put("id", user);
                            mItem.put("name", user);
                            mItem.put("imageView", R.drawable.default_prof);
                            hashData.add(mItem);
                        }
                    }
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
        return hashData;

    }

    public static int count(String s, char c) {
        return s.length() == 0 ? 0 : (s.charAt(0) == c ? 1 : 0) + count(s.substring(1), c);
    }

    // creating contacts list from server
// creating contacts list from server
    private ArrayList<HashMap<String, Object>> getContactMapData() {
        contactsData = new ArrayList<HashMap<String, Object>>();
        Map<String, String> params = new HashMap<String, String>();
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);
        username = accountInfo.getString("username", "");
        params.put("user1", accountInfo.getString("username", ""));// change later rn only for ayoko001
        final String json = new Gson().toJson(params);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json);
                    result = result.substring(1, result.length() - 1);
                    contactcount = count(result, ',');
                    for (int i = 0; i < contactcount + 1; i++) {
                        HashMap<String, Object> mItem = new HashMap<String, Object>();
                        if (result.contains(":")) {
                            contacthold = result.substring(1, result.indexOf(":") - 1);
                            if (i < contactcount) {
                                status = result.substring(result.indexOf(":") + 2, result.indexOf(",") - 1);
                            } else
                                status = result.substring(result.indexOf(":") + 2, result.length() - 1);
                        }
                        if (result.contains(",")) {
                            result = result.substring(result.indexOf(",") + 1, result.length());
                        }

                        mItem.put("id", contacthold);
                        mItem.put("name", status);

                        mItem.put("imageView", R.drawable.default_prof);

                        if (status == "accepted")
                            mItem.put("status", '0');
                        else if (status == "requested")
                            mItem.put("status", '1');
                        contactsData.add(mItem);
                    }


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


        return contactsData;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);

        Log.i("onDestroy", "!!!!!!!!!!!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage

        lat = location.getLatitude();
        lng = location.getLongitude();

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("attendee", accountInfo.getString("username", ""));// change later rn only for ayoko001
                    String json = new Gson().toJson(params);

                    String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/events", json);
                    Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
                    List<Map<String, String>> input = new Gson().fromJson(result, type);
                    List<String> eventList = new LinkedList<String>();
                    for(Map<String, String>m : input) {
                        eventList.add(m.get("id"));
                    }
                    for (String s : eventList) {
                        Map<String, String> locMap = new HashMap<String, String>();
                        locMap.put("id", s);// change later rn only for ayoko001
                        locMap.put("attendee", accountInfo.getString("username", ""));// change later rn only for ayoko001
                        locMap.put("location", lat + " " + lng);
                        String locMapJson = new Gson().toJson(locMap);

                        String locMapResult = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/tracking", locMapJson);

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





        /*------ maps -------- */
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

}

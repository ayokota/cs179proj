package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tridolphan on 3/7/2016.
 */
public class Search extends AppCompatActivity {

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //To load and update the account info
        accountInfo.edit().putString("fromsetting", "true").apply();
        startActivity(intent);
        finish();
    }



    ListView searchListView;
    ArrayList<HashMap<String, Object>> SearchData;
    final Map<String, String> SearchMap = new HashMap<String, String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        searchListView = (ListView) findViewById(R.id.search_list);


        Intent i = getIntent();

        //InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
        final SearchAdapter searchAdapter = new SearchAdapter(Search.this, getSearchmapData(), R.layout.search_list);
        searchListView.setAdapter(searchAdapter);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("ID: ", String.valueOf(hashData.get(position).get("id")));
                //Intent intent = new Intent(Search.this, Search.class);
                //intent.putExtra("name", String.valueOf(SearchData.get(position).get("name")));
                //startActivity(intent);
                //finish();
            }
        });
    }

    private class SearchAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, Object>> data;
        private int layoutResource;

        //@param context
        //@param data
        //@param resource
        //@param from
        //@param to 构造函数

        public SearchAdapter(Context context,
                             ArrayList<HashMap<String, Object>> data, int resource) {
            super(context, data, resource, null, null);
            this.context = context;
            this.data = data;
            this.layoutResource = resource;
        }

        class ViewHolder {
            TextView name;
            Button viewinfo;
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
            View layoutView = convertView;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            layoutView = layoutInflater.inflate(layoutResource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) layoutView.findViewById(R.id.name);
            viewHolder.viewinfo = (Button) layoutView.findViewById(R.id.viewbutton);
            viewHolder.name.setText(data.get(position).get("name").toString());
            Button viewbutt = (Button) layoutView.findViewById(R.id.viewbutton);
            viewbutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.eventinformation);
                    final TextView Topic = (TextView) findViewById(R.id.topictxt);
                    final Button join = (Button) findViewById(R.id.joinbut);
                    //SearchData = new ArrayList<HashMap<String, Object>>();
                    //Map<String, String> params = new HashMap<String, String>();
                    //params.put("search", data.get(position).get("").toString());
                    //final String json = new Gson().toJson(params);
                    //System.out.println(json);
                    //new Thread(new Runnable() {
                    //public void run() {
                    //      try {
                    //setContentView(R.layout.eventinformation);
                    //String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts",json);
                    //      SearchMap.putAll(new Gson().fromJson(result, Map.class));
                    //        for (String key : SearchMap.keySet()) {
                    Topic.setText(data.get(position).get("name").toString());
                    join.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", data.get(position).get("id").toString());
                            params.put("attendee", accountInfo.getString("username",""));
                            final String json = new Gson().toJson(params);
                            System.out.println(json);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/attendee",json);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            Intent intent = new Intent(Search.this, MainContent.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                            //    }
                            //} catch (Exception e) {
                            //    e.printStackTrace();
                            // }
                            // }
                            //}).start();
                            //do something
                    /*new Thread(new Runnable() {
                        public void run() {
                            try {
                                String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/contacts", json);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();*/
                            notifyDataSetChanged();
                    //Intent intent = new Intent(Search.this, viewevent.class);
                    //startActivity(intent);
                    //finish();
                }
            });

            return layoutView;
        }
    }

    private ArrayList<HashMap<String, Object>> getSearchmapData() {
        SearchData = new ArrayList<HashMap<String, Object>>();
        Map<String, String> params = new HashMap<String, String>();
        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE);
        params.put("search", "");
        final String json = new Gson().toJson(params);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    String result = new httpClient().Post("http://ec2-54-201-118-78.us-west-2.compute.amazonaws.com:8080/main_server/events", json);

                    SearchMap.putAll(new Gson().fromJson(result, Map.class));
                    for (String key : SearchMap.keySet()) {
                        HashMap<String, Object> mItem = new HashMap<String, Object>();
                        mItem.put("name", key);
                        mItem.put("id", SearchMap.get(key));
                        System.out.println(key);
                        SearchData.add(mItem);
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

        return SearchData;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);

        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

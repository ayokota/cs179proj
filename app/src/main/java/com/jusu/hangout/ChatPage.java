package com.jusu.hangout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.jusu.hangout.adapter.CustomChatAdapter;
import com.jusu.hangout.bean.Bean;
import com.jusu.hangout.bean.FriendChatBean;
import com.jusu.hangout.bean.UserChatBean;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;

public class ChatPage extends AppCompatActivity {

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


    String currentChatMsg = "";

    private LinkedList<FriendChatBean> sList = null;
    private LinkedList<UserChatBean> tList = null;
    private LinkedList<Bean> beans = null;


    /** 聊天message 格式 */
    private ListView listView;
    /** 信息编辑框 */
    private EditText edt;

    private CustomChatAdapter adapter;

    private RelativeLayout DialogueLayout;
    private ImageButton functionButton;
    private RelativeLayout bottomNavi;
    private EditText editChatText;
    private RelativeLayout functionNavi;

    private Handler handler=null;

    public void moveUp(View view) {


        if(functionNavi.getVisibility() == functionNavi.VISIBLE) {

            DialogueLayout.animate().translationYBy(152f).setDuration(200);
            bottomNavi.animate().translationYBy(152f).setDuration(200);
            functionNavi.setVisibility(View.INVISIBLE);
        } else {

            DialogueLayout.animate().translationYBy(-152f).setDuration(200);
            bottomNavi.animate().translationYBy(-152f).setDuration(200);
            functionNavi.setVisibility(View.VISIBLE);

        }
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
    }

    public void moveDown(View view) {
        if (functionNavi.getVisibility() == functionNavi.VISIBLE) {

            DialogueLayout.animate().translationYBy(152f).setDuration(200);
            bottomNavi.animate().translationYBy(152f).setDuration(200);

            functionNavi.setVisibility(View.INVISIBLE);

//            Log.i("moveDown", "actived");
        }
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
    }

    public void returnBack(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainContent.class);
        //Log.i("Login page", "finish");
        startActivity(intent);
        finish();
    }

    public void mapFunction(View view){

        Intent i = getIntent();

        Intent intent = new Intent(ChatPage.this, MapsActivity.class);

        intent.putExtra("numberid", i.getStringExtra("numberid"));

        intent.putExtra("name", i.getStringExtra("name"));

        startActivity(intent);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {                                            // OnCreate----------------------------------!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        final SharedPreferences accountInfo = this.getSharedPreferences("com.jusu.hangout", Context.MODE_PRIVATE); //get account info in local storage

        TextView chatTitle = (TextView) findViewById(R.id.chatTitle);
        editChatText = (EditText) findViewById(R.id.EditChatText);
        functionButton = (ImageButton) findViewById(R.id.FunctionButton);
        DialogueLayout = (RelativeLayout) findViewById(R.id.DialogueLayout);
        bottomNavi = (RelativeLayout) findViewById(R.id.bottom_navi);
        functionNavi = (RelativeLayout) findViewById(R.id.function_navi);

        functionButton.setClickable(true);
        DialogueLayout.setClickable(true);

        functionNavi.setVisibility(View.INVISIBLE);

        //创建属于主线程的handler
        handler=new Handler();

        Intent i = getIntent();
        chatTitle.setText("(" + i.getStringExtra("numberid") + ")" + i.getStringExtra("name"));

        editChatText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(functionNavi.getVisibility() == functionNavi.VISIBLE) {

                    DialogueLayout.animate().translationYBy(152f).setDuration(200);
                    bottomNavi.animate().translationYBy(152f).setDuration(200);
                    functionNavi.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        sList = new LinkedList<FriendChatBean>();
        tList = new LinkedList<UserChatBean>();
        beans = new LinkedList<Bean>();
        String[] msg = new String[] { "Why aren't you answering", "Okay.", "You find it yet?",
                "Okay, let me know when you do.", "Sorry, I dropped my phone and I can't find it. I'll text u when I find it.", "...",
                "No.", "..." };

        // 0 是教师； 1 是学生
        for (int loopi = 0; loopi < 4; loopi++) {
            sList.add(new FriendChatBean(msg[loopi], R.drawable.text_people_1,"", 1));
            tList.add(new UserChatBean(msg[loopi + 4], R.drawable.userphoto,"", 0));
        }

        // 归放到 同一个 类集合Bean中
        for (int loopj = 0; loopj < sList.size(); loopj++) {

            beans.add(sList.get(loopj));
            beans.add(tList.get(loopj));
        }
        initViewsMethod();              //chat listview initialized
        onHandleMethod();               //send message function init

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
            }
        });
        adapter = new CustomChatAdapter(this, beans);
        listView.setAdapter(adapter);
        listView.setSelection(beans.size() - 1);

        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);    //Pubnub Setup
        pubnub.enablePushNotificationsOnChannel(
                accountInfo.getString("username", ""),
                accountInfo.getString("gcmtoken", ""));

        subscribeMessage(accountInfo.getString("username", ""),"text");                                 //Pubnub subscribe function---------------------------------------------!!Pubnub Subscribe
    }



    /** 处理listView 的 item方法  */
    private void initViewsMethod(){
        listView = (ListView) findViewById(R.id.chatListView);
        edt = (EditText) findViewById(R.id.EditChatText);

        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);
        listView.setDivider(null);
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                // TODO Auto-generated method stub

                menu.setHeaderTitle("提示：");
                menu.setHeaderIcon(android.R.drawable.stat_notify_error);
                menu.add(0, 0, 1, "删除");
                menu.add(1, 1, 0, "取消");

            }
        });

    }



    /** 处理发送信息的方法  */
    public void onHandleMethod(){

        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(beans.size() - 1);
            }
        });

        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    String txt = edt.getText().toString();
                    if (txt.equals("")) {
                        Toast.makeText(getApplicationContext(), "发送内容不能为空 !", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.addItemNotifiChange(new Bean(txt, R.drawable.userphoto, new Date() + "", 0));
                        publishMessage("reno", txt);
//                        publishMessage("demotest",txt);
                        edt.setText("");
                        listView.setSelection(beans.size() - 1);
                        System.out.println(txt);
                        System.out.println("Enter is pressed");
                        v.clearFocus();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Bean bean = (Bean) adapter.getItem(info.position);
                beans.remove(bean);
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);

    }


    //Pubnub Subscribe function
    private void subscribeMessage(String subscribeChannel, final String wantedMsg){


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
                                JSONObject jsonObject = new JSONObject(message.toString());

                                Log.i("Text Content:", jsonObject.getString(wantedMsg));
                                sendNotification(jsonObject.getString(wantedMsg));
                                currentChatMsg = jsonObject.getString(wantedMsg);

                                handler.post(runnableUi);

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
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Hangout Chat Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    //Pubnub Publish function
    public void publishMessage(String publishChannel, String textmessage) {
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
            jsonObject.put("text",textmessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pubnub.publish(publishChannel, jsonObject, callback);
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            adapter.addItemNotifiChange(new Bean(currentChatMsg, R.drawable.userphoto, new Date() + "", 1));
            listView.setSelection(beans.size() - 1);
            currentChatMsg="";
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy", "!!!!!!!!!!!!");
    }
}

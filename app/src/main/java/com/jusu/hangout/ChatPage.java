package com.jusu.hangout;

import android.content.Context;
import android.content.Intent;
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

import com.jusu.hangout.adapter.CustomChatAdapter;
import com.jusu.hangout.bean.Bean;
import com.jusu.hangout.bean.FriendChatBean;
import com.jusu.hangout.bean.UserChatBean;

import java.util.Date;
import java.util.LinkedList;

public class ChatPage extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        TextView chatTitle = (TextView) findViewById(R.id.chatTitle);

        editChatText = (EditText) findViewById(R.id.EditChatText);

        functionButton = (ImageButton) findViewById(R.id.FunctionButton);
        DialogueLayout = (RelativeLayout) findViewById(R.id.DialogueLayout);
        bottomNavi = (RelativeLayout) findViewById(R.id.bottom_navi);
        functionNavi = (RelativeLayout) findViewById(R.id.function_navi);

        functionButton.setClickable(true);
        DialogueLayout.setClickable(true);

        functionNavi.setVisibility(View.INVISIBLE);

        Intent i = getIntent();
        chatTitle.setText("(" + i.getStringExtra("numberid") + ")" + i.getStringExtra("name"));

        //InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);

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
        initViewsMethod();
        onHandleMethod();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
            }
        });
    }
   /* private final EditText.OnEditorActionListener editorActionListener =
               new TextView.OnEditorActionListener() {
                       @Override
                       public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                               if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                                       //业务代码
                                       haoMent.createTest(Test.getId(), v.getText().toString());
                                       UiUtils.hideSoftKeyboard(getApplicationContext(), haoTest.this);
                                       v.setText("");
                                       v.clearFocus();
                                       handler.post(updateView);
                                   }
                               return true;
                           }
                  };*/


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
        adapter = new CustomChatAdapter(this, beans);
        listView.setAdapter(adapter);

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
                      }else {
                          adapter.addItemNotifiChange(new Bean(txt, R.drawable.userphoto, new Date() + "", 0));
                          edt.setText("");
                          listView.setSelection(beans.size() - 1);
                          System.out.println(txt);
                          System.out.println("Enter is pressed");
//                          InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                          imm.hideSoftInputFromWindow(editChatText.getWindowToken(), 0);
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        Log.i("onDestroy","!!!!!!!!!!!!");
    }
}

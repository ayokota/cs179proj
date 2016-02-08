package com.jusu.hangout;

import android.content.Context;
import android.media.session.PlaybackState;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContent extends AppCompatActivity {

    ListView chatListView;

    ImageView tapTab0;
    ImageView tapTab1;
    ImageView tapTab2;
    ImageView tapTab3;

    RelativeLayout chatLayout,contactsLayout,hangOutLayout,meLayout;

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

        } else if (tappedTag ==2) {
            tapTab2.setImageResource(R.mipmap.tabbar_discover_hl);
            tapTab1.setImageResource(R.mipmap.tabbar_contacts);
            tapTab0.setImageResource(R.mipmap.tabbar_mainframe);
            tapTab3.setImageResource(R.mipmap.tabbar_me);
            hangOutLayout.setVisibility(View.VISIBLE);
            contactsLayout.setVisibility(View.INVISIBLE);
            chatLayout.setVisibility(View.INVISIBLE);
            meLayout.setVisibility(View.INVISIBLE);
        } else if (tappedTag ==3) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincontent);

        tapTab0 = (ImageView)findViewById(R.id.img_mainframe);
        tapTab1 = (ImageView)findViewById(R.id.img_contacts);
        tapTab2 = (ImageView)findViewById(R.id.img_discover);
        tapTab3 = (ImageView)findViewById(R.id.img_me);

        chatLayout = (RelativeLayout) findViewById(R.id.chatLayout);
        contactsLayout = (RelativeLayout) findViewById(R.id.contactsLayout);
        hangOutLayout = (RelativeLayout) findViewById(R.id.hangOutLayout);
        meLayout = (RelativeLayout) findViewById(R.id.meLayout);


        chatListView = (ListView) findViewById(R.id.chatListView);

        CustomSimpleAdapter customSimpleAdapter = new CustomSimpleAdapter( MainContent.this, getHashMapData(), R.layout.custom_list_layout);

        chatListView.setAdapter(customSimpleAdapter);

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
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layoutView = layoutInflater.inflate(layoutResource, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) layoutView
                    .findViewById(R.id.imageViewLayout);
            viewHolder.number = (TextView) layoutView.findViewById(R.id.number);

            viewHolder.name = (TextView) layoutView.findViewById(R.id.name);
            viewHolder.picture.setImageResource(Integer.parseInt(data.get(
                    position).get("imageView").toString()));
            viewHolder.number.setText(data.get(position).get("id").toString());
            Log.e("id", data.get(position).get("name").toString());
            viewHolder.name.setText(data.get(position).get("name").toString());
            return layoutView;
        }
    }

    //binding the data and images use the custom adapter defined by myself

    private ArrayList<HashMap<String, Object>> getHashMapData() {
        ArrayList<HashMap<String, Object>> hashData = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> mItem = new HashMap<String, Object>();
            mItem.put("id", "Number：" + i);
            mItem.put("name", "Name" + i);
            switch (i % 5) {
                case 0:
                    mItem.put("imageView", R.drawable.text_people_1);
                    break;
                case 1:
                    mItem.put("imageView", R.drawable.text_people_2);
                    break;
                case 2:
                    mItem.put("imageView", R.drawable.text_people_3);
                    break;
                case 3:
                    mItem.put("imageView", R.drawable.text_people_4);
                    break;
                case 4:
                    mItem.put("imageView", R.drawable.text_people_5);
                    break;
                default:
                    mItem.put("imageView", R.drawable.text_people_6);
                    break;
            }
            hashData.add(mItem);
        }
        return hashData;

    }

}

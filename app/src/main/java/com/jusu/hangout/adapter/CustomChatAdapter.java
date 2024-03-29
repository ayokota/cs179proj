package com.jusu.hangout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusu.hangout.R;
import com.jusu.hangout.bean.Bean;
import com.jusu.hangout.util.DateFomats;

import java.util.Date;
import java.util.LinkedList;

public class CustomChatAdapter extends BaseAdapter {

	private LinkedList<Bean> beans = null;
	private LayoutInflater li;
	private final int ITEM_TYPES = 2, TYPE_0 = 0, TYPE_1 = 1;

	public CustomChatAdapter(Context context, LinkedList<Bean> beans) {
		// TODO Auto-generated constructor stub
		this.beans = beans;
		li = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return ITEM_TYPES;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int tp = beans.get(position).getId();
		if (TYPE_0 == tp)
			return TYPE_0;
		else if (TYPE_1 == tp)
			return TYPE_1;
		return TYPE_0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		PeopleView s = null;
		Bean bean = beans.get(position);
		int type = getItemViewType(position);

		if (null == convertView) {
			s = new PeopleView();

			switch (type) {
			case TYPE_0:
				convertView = li.inflate(R.layout.listview_item_me, null);
				break;
			case TYPE_1:
				convertView = li.inflate(R.layout.listview_item_friend, null);
				break;
			}

			s.time = (TextView) convertView.findViewById(R.id.Time);
			s.message = (TextView) convertView.findViewById(R.id.Msg);
			s.portrait = (ImageView) convertView.findViewById(R.id.Img);
			convertView.setTag(s);
		} else
			s = (PeopleView) convertView.getTag();
		s.time.setText(DateFomats.getCurrentTime(new Date().getTime()));
//		System.out.println("time:"+ new Date().getTime());
		s.message.setText(bean.gettMessage());
//		s.portrait.setImageResource(R.drawable.headpic_init);

		return convertView;

	}

	class PeopleView {
		TextView time;
		TextView message;
		ImageView portrait;
	}

	/** 添加发表私信内容，更新列表 */
	public void addItemNotifiChange(Bean bean) {
		beans.add(bean);
		notifyDataSetChanged();

	}
	/** Update the received message from others */
	public void addItem(Bean bean) {
		beans.add(bean);
	}

}

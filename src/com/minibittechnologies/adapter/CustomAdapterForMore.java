package com.minibittechnologies.adapter;

import java.util.ArrayList;

import org.woodyguthriecenter.app.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterForMore extends BaseAdapter {
	Activity activity;
	ArrayList<String> list;

	public CustomAdapterForMore(Activity activity, ArrayList<String> list) {
		super();
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder {
		ImageView img_cat;
		TextView tv_title;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.more_inflate, null);
			holder = new ViewHolder();
			holder.img_cat = (ImageView) convertView.findViewById(R.id.img_cat);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(list.get(position));
		if (position == 6) {
			holder.img_cat.setVisibility(View.GONE);
			holder.tv_title.setVisibility(View.GONE);
		} else {
			holder.img_cat.setVisibility(View.VISIBLE);
			holder.tv_title.setVisibility(View.VISIBLE);
			if (position == 0) {
				holder.img_cat.setImageResource(R.drawable.icon_reward);
			} else if (position == 1) {
				holder.img_cat.setImageResource(R.drawable.icon_phone);
			} else if (position == 2) {
				holder.img_cat.setImageResource(R.drawable.icon_browser);
			} else if (position == 3) {
				holder.img_cat.setImageResource(R.drawable.icon_email);
			} else if (position == 4) {
				holder.img_cat.setImageResource(R.drawable.icon_share);
			} else if (position == 5) {
				holder.img_cat.setImageResource(R.drawable.icon_logout);
			}
		}

		return convertView;
	}

}

package com.minibittechnologies.adapter;

import java.util.List;

import android.app.Activity;

import android.content.Context;

import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.minibittechnologies.R;

import com.minibittechnologies.model.Reward;

public class RewardListadapter extends ArrayAdapter<Reward> {
	public Context context;
	Bitmap defaultUserPic;

	public RewardListadapter(Context context, int textViewResourceId, List<Reward> items) {
		super(context, textViewResourceId, items);
		this.context = context;

	}

	private class ViewHolder {

		TextView name;
		TextView Days;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.rewards_list_row, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			holder.Days = (TextView) convertView.findViewById(R.id.tvDays);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();
		// Log.v("msg","hello");
		Reward reward = getItem(position);
		holder.name.setText(reward.getName());
		holder.Days.setText("expires in " + reward.getRemainingDays() + " days.");

		return convertView;

	}

}
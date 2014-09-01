package com.minibittechnologies.adapter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.woodyguthriecenter.app.R;
import com.minibittechnologies.utility.Constants;
import com.parse.ParseObject;

public class RewardListadapter extends ArrayAdapter<ParseObject> {
	public Context context;
	Bitmap defaultUserPic;

	public RewardListadapter(Context context, int textViewResourceId, List<ParseObject> items) {
		super(context, textViewResourceId, items);
		this.context = context;

	}

	private class ViewHolder {

		TextView name;
		TextView Days;

	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_list_reward, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tv_reward);
			holder.Days = (TextView) convertView.findViewById(R.id.tv_expire);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();
		// Log.v("msg","hello");
		ParseObject reward = getItem(position);
		holder.name.setText(reward.getString("name"));
		Date expDate=reward.getDate(Constants.EXPIRATION_DATE);
		long mili=expDate.getTime();
		
		long curMili=System.currentTimeMillis();
		if(mili<curMili)
		{
			holder.Days.setText("expired");
		}
		else
		{
			long  days=TimeUnit.DAYS.convert(mili-curMili,TimeUnit.MILLISECONDS);
			holder.Days.setText("expires in "+ days+" days.");
		}
		return convertView;

	}

}
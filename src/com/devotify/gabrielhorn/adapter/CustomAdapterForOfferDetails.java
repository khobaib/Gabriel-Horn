package com.devotify.gabrielhorn.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.model.Post;

public class CustomAdapterForOfferDetails extends BaseAdapter {
	Activity activity;
	ArrayList<Post> list;

	public CustomAdapterForOfferDetails(Activity activity, ArrayList<Post> list) {
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
		ImageView img_pic;
		TextView tv_title;
		TextView tv_message;
		View ve_top;
		RelativeLayout re_root_view;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_post, null);
			holder = new ViewHolder();
			holder.img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
			holder.ve_top = convertView.findViewById(R.id.ve_top);
			// holder.re_root_view = (RelativeLayout)
			// convertView.findViewById(R.id.re_root_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.re_root_view.setVisibility(View.GONE);
			holder.ve_top.setVisibility(View.VISIBLE);

		} else {
			holder.re_root_view.setVisibility(View.VISIBLE);
			holder.ve_top.setVisibility(View.GONE);
			holder.tv_title.setText(list.get(position).getTitle());
			// holder.tv_message.setText(list.get(position).getMessage());
			// ImageLoader.getInstance().displayImage(list.get(position).getImg_url(),holder.img_pic);

		}

		return convertView;
	}

}

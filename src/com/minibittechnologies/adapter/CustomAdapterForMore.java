package com.minibittechnologies.adapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import org.woodyguthriecenter.app.R;
import com.minibittechnologies.model.Post;



import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.Html;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapterForMore extends BaseAdapter {
	Activity activity;
	ArrayList<String> list;
	public CustomAdapterForMore(Activity activity,
			ArrayList<String> list) {
		super();
		this.activity=activity;
		this.list=list;
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
			convertView = inflater.inflate(R.layout.more_inflate,
					null);
			holder = new ViewHolder();
			holder.img_cat=(ImageView) convertView.findViewById(R.id.img_cat);
			holder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(list.get(position));
		if(position==6){
			holder.img_cat.setVisibility(View.GONE);
			holder.tv_title.setVisibility(View.GONE);
		}
		else{
			holder.img_cat.setVisibility(View.VISIBLE);
			holder.tv_title.setVisibility(View.VISIBLE);
			if (position==0) {
				holder.img_cat.setImageResource(R.drawable.rewards);
			}
			else if(position==1){
				holder.img_cat.setImageResource(R.drawable.call);
			}
			else if(position==2){
				holder.img_cat.setImageResource(R.drawable.website);
			}else if(position==3){
				holder.img_cat.setImageResource(R.drawable.mail);
			}else if(position==4){
				holder.img_cat.setImageResource(R.drawable.share);
			}else if(position==5){
				holder.img_cat.setImageResource(R.drawable.login);
			}
		}
		
	

		return convertView;
	}

}

package com.minibittechnologies.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.minibittechnologies.R;
import com.minibittechnologies.adapter.CustomAdapterForMore;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;




@SuppressLint("NewApi")
public class FragmentMore extends Fragment {

	ListView list_item;
	MoreTabFragments parent;
	ArrayList<String> item;
	ImageView backbuttonoftab;
	TextView welcome_title;
	RelativeLayout topBar;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item=new ArrayList<>();
		item.add("My Rewards Card");
		item.add("Call");
		item.add("Visit our Website");
		item.add("Email Us");
		item.add("Share our App");
		item.add("Log In");
		item.add(" sxs");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_more,
				container, false);
		topBar=(RelativeLayout) v.findViewById(R.id.topBar);
		welcome_title=(TextView) v.findViewById(R.id.welcome_title);
		backbuttonoftab=(ImageView) v.findViewById(R.id.backbuttonoftab);
		backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
		list_item=(ListView) v.findViewById(R.id.list_item);
		CustomAdapterForMore adapter=new CustomAdapterForMore(getActivity(), item);
		list_item.setAdapter(adapter);
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();

	}


	
	
}

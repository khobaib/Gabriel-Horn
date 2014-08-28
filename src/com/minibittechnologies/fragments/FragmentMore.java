package com.minibittechnologies.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minibittechnologies.R;
import com.minibittechnologies.adapter.CustomAdapterForMore;

@SuppressLint("NewApi")
public class FragmentMore extends Fragment {

	ListView list_item;
	MoreTabFragments parent;
	ArrayList<String> item;
	ImageView backbuttonoftab;
	TextView welcome_title;
	RelativeLayout topBar;
	OnDataPass dataPasser;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item = new ArrayList<>();
		item.add("My Rewards Card");
		item.add("Call");
		item.add("Visit our Website");
		item.add("Email Us");
		item.add("Share our App");
		item.add("Log In");
		item.add("sxs");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dataPasser = (OnDataPass) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_more, container, false);
		topBar = (RelativeLayout) v.findViewById(R.id.topBar);
		welcome_title = (TextView) v.findViewById(R.id.welcome_title);
		backbuttonoftab = (ImageView) v.findViewById(R.id.backbuttonoftab);
		backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
		list_item = (ListView) v.findViewById(R.id.list_item);
		CustomAdapterForMore adapter = new CustomAdapterForMore(getActivity(), item);
		list_item.setAdapter(adapter);

		list_item.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				if (pos == 5) {
					Toast.makeText(getActivity(), "Login Button pressed", Toast.LENGTH_SHORT).show();
					dataPasser.onDataPass(null);
				}

			}
		});

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public interface OnDataPass {
		public void onDataPass(String data);
	}

}

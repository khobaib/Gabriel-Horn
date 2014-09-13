package com.devotify.gabrielhorn.fragments;

import java.util.ArrayList;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.adapter.CustomAdapterForMore;
import com.devotify.gabrielhorn.interfaces.FragmentClickListener;
import com.devotify.gabrielhorn.utility.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseUser;

@SuppressLint("NewApi")
public class FragmentMore extends Fragment implements OnClickListener {

	private final String TAG = this.getClass().getSimpleName();

	ListView list_item;
	MoreTabFragments parent;
	ArrayList<String> items;
	// ImageView backbuttonoftab;
	TextView welcome_title;
	RelativeLayout topBar;

	// OnDataPass dataPasser;
	@SuppressWarnings("unused")
	private CustomAdapterForMore adapter;

	private FragmentClickListener fragClicker;

	public static Fragment newInstance() {
		return new FragmentMore();
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// fragClicker = (FragmentClickListener)
		// getArguments().getSerializable(Constants.KEY_FRAG_CLICKER);
		initMenuItems();
	}

	private void initMenuItems() {
		items = new ArrayList<>();
		items.add("My Rewards Card");
		items.add("Call");
		items.add("Visit our Website");
		items.add("Email Us");
		items.add("Share our App");
		ParseUser u = ParseUser.getCurrentUser();
		if (u == null)
			items.add("Log In");
		else
			items.add("Log Out");
		items.add("sxs");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// dataPasser = (OnDataPass) activity;
		fragClicker = (FragmentClickListener) activity;
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_more,
	// container, false);
	// topBar = (RelativeLayout) v.findViewById(R.id.topBar);
	// welcome_title = (TextView) v.findViewById(R.id.welcome_title);
	// // backbuttonoftab = (ImageView) v.findViewById(R.id.backbuttonoftab);
	// // backbuttonoftab.setOnClickListener(new OnClickListener() {
	// // @Override
	// // public void onClick(View v) {
	// // parent.onBackPressed();
	// // }
	// // });
	// list_item = (ListView) v.findViewById(R.id.list_item);
	// adapter = new CustomAdapterForMore(getActivity(), items);
	// list_item.setAdapter(adapter);
	//
	// list_item.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View v, int pos, long id)
	// {
	// if (pos == 5) {
	// // Toast.makeText(getActivity(), "Login Button pressed",
	// // Toast.LENGTH_SHORT).show();
	// // ParseUser.getCurrentUser().logOut();
	// // dataPasser.onDataPass("loginFragment");
	// if (ParseUser.getCurrentUser() == null) {
	// fragClicker.onFragmentItemClick(Constants.FRAG_MORE, true, null);
	// } else {
	// ParseUser.logOut();
	// // alert("Logged out.");
	// // initMenuItems();
	// // adapter.setData(items);
	// fragClicker.onFragmentItemClick(Constants.FRAG_MORE, true, null);
	// }
	// }
	//
	// }
	// });
	//
	// return v;
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rv = inflater.inflate(R.layout.frag_more_static, container, false);
		rv.findViewById(R.id.tvMyRewardsMore).setOnClickListener(this);
		TextView tvLogOut = (TextView) rv.findViewById(R.id.tvLogOutMore);
		tvLogOut.setOnClickListener(this);
		if (ParseUser.getCurrentUser() == null)
			tvLogOut.setText("Log in");
		else
			tvLogOut.setText("Log out");
		rv.findViewById(R.id.tvEditLocationMore).setOnClickListener(this);

		rv.findViewById(R.id.tvCallUsMore).setOnClickListener(this);
		rv.findViewById(R.id.tvEmailUsMore).setOnClickListener(this);
		rv.findViewById(R.id.tvVisitWebMore).setOnClickListener(this);
		rv.findViewById(R.id.tvShareAppMore).setOnClickListener(this);

		rv.findViewById(R.id.tvAboutAppMore).setOnClickListener(this);
		rv.findViewById(R.id.tvTermsConditionMore).setOnClickListener(this);
		rv.findViewById(R.id.tvPrivacyPolicyMore).setOnClickListener(this);

		return rv;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public interface OnDataPass {
		public void onDataPass(String data);
	}

	@SuppressWarnings("unused")
	private void alert(String message) {
		try {
			AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
			bld.setMessage(message);
			bld.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			bld.create().show();
		} catch (Exception e) {
			Log.e(TAG, "Exception inside alert with message: " + message + "\n" + e.getMessage());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvMyRewardsMore:
			fragClicker.gotoRewardsTab();
			return;
		case R.id.tvLogOutMore:
			if (ParseUser.getCurrentUser() != null)
				ParseUser.getCurrentUser().logOut();
			fragClicker.onFragmentItemClick(Constants.FRAG_MORE, true, null);
			return;
		case R.id.tvEditLocationMore:
			fragClicker.editStoreLocation();
			return;
		case R.id.tvCallUsMore:
			fragClicker.onCallUsMenuClick();
			return;
		case R.id.tvEmailUsMore:
			fragClicker.onEmailUsMenuClick();
			return;
		case R.id.tvVisitWebMore:
			fragClicker.onVisitWebMenuClick();
			return;
		case R.id.tvShareAppMore:
			fragClicker.onShareAppMenuClick();
			return;
		case R.id.tvAboutAppMore:
			fragClicker.onAboutAppMenuClick();
			return;
		case R.id.tvTermsConditionMore:
			fragClicker.onTermsConditionMenuClick();
			return;
		case R.id.tvPrivacyPolicyMore:
			fragClicker.onPrivacyPolicyMenuClick();
			return;

		default:
			return;
		}
	}
}

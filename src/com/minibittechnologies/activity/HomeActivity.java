package com.minibittechnologies.activity;

import java.io.File;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.minibittechnologies.R;
import com.minibittechnologies.fragments.FragmentMore;
import com.minibittechnologies.fragments.FragmentPostList;
import com.minibittechnologies.fragments.FragmentRewards;
import com.minibittechnologies.fragments.FragmentShare;
import com.minibittechnologies.fragments.LoginFragment;
import com.minibittechnologies.fragments.TabFragment;
import com.parse.ParseUser;

@SuppressLint("CutPasteId")
public class HomeActivity extends FragmentActivity implements FragmentMore.OnDataPass {
	public static Typeface tp;
	public String photofromcamera;
	public TabHost mTabHost;
	public String drectory;
	public String drectorythumb;
	private static final String LIST_STATE = "listState";
	private Parcelable mListState = null;
	ProgressDialog pd;
	public TextView text_notification_no_fromactivity;
	public ImageView img_cat_icon;
	File photo;
	public TabFragment activeFragment;
	FrameLayout tabcontent;
	public ViewGroup mTabsPlaceHoler;
	int count = 0;
	public ListView ProductList;
	int currentTab = 0;
	AsyncTask<Void, Void, Void> mRegisterTask;
	public TextView tv_tab_title;
	TextView text_notification_no;
	boolean galary;
	String a = "normal";
	Timer myTimer;
	String kkr;
	int k = 4;
	TabWidget tabs;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		tabs = (TabWidget) findViewById(android.R.id.tabs);

		// mTabHost.getChildAt(2).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// tabs.getChildTabViewAt(2).setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				/** If current tab is android */
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				if (tabId.equalsIgnoreCase("MoreTabFragments")) {
					ft.setCustomAnimations(R.anim.slide_out_rightleft, R.anim.slide_in_right_toleft);
					ft.add(R.id.realtabcontent, new FragmentMore(), "MoreTabFragments");
				} else if (tabId.equalsIgnoreCase("OffersTabFragment")) {
					ft.setCustomAnimations(R.anim.slide_out_leftright, R.anim.slide_in_left_to_right);
					ft.add(R.id.realtabcontent, new FragmentPostList(), "OffersTabFragment");

				} else if (tabId.equalsIgnoreCase("RewardsTabFragments")) {
					ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_bottom);
					// ft.setCustomAnimations(R.anim.slide_in_bottom,
					// R.anim.slide_out_bottom);

					ParseUser user = ParseUser.getCurrentUser();
					if (user != null)
						ft.add(R.id.realtabcontent, new FragmentRewards());
					else
						ft.add(R.id.realtabcontent, new LoginFragment(), "LoginTabFragments");
				} else if (tabId.equalsIgnoreCase("ShareTabFragments")) {
					ft.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
					ft.replace(R.id.realtabcontent, new FragmentShare(), "ShareTabFragments");

				}
				ft.commit();
			}
		};

		mTabHost.setOnTabChangedListener(tabChangeListener);

		/** Defining tab builder for Andriod tab */
		Log.e("tag", "1");
		// mTabsPlaceHoler = (TabWidget) findViewById(android.R.id.tabs);
		// View tabIndicator = LayoutInflater.from(this).inflate(
		// R.layout.tab_indicator, mTabsPlaceHoler, false);
		// ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);

		TabHost.TabSpec tSpecAndroid = mTabHost.newTabSpec("OffersTabFragment");
		Log.e("tag", "2");
		// icon.setImageResource(R.drawable.rewards_tab);
		// tSpecAndroid.setIndicator(tabIndicator);
		// tSpecAndroid.setIndicator("",
		// getResources().getDrawable(R.drawable.offer_tab));
		tSpecAndroid.setIndicator(prepareTabView(tabs.getContext(), R.layout.tab_view_offer));
		Log.e("tag", "3");
		// tSpecAndroid.setContent(new DummyTabContent(HomeActivity.this));
		tSpecAndroid.setContent(new DummyTabContent(getBaseContext()));
		// Log.e("tag"," msg "+tabIndicator+" "+
		// tSpecAndroid.setIndicator(tabIndicator));
		mTabHost.addTab(tSpecAndroid);
		Log.e("tag", "4");

		tSpecAndroid = mTabHost.newTabSpec("RewardsTabFragments");
		// icon.setImageResource(R.drawable.offer_tab);
		// tSpecAndroid.setIndicator(tabIndicator);
		// tSpecAndroid.setIndicator("",
		// getResources().getDrawable(R.drawable.rewards_tab));
		tSpecAndroid.setIndicator(prepareTabView(tabs.getContext(), R.layout.tab_view_rewards));
		tSpecAndroid.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(tSpecAndroid);

		tSpecAndroid = mTabHost.newTabSpec("ShareTabFragments");
		// icon.setImageResource(R.drawable.share_tab);
		// tSpecAndroid.setIndicator(tabIndicator);
		// tSpecAndroid.setIndicator("",
		// getResources().getDrawable(R.drawable.share_tab));
		tSpecAndroid.setIndicator(prepareTabView(tabs.getContext(), R.layout.tab_view_share));
		tSpecAndroid.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(tSpecAndroid);

		tSpecAndroid = mTabHost.newTabSpec("MoreTabFragments");
		// icon.setImageResource(R.drawable.more_tab);
		// tSpecAndroid.setIndicator(tabIndicator);
		// tSpecAndroid.setIndicator("",
		// getResources().getDrawable(R.drawable.more_tab));
		tSpecAndroid.setIndicator(prepareTabView(tabs.getContext(), R.layout.tab_view_more));
		tSpecAndroid.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(tSpecAndroid);
		// mTabHost.getTabWidget().setBackgroundColor(Color.parseColor("#F5F5F5"));
		tabs.getChildAt(2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent
						.putExtra(Intent.EXTRA_TEXT,
								"Hey I am using SeatUnity. This is a nice app to store boarding pass and to communicate with seatmates.");
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "Share Via"));

			}
		});

		tabs.getChildAt(0).setBackgroundResource(R.drawable.individual_tab);
		tabs.getChildAt(1).setBackgroundResource(R.drawable.individual_tab);
		tabs.getChildAt(2).setBackgroundResource(R.drawable.individual_tab);
		tabs.getChildAt(3).setBackgroundResource(R.drawable.individual_tab);

	}

	//
	// private void setTabs() {
	// mTabHost=(MyAnimTabHost) findViewById(android.R.id.tabhost);
	// tabcontent=(FrameLayout) findViewById(android.R.id.tabcontent);
	// tabs =tabs =(TabWidget) findViewById(android.R.id.tabs);
	// mTabHost.setup(this, getSupportFragmentManager(),
	// android.R.id.tabcontent);
	// mTabsPlaceHoler = (TabWidget) findViewById(android.R.id.tabs);
	// addTab("Offers", R.drawable.offer_tab, FragmentOfferList.class);
	// addTab("Rewards", R.drawable.rewards_tab,FragmentAccount.class);
	// addTab("Share", R.drawable.share_tab, ShareTabFragments.class);
	// addTab("More", R.drawable.more_tab, FragmentMore.class);
	//
	// tabs.getChildAt(2).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// Intent sendIntent = new Intent();
	// sendIntent.setAction(Intent.ACTION_SEND);
	// sendIntent.putExtra(Intent.EXTRA_TEXT,
	// "Hey I am using SeatUnity. This is a nice app to store boarding pass and to communicate with seatmates.");
	// sendIntent.setType("text/plain");
	// startActivity(Intent.createChooser(sendIntent, "Share Via"));
	//
	// }
	// });
	//
	// tabs.getChildAt(0).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	//
	// }
	// });
	// tabs.getChildAt(1).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	//
	// tabs.getChildAt(2).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	//
	// }
	// });
	//
	//

	// }

	//
	// private void addTab(String labelId, int drawableId, Class<?> c) {
	// FragmentTabHost.TabSpec spec = mTabHost.newTabSpec(labelId);
	//
	// View tabIndicator = LayoutInflater.from(this).inflate(
	// R.layout.tab_indicator, mTabsPlaceHoler, false);
	// ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
	// icon.setImageResource(drawableId);
	// spec.setIndicator(tabIndicator);
	// spec.setIndicator(tabIndicator);
	// mTabHost.addTab(spec, c, null);
	// }
	//
	private static View prepareTabView(Context context, int layout) {
		View view = LayoutInflater.from(context).inflate(layout, null);

		return view;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// myTimer.cancel();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void setTabSelection() {
		// TODO Auto-generated method stub
		mTabHost.setCurrentTab(1);
	}

	public void setTabSelection0() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		if (activeFragment != null)
			activeFragment.onBackPressed();
	}

	public void close() {
		finish();
	}

	@Override
	public void onDataPass(String data) {
		Log.e(">>>>>>>>", "in onDataPass, HomeActivity");
		// Fragment newFragment = new LoginFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with
		// this fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.realtabcontent, new LoginFragment());
		// transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();

	}

}

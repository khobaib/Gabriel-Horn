/**
 * 
 */
package com.devotify.gabrielhorn.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.fragments.AddPostFragment;
import com.devotify.gabrielhorn.fragments.FragmentEditLocation;
import com.devotify.gabrielhorn.fragments.FragmentMore;
import com.devotify.gabrielhorn.fragments.FragmentPostList;
import com.devotify.gabrielhorn.fragments.FragmentRewards;
import com.devotify.gabrielhorn.fragments.FragmentSingleOffer;
import com.devotify.gabrielhorn.fragments.LoginFragment;
import com.devotify.gabrielhorn.fragments.TermsAndConditionsFragment;
import com.devotify.gabrielhorn.fragments.VisitSiteFragment;
import com.devotify.gabrielhorn.interfaces.FragmentClickListener;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.utility.Constants;
import com.devotify.gabrielhorn.utility.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class HolderActivity extends FragmentActivity implements OnClickListener, FragmentClickListener, Serializable {

	private static final long serialVersionUID = -7335912528173342127L;

	private final String TAG = this.getClass().getSimpleName();

	private Button btnOffer, btnReward, btnAddPost, btnMore;
	// private FrameLayout flFragHolder;
	private LinearLayout llBottomTabHoolder;

	private FragmentManager fragMang;
	private FragmentTransaction fragTranx;

	private static Stack<Fragment> fragBackStack;

	// public static boolean isLoggedIn = false;

	/**
	 * Length = 5 <br>
	 * 0: FragmentPostList - has arg <br>
	 * 1: FragmentRewards <br>
	 * 2: AddPostFragment <br>
	 * 3: FragmentMore - has arg<br>
	 * 4: LoginFragment - has arg<br>
	 */
	// * 5: FragmentSingleOffer <br>
	private static ArrayList<Fragment> fList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_holder);
		ParseAnalytics.trackAppOpened(getIntent());
		getAppCompanyInfo();
		fragBackStack = new Stack<>();
		initTabButtons();
		setupFragmentList();

		fragMang = getSupportFragmentManager();
		fragTranx = fragMang.beginTransaction();
		fragTranx.add(R.id.flFragmentHolder, fList.get(0));
		fragTranx.commit();
		fragBackStack.push(fList.get(0));
		setOfferTabPressed();

		// flFragHolder = (FrameLayout) findViewById(R.id.flFragmentHolder);

		btnOffer.setOnClickListener(this);
		btnReward.setOnClickListener(this);
		btnAddPost.setOnClickListener(this);
		btnMore.setOnClickListener(this);
	}

	private void setupFragmentList() {
		fList = new ArrayList<>();
		fList.add(FragmentPostList.newInstance()); // 0
		fList.add(FragmentRewards.newInstance()); // 1
		fList.add(AddPostFragment.newInstance()); // 2
		fList.add(FragmentMore.newInstance()); // 3
		fList.add(LoginFragment.newInstance()); // 4
		// fList.add(FragmentSingleOffer.newInstance(this)); // 5

		// Fragment f = FragmentPostList.newInstance();
		// // f.setArguments(b);
		// fList.add(f); // 0
		// fList.add(FragmentRewards.newInstance()); // 1
		// fList.add(AddPostFragment.newInstance()); // 2
		// f = FragmentMore.newInstance();
		// // f.setArguments(b);
		// fList.add(f); // 3
		// f = LoginFragment.newInstance();
		// // f.setArguments(b);
		// fList.add(f); // 4
	}

	@Override
	public void onClick(View v) {
		fragTranx = fragMang.beginTransaction();
		boolean isLoggedIn = (ParseUser.getCurrentUser() != null);
		switch (v.getId()) {
		case R.id.btnOffer:
			Log.d(TAG, "Offer Clicked");
			fragTranx.setCustomAnimations(R.anim.slide_out_leftright, R.anim.slide_in_left_to_right);
			fragTranx.replace(R.id.flFragmentHolder, fList.get(0));
			fragTranx.commit();
			if (fragBackStack.contains(fList.get(0)))
				clearStackUptoPos(0);
			fragBackStack.push(fList.get(0));
			setOfferTabPressed();
			break;
		case R.id.btnReward:
			Log.d(TAG, "Reward Clicked");
			if (isLoggedIn) {
				fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
				fragTranx.replace(R.id.flFragmentHolder, fList.get(1));
				if (fragBackStack.contains(fList.get(1)))
					clearStackUptoPos(1);
				fragBackStack.push(fList.get(1));
			} else {
				fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
				fragTranx.replace(R.id.flFragmentHolder, fList.get(4));
				if (fragBackStack.contains(fList.get(4)))
					clearStackUptoPos(4);
				fragBackStack.push(fList.get(4));
			}
			fragTranx.commit();
			setRewardTabPressed();
			break;
		case R.id.btnAddPost:
			Log.d(TAG, "Add Post Clicked");
			if (isLoggedIn) {
				fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
				fragTranx.replace(R.id.flFragmentHolder, fList.get(2));
				fragTranx.commit();
				if (fragBackStack.contains(fList.get(2)))
					clearStackUptoPos(2);
				fragBackStack.push(fList.get(2));
				setAddPostTabPressed();
			} else {
				shareTheApp();
			}
			Log.i(TAG, "broken!!");
			break;
		case R.id.btnMore:
			Log.d(TAG, "More Clicked");
			fragTranx.setCustomAnimations(R.anim.slide_out_rightleft, R.anim.slide_in_right_toleft);
			fragTranx.replace(R.id.flFragmentHolder, fList.get(3));
			fragTranx.commit();
			if (fragBackStack.contains(fList.get(3)))
				clearStackUptoPos(3);
			fragBackStack.push(fList.get(3));
			setMoreTabPressed();
			break;

		default:
			break;
		}
	}

	private void clearStackUptoPos(int pos) {
		Fragment f = fList.get(pos);
		while (!(fragBackStack.isEmpty()) && (fragBackStack.pop() != f))
			Log.d(TAG, "Clearing frag upto pos=" + pos + ": fragBackStack.size() = " + fragBackStack.size());
	}

	private void clearStackUptoFragment(Fragment f) {
		while (!(fragBackStack.isEmpty()) && (fragBackStack.pop() != f))
			Log.d(TAG, "Clearing frag upto f: fragBackStack.size() = " + fragBackStack.size());
	}

	public void setTabPressedState(int index) {
		if (index == Constants.FRAG_OFFER)
			setOfferTabPressed();
		else if (index == Constants.FRAG_REWARD)
			setRewardTabPressed();
		else if (index == Constants.FRAG_ADD_POST)
			setAddPostTabPressed();
		else if (index == Constants.FRAG_MORE)
			setMoreTabPressed();
	}

	private void setOfferTabPressed() {
		// initTabButtons();
		btnOffer.setTextColor(getResources().getColor(R.color.app_theme));
		Drawable d = getResources().getDrawable(R.drawable.tab_1_p);
		if (d != null)
			btnOffer.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);

		d = getResources().getDrawable(R.drawable.tab_2);
		if (d != null)
			btnReward.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(getAddPostResIdAndSetText(false));
		if (d != null)
			btnAddPost.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(R.drawable.tab_4);
		if (d != null)
			btnMore.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		btnReward.setTextColor(Color.GRAY);
		btnAddPost.setTextColor(Color.GRAY);
		btnMore.setTextColor(Color.GRAY);
		llBottomTabHoolder.setBackgroundColor(Color.argb(255, 245, 245, 245));
	}

	private void setRewardTabPressed() {
		// initTabButtons();
		btnReward.setTextColor(getResources().getColor(R.color.app_theme));
		Drawable d = getResources().getDrawable(R.drawable.tab_2_p);
		if (d != null)
			btnReward.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);

		d = getResources().getDrawable(R.drawable.tab_1);
		if (d != null)
			btnOffer.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(getAddPostResIdAndSetText(false));
		if (d != null)
			btnAddPost.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(R.drawable.tab_4);
		if (d != null)
			btnMore.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		btnOffer.setTextColor(Color.WHITE);
		btnAddPost.setTextColor(Color.WHITE);
		btnMore.setTextColor(Color.WHITE);
		llBottomTabHoolder.setBackgroundColor(Color.argb(200, 20, 20, 20));
	}

	private void setAddPostTabPressed() {
		// initTabButtons();
		btnAddPost.setTextColor(getResources().getColor(R.color.app_theme));
		Drawable d = getResources().getDrawable(getAddPostResIdAndSetText(true));
		if (d != null)
			btnAddPost.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);

		d = getResources().getDrawable(R.drawable.tab_1);
		if (d != null)
			btnOffer.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(R.drawable.tab_2);
		if (d != null)
			btnReward.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(R.drawable.tab_4);
		if (d != null)
			btnMore.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		btnOffer.setTextColor(Color.WHITE);
		btnReward.setTextColor(Color.WHITE);
		btnMore.setTextColor(Color.WHITE);
		llBottomTabHoolder.setBackgroundColor(Color.argb(200, 20, 20, 20));
	}

	private void setMoreTabPressed() {
		// initTabButtons();
		btnMore.setTextColor(getResources().getColor(R.color.app_theme));
		Drawable d = getResources().getDrawable(R.drawable.tab_4_p);
		if (d != null)
			btnMore.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);

		d = getResources().getDrawable(R.drawable.tab_1);
		if (d != null)
			btnOffer.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(R.drawable.tab_2);
		if (d != null)
			btnReward.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		d = getResources().getDrawable(getAddPostResIdAndSetText(false));
		if (d != null)
			btnAddPost.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
		btnOffer.setTextColor(Color.GRAY);
		btnReward.setTextColor(Color.GRAY);
		btnAddPost.setTextColor(Color.GRAY);
		llBottomTabHoolder.setBackgroundColor(Color.argb(255, 245, 245, 245));
	}

	private void initTabButtons() {
		// if (llBottomTabHoolder == null)
		llBottomTabHoolder = (LinearLayout) findViewById(R.id.llBottomTabHolder);
		// if (btnOffer == null)
		btnOffer = (Button) findViewById(R.id.btnOffer);
		// if (btnReward == null)
		btnReward = (Button) findViewById(R.id.btnReward);
		// if (btnAddPost == null)
		btnAddPost = (Button) findViewById(R.id.btnAddPost);
		// if (btnMore == null)
		btnMore = (Button) findViewById(R.id.btnMore);
	}

	private int getAddPostResIdAndSetText(boolean isPressed) {
		boolean isLoggedIn = !(ParseUser.getCurrentUser() == null);
		if (isLoggedIn) {
			btnAddPost.setText("Add Post");
			if (isPressed)
				return R.drawable.tab_post_1_p;
			return R.drawable.tab_post_1;
		} else {
			btnAddPost.setText("Share App");
			if (isPressed)
				return R.drawable.tab_3_p;
			return R.drawable.tab_3;
		}
	}

	private void shareTheApp() {
		Log.e(TAG, "shareTheApp");
		Intent sendIntent = new Intent(HolderActivity.this, HomeActivity.class);
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey I am using Gabriel Horn!");
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share Via"));
	}

	@Override
	public void onBackPressed() {
		if (fragBackStack.size() <= 1) {
			Log.e(TAG, "Going out from the app with no fragment remaining in the back-stack.");
			super.onBackPressed();
		} else {
			fragBackStack.pop();
			Log.i(TAG, "Popping out last fragment from the back stack with size: " + fragBackStack.size());
			fragTranx = fragMang.beginTransaction();
			fragTranx.replace(R.id.flFragmentHolder, fragBackStack.peek());
			fragTranx.commit();
			resetTabSelectionForFragment(fragBackStack.peek());
		}
	}

	private void resetTabSelectionForFragment(Fragment f) {
		if (f == fList.get(0))
			setOfferTabPressed();
		else if (f == fList.get(1) || f == fList.get(4))
			setRewardTabPressed();
		else if (f == fList.get(2))
			setAddPostTabPressed();
		else if (f == fList.get(3))
			setMoreTabPressed();
		else
			Log.e(TAG, "No suitable tab found!! :: f = " + f);
	}

	@Override
	public void onFragmentItemClick(int fragType, boolean doLogIn, Post post) {
		// boolean isLoggedIn = !(ParseUser.getCurrentUser()==null);
		if (fragType == Constants.FRAG_REWARD) {
			fragTranx = fragMang.beginTransaction();
			fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
			fragTranx.replace(R.id.flFragmentHolder, fList.get(1));
			fragTranx.commit();
			if (fragBackStack.contains(fList.get(1)))
				clearStackUptoPos(1);
			fragBackStack.push(fList.get(1));
			setRewardTabPressed();
		} else if (fragType == Constants.FRAG_OFFER) {
			// TO_DO Show post details
			Log.d(TAG, "FRAG_MORE transitioning to FragmentSingleOffer");
			fragTranx = fragMang.beginTransaction();
			Fragment f = FragmentSingleOffer.newInstance(post);
			fragTranx.setCustomAnimations(R.anim.slide_out_rightleft, R.anim.slide_in_right_toleft);
			fragTranx.replace(R.id.flFragmentHolder, f);
			fragTranx.commit();
			if (fragBackStack.contains(f))
				clearStackUptoFragment(f);
			fragBackStack.push(f);
		} else if (fragType == Constants.FRAG_MORE) {
			// TO_DO Show log-in fragment
			if (doLogIn) {
				Log.d(TAG, "FRAG_MORE transitioning to LogInFragment");
				fragTranx = fragMang.beginTransaction();
				fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
				fragTranx.replace(R.id.flFragmentHolder, fList.get(4));
				fragTranx.commit();
				if (fragBackStack.contains(fList.get(4)))
					clearStackUptoPos(4);
				fragBackStack.push(fList.get(4));
				setRewardTabPressed();
			}
		} else if (fragType == Constants.FRAG_LOGGED_IN) {
			fragTranx = fragMang.beginTransaction();
			fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
			fragTranx.replace(R.id.flFragmentHolder, fList.get(1));
			fragTranx.commit();
			if (fragBackStack.contains(fList.get(1)))
				clearStackUptoPos(1);
			fragBackStack.push(fList.get(1));
			setRewardTabPressed();
		}
	}

	private void getAppCompanyInfo() {
		String appCompany = Utils.readString(HolderActivity.this, Utils.KEY_PARENT_APP_ID, "");
		if (appCompany.equals("")) {
			String appPackage = getApplicationContext().getPackageName();
			ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("AppParentCompany");
			parseQuery.whereEqualTo("appIdentifier", appPackage);
			parseQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> list, ParseException e) {
					if (e == null) {
						ParseObject company = list.get(0);
						String appId = company.getObjectId();
						Utils.writeString(HolderActivity.this, Utils.KEY_PARENT_APP_ID, appId);
						String companyPhn = company.getString("phoneNumber");
						Utils.writeString(HolderActivity.this, Utils.APP_COMPANY_PHONE, companyPhn);
						String companyEmail = company.getString("email");
						Utils.writeString(HolderActivity.this, Utils.APP_COMPANY_EMAIL, companyEmail);
						String companySite = company.getString("websiteUrl");
						Utils.writeString(HolderActivity.this, Utils.VISIT_OUR_SITE, companySite);
						company.pinInBackground(new SaveCallback() {

							@Override
							public void done(ParseException arg0) {
								Log.e("MSG", "appCompanySaved");
							}
						});
						Log.e("MSGD", appId + companyPhn + companyEmail);
					}

				}
			});
		}
	}

	@Override
	public void gotoRewardsTab() {
		fragTranx = fragMang.beginTransaction();
		fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
		Fragment f;
		if (ParseUser.getCurrentUser() == null)
			f = fList.get(4);
		else
			f = fList.get(1);
		fragTranx.replace(R.id.flFragmentHolder, f);
		fragTranx.commit();
		if (fragBackStack.contains(f))
			clearStackUptoFragment(f);
		fragBackStack.push(f);
		setRewardTabPressed();
	}

	@Override
	public void logInOrOut() {
	}

	@Override
	public void editStoreLocation() {
		// TODO Load g-map & get selected location
		Log.i(TAG, "editStoreLocation");
		fragTranx = fragMang.beginTransaction();
		fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
		Fragment f = FragmentEditLocation.newInstance();
		fragTranx.replace(R.id.flFragmentHolder, f);
		fragTranx.commit();
		if (fragBackStack.contains(f))
			clearStackUptoFragment(f);
		fragBackStack.push(f);
		llBottomTabHoolder.setBackgroundColor(Color.argb(255, 255, 255, 255));
		// setMoreTabPressed();

	}

	@Override
	public void onCallUsMenuClick() {
		String phn = Utils.readString(HolderActivity.this, Utils.APP_COMPANY_PHONE, "");
		if (!phn.equals("")) {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + phn));
			startActivity(callIntent);
		}
	}

	@Override
	public void onEmailUsMenuClick() {
		String email = Utils.readString(HolderActivity.this, Utils.APP_COMPANY_EMAIL, "");
		if (!email.equals("")) {
			Intent intentMail = new Intent(Intent.ACTION_SEND);
			intentMail.putExtra(Intent.EXTRA_EMAIL, new String[] { email });

			intentMail.setType("message/rfc822");
			startActivity(Intent.createChooser(intentMail, "Choose an Email client :"));
		}
	}

	@Override
	public void onVisitWebMenuClick() {
		fragTranx = fragMang.beginTransaction();
		fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
		VisitSiteFragment visitSiteFragment = new VisitSiteFragment();
		fragTranx.replace(R.id.flFragmentHolder, visitSiteFragment);
		fragTranx.commit();
		if (fragBackStack.contains(visitSiteFragment))
			clearStackUptoFragment(visitSiteFragment);
		fragBackStack.push(visitSiteFragment);
		llBottomTabHoolder.setBackgroundColor(Color.argb(255, 255, 255, 255));
	}

	@Override
	public void onShareAppMenuClick() {
		shareTheApp();
	}

	@Override
	public void onAboutAppMenuClick() {
		// TODO load about-app fragment
	}

	@Override
	public void onTermsConditionMenuClick() {
		fragTranx = fragMang.beginTransaction();
		fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
		TermsAndConditionsFragment tcFragment = new TermsAndConditionsFragment();
		fragTranx.replace(R.id.flFragmentHolder, tcFragment);
		fragTranx.commit();
		if (fragBackStack.contains(tcFragment))
			clearStackUptoFragment(tcFragment);
		fragBackStack.push(tcFragment);
		llBottomTabHoolder.setBackgroundColor(Color.argb(255, 255, 255, 255));
	}

	@Override
	public void onPrivacyPolicyMenuClick() {
		// TODO load privacy-policy fragment
	}

}

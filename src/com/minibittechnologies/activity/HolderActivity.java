/**
 * 
 */
package com.minibittechnologies.activity;

import java.util.ArrayList;
import java.util.Stack;

import org.woodyguthriecenter.app.R;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.minibittechnologies.fragments.AddPostFragment;
import com.minibittechnologies.fragments.FragmentMore;
import com.minibittechnologies.fragments.FragmentPostList;
import com.minibittechnologies.fragments.FragmentRewards;
import com.minibittechnologies.fragments.FragmentSingleOffer;
import com.minibittechnologies.fragments.LoginFragment;
import com.minibittechnologies.interfaces.FragmentClickListener;
import com.minibittechnologies.model.Post;
import com.minibittechnologies.utility.Constants;
import com.parse.ParseUser;

/**
 * @author Touhid
 * 
 */
public class HolderActivity extends FragmentActivity implements OnClickListener, FragmentClickListener {

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
	 * 0: FragmentPostList <br>
	 * 1: FragmentRewards <br>
	 * 2: AddPostFragment <br>
	 * 3: FragmentMore <br>
	 * 4: LoginFragment <br>
	 */
	// * 5: FragmentSingleOffer <br>
	private static ArrayList<Fragment> fList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_holder);
		
		fragBackStack = new Stack<>();
		initTabButtons();
		fList = new ArrayList<>();
		fList.add(FragmentPostList.newInstance(this)); // 0
		fList.add(FragmentRewards.newInstance()); // 1
		fList.add(AddPostFragment.newInstance()); // 2
		fList.add(FragmentMore.newInstance(this)); // 3
		fList.add(LoginFragment.newInstance(this)); // 4
		// fList.add(FragmentSingleOffer.newInstance(this)); // 5

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

	@Override
	public void onClick(View v) {
		fragTranx = fragMang.beginTransaction();
		boolean isLoggedIn = (ParseUser.getCurrentUser() != null);
		switch (v.getId()) {
		case R.id.btnOffer:
			Log.d(TAG, "Offer Clicked");
			fragTranx.replace(R.id.flFragmentHolder, fList.get(0));
			fragTranx.commit();
			fragBackStack.push(fList.get(0));
			setOfferTabPressed();
			break;
		case R.id.btnReward:
			Log.d(TAG, "Reward Clicked");
			if (isLoggedIn) {
				fragTranx.replace(R.id.flFragmentHolder, fList.get(1));
				fragBackStack.push(fList.get(1));
			} else {
				fragTranx.replace(R.id.flFragmentHolder, fList.get(4));
				fragBackStack.push(fList.get(4));
			}
			fragTranx.commit();
			setRewardTabPressed();
			break;
		case R.id.btnAddPost:
			Log.d(TAG, "Add Post Clicked");
			if (isLoggedIn) {
				fragTranx.replace(R.id.flFragmentHolder, fList.get(2));
				fragTranx.commit();
				fragBackStack.push(fList.get(2));
				setAddPostTabPressed();
			} else {
				shareTheApp();
			}
			break;
		case R.id.btnMore:
			Log.d(TAG, "More Clicked");
			fragTranx.replace(R.id.flFragmentHolder, fList.get(3));
			fragTranx.commit();
			fragBackStack.push(fList.get(3));
			setMoreTabPressed();
			break;

		default:
			break;
		}
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
		btnOffer.setTextColor(Color.BLUE);
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
		btnReward.setTextColor(Color.BLACK);
		btnAddPost.setTextColor(Color.BLACK);
		btnMore.setTextColor(Color.BLACK);
		llBottomTabHoolder.setBackgroundColor(Color.argb(220, 204, 204, 204));
	}

	private void setRewardTabPressed() {
		// initTabButtons();
		btnReward.setTextColor(Color.BLUE);
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
		llBottomTabHoolder.setBackgroundColor(Color.argb(220, 20, 20, 20));
	}

	private void setAddPostTabPressed() {
		// initTabButtons();
		btnAddPost.setTextColor(Color.BLUE);
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
		llBottomTabHoolder.setBackgroundColor(Color.argb(220, 29, 31, 31));
	}

	private void setMoreTabPressed() {
		// initTabButtons();
		btnMore.setTextColor(Color.BLUE);
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
		btnOffer.setTextColor(Color.BLACK);
		btnReward.setTextColor(Color.BLACK);
		btnAddPost.setTextColor(Color.BLACK);
		llBottomTabHoolder.setBackgroundColor(Color.argb(220, 250, 250, 250));
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

	@Override
	public void onFragmentItemClick(int fragType, boolean doLogIn, Post post) {
		// boolean isLoggedIn = !(ParseUser.getCurrentUser()==null);
		if (fragType == Constants.FRAG_REWARD) {
			if (doLogIn) {
				Log.d(TAG, "Logging in & log in fragment calling as the reward tab...");
			} else {
				// isLoggedIn = false;
				// Toast.makeText(this, "Now assume that you're logged out!",
				// Toast.LENGTH_LONG).show();
				Log.d(TAG, "Logged out & Reward tab...");
			}
			fragTranx = fragMang.beginTransaction();
			fragTranx.replace(R.id.flFragmentHolder, fList.get(1));
			fragTranx.commit();
			fragBackStack.push(fList.get(1));
			setRewardTabPressed();
		} else if (fragType == Constants.FRAG_OFFER) {
			// TODO Show post details
			Log.d(TAG, "FRAG_MORE transitioning to FragmentSingleOffer");
			fragTranx = fragMang.beginTransaction();
			Fragment f = FragmentSingleOffer.newInstance(post);
			fragTranx.replace(R.id.flFragmentHolder, f);
			fragTranx.commit();
			fragBackStack.push(f);
		} else if (fragType == Constants.FRAG_MORE) {
			// TODO Show log-in fragment
			if (doLogIn) {
				Log.d(TAG, "FRAG_MORE transitioning to LogInFragment");
				fragTranx = fragMang.beginTransaction();
				fragTranx.replace(R.id.flFragmentHolder, fList.get(4));
				fragTranx.commit();
				fragBackStack.push(fList.get(4));
				setRewardTabPressed();
			} else
				Toast.makeText(HolderActivity.this, "Log out is yet to be implemented!", Toast.LENGTH_LONG).show();
		}else if(fragType == Constants.FRAG_LOGGED_IN){
			fragTranx = fragMang.beginTransaction();
			fragTranx.replace(R.id.flFragmentHolder, fList.get(1));
			fragTranx.commit();
			fragBackStack.push(fList.get(1));
			setRewardTabPressed();
		}
	}

	private void shareTheApp() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey I am using Gabriel Horn!");
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share Via"));
	}

	@Override
	public void onBackPressed() {
		if (fragBackStack.size() == 0) {
			Log.e(TAG, "Going out from the app with no fragment remaining in the back-stack.");
			super.onBackPressed();
		} else {
			fragBackStack.pop();
			Log.i(TAG, "Popping out last fragment from the back stack with size: " + fragBackStack.size());
			fragTranx = fragMang.beginTransaction();
			fragTranx.replace(R.id.flFragmentHolder, fragBackStack.peek());
			fragTranx.commit();
		}
	}

}

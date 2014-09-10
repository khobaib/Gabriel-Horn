package com.minibittechnologies.fragments;

import java.util.Stack;

import org.woodyguthriecenter.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.minibittechnologies.activity.HomeActivity;
import com.minibittechnologies.utility.Constants;

public class OffersTabFragment extends TabFragment {
	protected Stack<Fragment> backEndStack;
	protected Stack<Integer> trackcallhome;
	Bundle sBundle;

	int callstatefromtab = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trackcallhome = new Stack<Integer>();
		backEndStack = new Stack<Fragment>();

		FragmentPostList initialFragment = new FragmentPostList();
		initialFragment.setParent(this);

		trackcallhome.push(0);
		backEndStack.push(initialFragment);
		sBundle = savedInstanceState;
	}

	// public void StartSingleOfferFragment(Post singleofferDetails)
	// {
	// FragmentSingleOffer newFragment = new
	// FragmentSingleOffer(singleofferDetails);
	// newFragment.parent = this;
	// FragmentManager fragmentManager = getChildFragmentManager();
	// FragmentTransaction fragmentTransaction =
	// fragmentManager.beginTransaction();
	// // fragmentTransaction.setCustomAnimations(
	// // R.anim.slide_in_right_toleft,R.anim.slide_out_leftright);
	// fragmentTransaction.replace(R.id.tab3Content, newFragment);
	// fragmentTransaction.addToBackStack(null);
	// backEndStack.push(newFragment);
	// fragmentTransaction.commitAllowingStateLoss();
	// }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewParent parent = (ViewParent) container.getParent();
		if (parent instanceof View) {

		}
		View v = inflater.inflate(R.layout.fragment_tab3, container, false);
		return v;
	}

	public void onStart() {
		if (Constants.TABFROMSHARE) {
			Constants.TABFROMSHARE = false;
		} else {
			trackcallhome.peek();
			Fragment fragment = backEndStack.peek();

			FragmentManager fragmentManager = getChildFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			// fragmentTransaction.setcu
			// fragmentTransaction.setCustomAnimations(
			// R.anim.slide_in_left_to_right,R.anim.slide_out_leftright);
			fragmentTransaction.replace(R.id.tab3Content, fragment);
			fragmentTransaction.commitAllowingStateLoss();
		}

		// fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right);

		super.onStart();
	}

	public void clearr() {
		trackcallhome.pop();
		backEndStack.pop();
	}

	@Override
	public void onBackPressed() {

		if (backEndStack.size() == 1) {
			((HomeActivity) getActivity()).close();
		} else {
			if (backEndStack.size() == 1) {
				((HomeActivity) getActivity()).close();
			} else {
				backEndStack.pop();
				Fragment frg = backEndStack.peek();
				Log.d("1", "4");
				FragmentManager fragmentManager = getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				// fragmentTransaction.setCustomAnimations(
				// R.anim.slide_in_left_to_right,R.anim.slide_out_leftright);
				fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
			}

		}

	}
}

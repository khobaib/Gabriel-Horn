package com.minibittechnologies.fragments;

import java.util.Stack;

import com.minibittechnologies.R;
import com.minibittechnologies.activity.HomeActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewParent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

public class MoreTabFragments extends TabFragment{
	protected Stack<Fragment> backEndStack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backEndStack = new Stack<Fragment>();
		FragmentMore initialFragment = new FragmentMore();
		initialFragment.parent = this;
		backEndStack.push(initialFragment);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewParent parent = (ViewParent) container.getParent();
	
		View v = inflater.inflate(R.layout.fragment_tab3, container, false);
		return v;
	}
	public void onStart( ) {
		Fragment fragment = backEndStack.peek();
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		super.onStart();
	}
	
	public void clearr(){
		backEndStack.pop();
	}
	@Override
	public void onBackPressed() {
		
		if (backEndStack.size()==1) {
			((HomeActivity) getActivity()).close();
		}
		else {
			if (backEndStack.size()==1) {
				((HomeActivity) getActivity()).close();
			} else {
				backEndStack.pop();
				Fragment frg = backEndStack.peek();
				Log.d("1", "4");
				FragmentManager fragmentManager = getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
			}

		}
	}
}

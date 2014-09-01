package com.minibittechnologies.fragments;

import java.util.Stack;

import org.woodyguthriecenter.app.R;
import com.minibittechnologies.activity.HomeActivity;
import com.minibittechnologies.utility.Constants;

import android.content.Intent;
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

public class ShareTabFragments extends TabFragment{
	protected Stack<Fragment> backEndStack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		backEndStack = new Stack<Fragment>();
//		FragmentShare initialFragment = new FragmentShare();
//		initialFragment.parent = this;
//		backEndStack.push(initialFragment);
	}
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		ViewParent parent = (ViewParent) container.getParent();
//	
//		View v = inflater.inflate(R.layout.fragment_tab3, container, false);
//		
//		return v;
//	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Constants.TABFROMSHARE=true;
		
		
	}

	
	public void clearr(){
		backEndStack.pop();
	}
	@Override
	public void onBackPressed() {
		
		
			((HomeActivity) getActivity()).close();
		
	}
}

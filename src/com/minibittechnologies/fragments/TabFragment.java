package com.minibittechnologies.fragments;

import java.util.Stack;

import com.minibittechnologies.activity.HomeActivity;



import android.support.v4.app.Fragment;

public abstract class TabFragment extends Fragment {
	@Override
	public void onResume(){
		((HomeActivity)getActivity()).activeFragment=this;
		super.onResume();
	}
	public abstract void onBackPressed();
}

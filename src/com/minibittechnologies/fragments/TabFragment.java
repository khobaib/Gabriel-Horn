package com.minibittechnologies.fragments;

import android.support.v4.app.Fragment;

import com.minibittechnologies.activity.HomeActivity;

public abstract class TabFragment extends Fragment {
	@Override
	public void onResume(){
		((HomeActivity)getActivity()).activeFragment=this;
		super.onResume();
	}
	public abstract void onBackPressed();
}

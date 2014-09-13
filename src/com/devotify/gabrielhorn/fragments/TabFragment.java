package com.devotify.gabrielhorn.fragments;

import android.support.v4.app.Fragment;

import com.devotify.gabrielhorn.activity.HomeActivity;

public abstract class TabFragment extends Fragment {
	@Override
	public void onResume(){
		((HomeActivity)getActivity()).activeFragment=this;
		super.onResume();
	}
	public abstract void onBackPressed();
}

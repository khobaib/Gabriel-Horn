package com.minibittechnologies.fragments;

import org.woodyguthriecenter.app.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




@SuppressLint("NewApi")
public class FragmentShare extends Fragment {

	
	ShareTabFragments parent;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_account,
				container, false);
	
		
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();

	}


	
	
}

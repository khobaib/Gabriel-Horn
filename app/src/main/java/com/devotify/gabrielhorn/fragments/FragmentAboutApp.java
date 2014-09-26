/**
 * 
 */
package com.devotify.gabrielhorn.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devotify.gabrielhorn.R;

/**
 * @author Touhid
 * 
 */
public class FragmentAboutApp extends Fragment {

	private final String TAG = this.getClass().getSimpleName();

	public static Fragment newInstance() {
		return new FragmentAboutApp();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "inside onCreateView");
		View v = inflater.inflate(R.layout.frag_about_app, container, false);
		return v;
	}

}

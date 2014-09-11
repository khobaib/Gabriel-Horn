package com.minibittechnologies.fragments;

import org.woodyguthriecenter.app.R;

import com.minibittechnologies.utility.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class VisitSiteFragment extends Fragment{
	
	private WebView vsWebView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.layout_tc_fragment,container,false);
		vsWebView=(WebView)v.findViewById(R.id.wv_tc);
		String url=Utils.readString(getActivity(),Utils.VISIT_OUR_SITE,"");
		if(!url.equals(""))
			vsWebView.loadUrl(url);
		return v;
	}


}

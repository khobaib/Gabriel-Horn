package com.devotify.gabrielhorn.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.devotify.gabrielhorn.R;

public class TermsAndConditionsFragment extends Fragment
{
    private WebView tcWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.layout_tc_fragment, container, false);
        tcWebView = (WebView) v.findViewById(R.id.wv_tc);
        tcWebView.loadUrl("file:///android_asset/ghorn_tc.html");
        return v;
    }

}

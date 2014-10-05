/**
 *
 */
package com.devotify.gabrielhorn.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;

/**
 * @author Touhid
 */
public class PrivacyPolicyFragment extends Fragment
{
    public static PrivacyPolicyFragment newInstance()
    {
        return new PrivacyPolicyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.frag_privacy_terms, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tvPrivacyOrTerms);

        String appName = getString(R.string.app_name);
        String privacyPolicy = getString(R.string.privacy_policy_text);
        tv.setText(Html.fromHtml(privacyPolicy.replaceAll("@@@", appName)));
        return v;
    }

}

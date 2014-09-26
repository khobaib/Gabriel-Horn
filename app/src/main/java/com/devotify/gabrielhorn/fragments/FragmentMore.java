package com.devotify.gabrielhorn.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.interfaces.FragmentClickListener;
import com.devotify.gabrielhorn.utility.Constants;
import com.parse.ParseUser;

public class FragmentMore extends Fragment implements OnClickListener
{
    private final String TAG = this.getClass().getSimpleName();

    ListView list_item;
    TextView welcome_title;
    RelativeLayout topBar;

    private FragmentClickListener fragClicker;

    public static Fragment newInstance()
    {
        return new FragmentMore();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragClicker = (FragmentClickListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rv = inflater.inflate(R.layout.frag_more_static, container, false);
        rv.findViewById(R.id.tvMyRewardsMore).setOnClickListener(this);

        TextView tvLogOut = (TextView) rv.findViewById(R.id.tvLogOutMore);
        tvLogOut.setOnClickListener(this);

        if (ParseUser.getCurrentUser() == null)
            tvLogOut.setText("Log in");
        else
            tvLogOut.setText("Log out");

        rv.findViewById(R.id.tvEditLocationMore).setOnClickListener(this);

        rv.findViewById(R.id.tvCallUsMore).setOnClickListener(this);
        rv.findViewById(R.id.tvEmailUsMore).setOnClickListener(this);
        rv.findViewById(R.id.tvVisitWebMore).setOnClickListener(this);
        rv.findViewById(R.id.tvShareAppMore).setOnClickListener(this);

        rv.findViewById(R.id.tvAboutAppMore).setOnClickListener(this);
        rv.findViewById(R.id.tvTermsConditionMore).setOnClickListener(this);
        rv.findViewById(R.id.tvPrivacyPolicyMore).setOnClickListener(this);

        View editStoreLocationsView = rv.findViewById(R.id.tvEditLocationMore);
        if (ParseUser.getCurrentUser() != null)
            editStoreLocationsView.setVisibility(ParseUser.getCurrentUser().getBoolean("isAdmin") ? View.VISIBLE : View.INVISIBLE);
        else
            editStoreLocationsView.setVisibility(View.GONE);

        return rv;
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    public interface OnDataPass
    {
        public void onDataPass(String data);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvMyRewardsMore:
                fragClicker.gotoRewardsTab();
                return;
            case R.id.tvLogOutMore:
                if (ParseUser.getCurrentUser() != null)
                    ParseUser.getCurrentUser().logOut();
                fragClicker.onFragmentItemClick(Constants.FRAG_MORE, true, null);
                return;
            case R.id.tvEditLocationMore:
                fragClicker.editStoreLocation();
                return;
            case R.id.tvCallUsMore:
                fragClicker.onCallUsMenuClick();
                return;
            case R.id.tvEmailUsMore:
                fragClicker.onEmailUsMenuClick();
                return;
            case R.id.tvVisitWebMore:
                fragClicker.onVisitWebMenuClick();
                return;
            case R.id.tvShareAppMore:
                fragClicker.onShareAppMenuClick();
                return;
            case R.id.tvAboutAppMore:
                fragClicker.onAboutAppMenuClick();
                return;
            case R.id.tvTermsConditionMore:
                fragClicker.onTermsConditionMenuClick();
                return;
            case R.id.tvPrivacyPolicyMore:
                fragClicker.onPrivacyPolicyMenuClick();
                return;

            default:
                return;
        }
    }
}

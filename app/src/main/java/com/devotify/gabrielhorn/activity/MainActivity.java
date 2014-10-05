package com.devotify.gabrielhorn.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.adapter.HomePageSwipeAdapter;
import com.devotify.gabrielhorn.fragments.AboutFragment;
import com.devotify.gabrielhorn.fragments.EditLocationFragment;
import com.devotify.gabrielhorn.fragments.FragmentMore;
import com.devotify.gabrielhorn.fragments.FragmentPostList;
import com.devotify.gabrielhorn.fragments.PostDetailsFragment;
import com.devotify.gabrielhorn.fragments.PrivacyPolicyFragment;
import com.devotify.gabrielhorn.fragments.TabContainerFragment;
import com.devotify.gabrielhorn.fragments.TermsAndConditionsFragment;
import com.devotify.gabrielhorn.fragments.VisitSiteFragment;
import com.devotify.gabrielhorn.interfaces.ActivityResultListener;
import com.devotify.gabrielhorn.interfaces.LogInStateListener;
import com.devotify.gabrielhorn.interfaces.RegisterActivityResultListener;
import com.devotify.gabrielhorn.model.LocalUser;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.utility.AsyncCallback;
import com.devotify.gabrielhorn.utility.FontUtils;
import com.devotify.gabrielhorn.utility.Fonts;
import com.parse.ParseAnalytics;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements FragmentPostList.FragmentPostItemClickedListener,
        FragmentMore.MoreItemClickedListener, LogInStateListener, RegisterActivityResultListener
{
    public static final long LOCATION_ALARM_DURATION = (long) (30 * 10 * 1000);
    private TabContainerFragment mTabContainerFragment;
    private ArrayList<ActivityResultListener> mActivityResultListeners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FontUtils.initialize(this, new String[]{Fonts.LIGHT});
        initLocationAlarm(this);
        ParseAnalytics.trackAppOpened(getIntent());
        getAppCompanyInfo(new AsyncCallback<Boolean>()
        {
            @Override
            public void onOperationCompleted(Boolean result)
            {
                initUI();
            }
        });
    }

    public void initUI()
    {
        mTabContainerFragment = new TabContainerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mTabContainerFragment).commit();

        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView actionBarTitleTextView = (TextView) findViewById(titleId);
        FontUtils.getInstance().overrideFonts(actionBarTitleTextView, Fonts.LIGHT);
    }

    public static void initLocationAlarm(Context context)
    {
        Intent locationAlarmIntent = new Intent(context, BackgroundNotificationService.class);
        PendingIntent pendingLocationAlarmIntent = PendingIntent.getService(context, 0,
                locationAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), LOCATION_ALARM_DURATION, pendingLocationAlarmIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_share:
                onShareAppMenuClicked();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAppCompanyInfo(final AsyncCallback<Boolean> onCompanyInitialized)
    {
        LocalUser.initialize(this, new AsyncCallback<Boolean>()
        {
            @Override
            public void onOperationCompleted(Boolean result)
            {
                onCompanyInitialized.onOperationCompleted(result);
            }
        });
    }

    @Override
    public void onPostClicked(Post post)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PostDetailsFragment.newInstance(post)).addToBackStack(null)
                .commit();
    }

    @Override
    public void onVisitWebMenuClicked()
    {
        VisitSiteFragment visitSiteFragment = VisitSiteFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, visitSiteFragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void onShareAppMenuClicked()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey I am using Gabriel Horn!");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Via"));
    }

    @Override
    public void onTermsConditionMenuClicked()
    {
        TermsAndConditionsFragment termsAndConditionsFragment = new TermsAndConditionsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, termsAndConditionsFragment).addToBackStack(null).commit();
    }

    @Override
    public void onPrivacyPolicyMenuClicked()
    {
        PrivacyPolicyFragment policyFragment = PrivacyPolicyFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, policyFragment).addToBackStack(null).commit();
    }

    @Override
    public void onRewardsClicked()
    {
        mTabContainerFragment.getHomePager().setCurrentItem(HomePageSwipeAdapter.POS_REWARDS);
    }

    @Override
    public void onEditStoreLocationClicked()
    {
        EditLocationFragment editLocationFragment = EditLocationFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editLocationFragment).addToBackStack(null).commit();
    }

    @Override
    public void onAboutAppMenuClicked()
    {
        AboutFragment aboutFragment = AboutFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, aboutFragment).addToBackStack(null).commit();
    }

    @Override
    public void onCallUsMenuClicked()
    {
        String phn = LocalUser.getInstance().getParentCompany().getString("phoneNumber");
        if (!phn.equals(""))
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phn));
            startActivity(callIntent);
        }
    }

    @Override
    public void onEmailUsMenuClicked()
    {
        String email = LocalUser.getInstance().getParentCompany().getString("email");
        if (!email.equals(""))
        {
            Intent intentMail = new Intent(Intent.ACTION_SEND);
            intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

            intentMail.setType("message/rfc822");
            startActivity(Intent.createChooser(intentMail, "Choose an Email client :"));
        }
    }

    @Override
    public void onLogInToggled(boolean requestLogin)
    {
        mTabContainerFragment.getHomePageSwipeAdapter().notifyDataSetChanged();

        if (requestLogin)
            mTabContainerFragment.getHomePager().setCurrentItem(HomePageSwipeAdapter.POS_REWARDS);
    }

    @Override
    public void registerActivityResultListener(ActivityResultListener listener)
    {
        mActivityResultListeners.add(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        for (ActivityResultListener listener : mActivityResultListeners)
        {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }
}
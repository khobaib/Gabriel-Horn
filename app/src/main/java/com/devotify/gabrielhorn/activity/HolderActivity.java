/**
 *
 */
package com.devotify.gabrielhorn.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.fragments.AddPostFragment;
import com.devotify.gabrielhorn.fragments.FragmentAboutApp;
import com.devotify.gabrielhorn.fragments.FragmentEditLocation;
import com.devotify.gabrielhorn.fragments.FragmentMore;
import com.devotify.gabrielhorn.fragments.FragmentOfferDetails;
import com.devotify.gabrielhorn.fragments.FragmentPostList;
import com.devotify.gabrielhorn.fragments.FragmentPrivacyPolicy;
import com.devotify.gabrielhorn.fragments.FragmentRewards;
import com.devotify.gabrielhorn.fragments.LoginFragment;
import com.devotify.gabrielhorn.fragments.TermsAndConditionsFragment;
import com.devotify.gabrielhorn.fragments.VisitSiteFragment;
import com.devotify.gabrielhorn.interfaces.FragmentClickListener;
import com.devotify.gabrielhorn.model.LocalUser;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.utility.AsyncCallback;
import com.devotify.gabrielhorn.utility.Constants;
import com.devotify.gabrielhorn.utility.Utils;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class HolderActivity extends FragmentActivity implements OnClickListener, FragmentClickListener, Serializable
{
    public static final String DEBUG_TAG = "Minibit Debug Tag";
    private final String TAG = this.getClass().getSimpleName();

    private View offersItemContainer, rewardsItemContainer, postItemContainer, moreItemContainer, bottomBar;
    private ImageView sharePostToggleButton;
    private TextView sharePostToggleTextView;

    private FragmentManager fragMang;
    private FragmentTransaction fragTranx;

    private static Stack<Fragment> fragBackStack;

    public static final long LOCATION_ALARM_DURATION = (long) (10 * 1000);
    public static final int POS_POST_LIST = 0, POS_REWARDS = 1, POS_SHARE_ADD_POST = 2, POS_MORE = 3, POS_LOGIN = 4;
    private static ArrayList<Fragment> fList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_holder);

        initTabButtons();
        initLocationAlarm(this);

        ParseAnalytics.trackAppOpened(getIntent());
        getAppCompanyInfo(new AsyncCallback<Boolean>()
        {
            @Override
            public void onOperationCompleted(Boolean result)
            {
                fragBackStack = new Stack<>();
                initTabButtons();
                setupFragmentList();

                fragMang = getSupportFragmentManager();
                fragTranx = fragMang.beginTransaction();
                fragTranx.add(R.id.flFragmentHolder, fList.get(0));
                fragTranx.commit();
                fragBackStack.push(fList.get(0));
                itemClicked(POS_POST_LIST);

                offersItemContainer.setOnClickListener(HolderActivity.this);
                rewardsItemContainer.setOnClickListener(HolderActivity.this);
                postItemContainer.setOnClickListener(HolderActivity.this);
                moreItemContainer.setOnClickListener(HolderActivity.this);
            }
        });
    }

    public static void initLocationAlarm(Context context)
    {
        Intent locationAlarmIntent = new Intent(context, BackgroundNotificationService.class);
        PendingIntent pendingLocationAlarmIntent = PendingIntent.getService(context, 0,
                locationAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), LOCATION_ALARM_DURATION, pendingLocationAlarmIntent);
    }

    private void setupFragmentList()
    {
        fList = new ArrayList<>();
        fList.add(POS_POST_LIST, FragmentPostList.newInstance());
        fList.add(POS_REWARDS, FragmentRewards.newInstance());
        fList.add(POS_SHARE_ADD_POST, AddPostFragment.newInstance());
        fList.add(POS_MORE, FragmentMore.newInstance());
        fList.add(POS_LOGIN, LoginFragment.newInstance());
    }

    @Override
    public void onClick(View v)
    {
        fragTranx = fragMang.beginTransaction();
        boolean isLoggedIn = (ParseUser.getCurrentUser() != null);
        switch (v.getId())
        {
            case R.id.action_offers:
                fragTranx.setCustomAnimations(R.anim.slide_out_leftright, R.anim.slide_in_left_to_right);
                fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_POST_LIST));
                fragTranx.commit();
                if (fragBackStack.contains(fList.get(POS_POST_LIST)))
                    clearStackUptoPos(POS_POST_LIST);
                fragBackStack.push(fList.get(POS_POST_LIST));
                itemClicked(POS_POST_LIST);
                break;
            case R.id.action_rewards:
                if (isLoggedIn)
                {
                    fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_REWARDS));
                    if (fragBackStack.contains(fList.get(POS_REWARDS)))
                        clearStackUptoPos(POS_REWARDS);
                    fragBackStack.push(fList.get(POS_REWARDS));
                }
                else
                {
                    fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_LOGIN));
                    if (fragBackStack.contains(fList.get(POS_LOGIN)))
                        clearStackUptoPos(POS_LOGIN);
                    fragBackStack.push(fList.get(POS_LOGIN));
                }
                fragTranx.commit();
                itemClicked(POS_REWARDS);
                break;
            case R.id.action_share_add_post:

                if (isLoggedIn)
                {
                    fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_SHARE_ADD_POST));
                    fragTranx.commit();
                    if (fragBackStack.contains(fList.get(POS_SHARE_ADD_POST)))
                        clearStackUptoPos(POS_SHARE_ADD_POST);
                    fragBackStack.push(fList.get(POS_SHARE_ADD_POST));
                }
                else
                {
                    shareTheApp();
                }

                itemClicked(POS_SHARE_ADD_POST);
                break;
            case R.id.action_more:
                fragTranx.setCustomAnimations(R.anim.slide_out_rightleft, R.anim.slide_in_right_toleft);
                fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_MORE));
                fragTranx.commit();
                if (fragBackStack.contains(fList.get(POS_MORE)))
                    clearStackUptoPos(POS_MORE);
                fragBackStack.push(fList.get(POS_MORE));
                itemClicked(POS_MORE);
                break;
        }

        bottomBar.setBackgroundColor(getResources().getColor(v.getId() == R.id.action_rewards ?
                R.color.bottom_bar_rewards_color : R.color.bottom_bar_color));
    }

    private void clearStackUptoPos(int pos)
    {
        Fragment f = fList.get(pos);
        while (!(fragBackStack.isEmpty()) && (fragBackStack.pop() != f))
            Log.d(TAG, "Clearing frag upto pos=" + pos + ": fragBackStack.size() = " + fragBackStack.size());
    }

    private void clearStackUptoFragment(Fragment f)
    {
        while (!(fragBackStack.isEmpty()) && (fragBackStack.pop() != f))
            Log.d(TAG, "Clearing frag upto f: fragBackStack.size() = " + fragBackStack.size());
    }

    private void initTabButtons()
    {
        bottomBar = findViewById(R.id.bottom_bar).findViewById(R.id.bottom_bar_background_view);
        offersItemContainer = findViewById(R.id.action_offers);
        rewardsItemContainer = findViewById(R.id.action_rewards);
        postItemContainer = findViewById(R.id.action_share_add_post);
        moreItemContainer = findViewById(R.id.action_more);
        sharePostToggleButton = (ImageView) findViewById(R.id.share_post_toggle_button);
        sharePostToggleTextView = (TextView) findViewById(R.id.share_post_toggle_text_view);
        updatePostShareToggle();
    }


    private void shareTheApp()
    {
        Log.e(TAG, "shareTheApp");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey I am using Gabriel Horn!");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Via"));
    }

    @Override
    public void onBackPressed()
    {
        if (fragBackStack.size() <= 1)
        {
            Log.e(TAG, "Going out from the app with no fragment remaining in the back-stack.");
            super.onBackPressed();
        }
        else
        {
            fragBackStack.pop();
            Log.i(TAG, "Popping out last fragment from the back stack with size: " + fragBackStack.size());
            fragTranx = fragMang.beginTransaction();
            fragTranx.replace(R.id.flFragmentHolder, fragBackStack.peek());
            fragTranx.commit();
        }
    }

    @Override
    public void onFragmentItemClick(int fragType, boolean doLogIn, Post post)
    {
        if (fragType == Constants.FRAG_REWARD)
        {
            fragTranx = fragMang.beginTransaction();
            fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_REWARDS));
            fragTranx.commit();
            if (fragBackStack.contains(fList.get(POS_REWARDS)))
                clearStackUptoPos(POS_REWARDS);
            fragBackStack.push(fList.get(POS_REWARDS));
        }
        else if (fragType == Constants.FRAG_OFFER)
        {
            fragTranx = fragMang.beginTransaction();
            Fragment f = FragmentOfferDetails.newInstance(post);
            fragTranx.setCustomAnimations(R.anim.slide_out_rightleft, R.anim.slide_in_right_toleft);
            fragTranx.replace(R.id.flFragmentHolder, f);
            fragTranx.commit();
            if (fragBackStack.contains(f))
                clearStackUptoFragment(f);
            fragBackStack.push(f);
        }
        else if (fragType == Constants.FRAG_MORE)
        {
            if (doLogIn)
            {
                fragTranx = fragMang.beginTransaction();
                fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_LOGIN));
                fragTranx.commit();
                if (fragBackStack.contains(fList.get(POS_LOGIN)))
                    clearStackUptoPos(POS_LOGIN);
                fragBackStack.push(fList.get(POS_LOGIN));
            }
        }
        else if (fragType == Constants.FRAG_LOGGED_IN)
        {
            fragTranx = fragMang.beginTransaction();
            fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            fragTranx.replace(R.id.flFragmentHolder, fList.get(POS_REWARDS));
            fragTranx.commit();
            if (fragBackStack.contains(fList.get(POS_REWARDS)))
                clearStackUptoPos(POS_REWARDS);
            fragBackStack.push(fList.get(POS_REWARDS));

            updatePostShareToggle();
        }

        bottomBar.setBackgroundColor(getResources().getColor(fragType == Constants.FRAG_REWARD ?
                R.color.bottom_bar_rewards_color : R.color.bottom_bar_color));
    }

    private void getAppCompanyInfo(final AsyncCallback<Boolean> onCompanyInitialized)
    {
        LocalUser.initialize(this, new AsyncCallback<Boolean>()
        {
            @Override
            public void onOperationCompleted(Boolean result)
            {
                ParseObject company = LocalUser.getInstance().getParentCompany();
                String appId = company.getObjectId();
                Utils.writeString(HolderActivity.this, Utils.KEY_PARENT_APP_ID, appId);
                String companyPhn = company.getString("phoneNumber");
                Utils.writeString(HolderActivity.this, Utils.APP_COMPANY_PHONE, companyPhn);
                String companyEmail = company.getString("email");
                Utils.writeString(HolderActivity.this, Utils.APP_COMPANY_EMAIL, companyEmail);
                String companySite = company.getString("websiteUrl");
                Utils.writeString(HolderActivity.this, Utils.VISIT_OUR_SITE, companySite);

                onCompanyInitialized.onOperationCompleted(result);
            }
        });
    }

    @Override
    public void gotoRewardsTab()
    {
        fragTranx = fragMang.beginTransaction();
        fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        Fragment f;
        if (ParseUser.getCurrentUser() == null)
            f = fList.get(4);
        else
            f = fList.get(1);
        fragTranx.replace(R.id.flFragmentHolder, f);
        fragTranx.commit();
        if (fragBackStack.contains(f))
            clearStackUptoFragment(f);
        fragBackStack.push(f);
    }

    @Override
    public void logInOrOut()
    {
    }

    @Override
    public void editStoreLocation()
    {
        fragTranx = fragMang.beginTransaction();
        fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        Fragment f = FragmentEditLocation.newInstance();
        fragTranx.replace(R.id.flFragmentHolder, f);
        fragTranx.commit();
        if (fragBackStack.contains(f))
            clearStackUptoFragment(f);
        fragBackStack.push(f);
    }

    @Override
    public void onCallUsMenuClick()
    {
        String phn = Utils.readString(HolderActivity.this, Utils.APP_COMPANY_PHONE, "");
        if (!phn.equals(""))
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phn));
            startActivity(callIntent);
        }
    }

    @Override
    public void onEmailUsMenuClick()
    {
        String email = Utils.readString(HolderActivity.this, Utils.APP_COMPANY_EMAIL, "");
        if (!email.equals(""))
        {
            Intent intentMail = new Intent(Intent.ACTION_SEND);
            intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

            intentMail.setType("message/rfc822");
            startActivity(Intent.createChooser(intentMail, "Choose an Email client :"));
        }
    }

    @Override
    public void onVisitWebMenuClick()
    {
        fragTranx = fragMang.beginTransaction();
        fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        VisitSiteFragment visitSiteFragment = new VisitSiteFragment();
        fragTranx.replace(R.id.flFragmentHolder, visitSiteFragment);
        fragTranx.commit();
        if (fragBackStack.contains(visitSiteFragment))
            clearStackUptoFragment(visitSiteFragment);
        fragBackStack.push(visitSiteFragment);
    }

    @Override
    public void onShareAppMenuClick()
    {
        shareTheApp();
    }

    @Override
    public void onAboutAppMenuClick()
    {
        fragTranx = fragMang.beginTransaction();
        fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        Fragment aboutFragment = FragmentAboutApp.newInstance();
        fragTranx.replace(R.id.flFragmentHolder, aboutFragment);
        fragTranx.commit();
        if (fragBackStack.contains(aboutFragment))
            clearStackUptoFragment(aboutFragment);
        fragBackStack.push(aboutFragment);
    }

    @Override
    public void onTermsConditionMenuClick()
    {
        fragTranx = fragMang.beginTransaction();
        fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        TermsAndConditionsFragment tcFragment = new TermsAndConditionsFragment();
        fragTranx.replace(R.id.flFragmentHolder, tcFragment);
        fragTranx.commit();
        if (fragBackStack.contains(tcFragment))
            clearStackUptoFragment(tcFragment);
        fragBackStack.push(tcFragment);
    }

    @Override
    public void onPrivacyPolicyMenuClick()
    {
        fragTranx = fragMang.beginTransaction();
        fragTranx.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        Fragment aboutFragment = FragmentPrivacyPolicy.newInstance();
        fragTranx.replace(R.id.flFragmentHolder, aboutFragment);
        fragTranx.commit();
        if (fragBackStack.contains(aboutFragment))
            clearStackUptoFragment(aboutFragment);
        fragBackStack.push(aboutFragment);
    }

    public void itemClicked(int position)
    {
        for (int i = 0; i < 4; i++)
        {
            View view = getViewForPosition(i);
            switchColors(view, i != position);
        }
    }

    public View getViewForPosition(int position)
    {
        switch (position)
        {
            case POS_POST_LIST:
                return offersItemContainer;
            case POS_REWARDS:
                return rewardsItemContainer;
            case POS_MORE:
                return moreItemContainer;
            case POS_SHARE_ADD_POST:
                return postItemContainer;
        }

        return null;
    }

    public void switchColors(View container, boolean toPrimary)
    {
        int resourceColor = toPrimary ? R.color.primary_color : R.color.secondary_color;
        resourceColor = getResources().getColor(resourceColor);
        ViewGroup parentView = (ViewGroup) container;
        for (int i = 0; i < parentView.getChildCount(); i++)
        {
            View childView = parentView.getChildAt(i);
            if (childView instanceof TextView)
            {
                ((TextView) childView).setTextColor(resourceColor);
            }
            else if (childView instanceof ImageView)
            {
                ImageView imageView = (ImageView) childView;
                if (imageView.getDrawable() != null)
                {
                    Drawable childViewBackground = imageView.getDrawable().mutate();
                    childViewBackground.clearColorFilter();
                    childViewBackground.setColorFilter(resourceColor, PorterDuff.Mode.MULTIPLY);
                }
            }
        }
    }

    public void updatePostShareToggle()
    {
        if (ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().getBoolean("isAdmin"))
        {
            sharePostToggleButton.setImageResource(R.drawable.add_post_icon);
            sharePostToggleTextView.setText(R.string.add_post_title);
        }
        else
        {
            sharePostToggleButton.setImageResource(R.drawable.share_icon);
            sharePostToggleTextView.setText(R.string.share_title);
        }
    }
}

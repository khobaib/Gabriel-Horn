/**
 *
 */
package com.devotify.gabrielhorn.interfaces;

import com.devotify.gabrielhorn.model.Post;

/**
 * @author Touhid
 */
public interface FragmentClickListener
{

    public void onFragmentItemClick(int fragType, boolean doLogIn, Post post);

    public void gotoRewardsTab();

    public void logInOrOut();

    public void editStoreLocation();

    public void onCallUsMenuClick();

    public void onEmailUsMenuClick();

    public void onVisitWebMenuClick();

    public void onShareAppMenuClick();

    public void onAboutAppMenuClick();

    public void onTermsConditionMenuClick();

    public void onPrivacyPolicyMenuClick();

}

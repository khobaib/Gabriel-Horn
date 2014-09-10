/**
 * 
 */
package com.minibittechnologies.interfaces;

import java.io.Serializable;

import com.minibittechnologies.model.Post;

/**
 * @author Touhid
 * 
 */
public interface FragmentClickListener extends Serializable {

	public void onFragmentItemClick(int fragType, boolean doLogIn, Post tPsot);
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

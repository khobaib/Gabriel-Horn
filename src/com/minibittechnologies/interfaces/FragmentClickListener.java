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

	long serialId = 0L;

	public void onFragmentItemClick(int fragType, boolean doLogIn, Post tPsot);

}

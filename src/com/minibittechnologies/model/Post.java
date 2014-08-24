package com.minibittechnologies.model;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Post")
public class Post extends ParseObject {

	public String getAppCompany() {
		return getString("appCompany");
	}

	public void setAppCompany(String value) {
		put("appCompany", value);
	}

	public Date getExpired() {
		return getDate("expiration");
	}

	public void setExpired(Date value) {
		put("expiration", value);
	}

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String value) {
		put("title", value);
	}

	public String getContent() {
		return getString("contents");
	}

	public void setContent(String value) {
		put("contents", value);
	}

	public String getLink() {
		return getString("link");
	}

	public void setLink(String value) {
		put("link", value);
	}

	public static ParseQuery<Post> getQuery() {
		return ParseQuery.getQuery(Post.class);
	}

}

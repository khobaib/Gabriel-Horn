package com.minibittechnologies.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.minibittechnologies.model.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;

public class GabrielHornApplication extends Application {
	private static Context context;
	protected SharedPreferences User;

	@Override
	public void onCreate() {
		super.onCreate();
		ParseObject.registerSubclass(Post.class);
		Parse.initialize(this, "fy6nHyWuCJtnH0w2bPaCY4GO9e5F7UnDm1bz81oA", "uXCrbxPSX4YDEQkPuitrmBB1nF7WFOslT1cNWea1");
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);

		context = getApplicationContext();
		User = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static Context getAppContext() {
		return context;
	}
}

package com.devotify.gabrielhorn.utility;

import android.app.Application;

import com.devotify.gabrielhorn.activity.HolderActivity;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.model.RetailLocation;
import com.devotify.gabrielhorn.model.Reward;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

public class GabrielHornApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Reward.class);
        ParseObject.registerSubclass(RetailLocation.class);

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "fy6nHyWuCJtnH0w2bPaCY4GO9e5F7UnDm1bz81oA", "uXCrbxPSX4YDEQkPuitrmBB1nF7WFOslT1cNWea1");
        PushService.setDefaultPushCallback(this, HolderActivity.class);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }
}

package com.devotify.gabrielhorn.utility;

import android.app.Application;

import com.devotify.gabrielhorn.activity.MainActivity;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.model.RetailLocation;
import com.devotify.gabrielhorn.model.Reward;
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
        PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}

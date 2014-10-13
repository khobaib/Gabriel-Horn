package com.devotify.gabrielhorn.activity;

import android.app.Application;

import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.model.RetailLocation;
import com.devotify.gabrielhorn.model.Reward;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class GabrielHornApplication extends Application
{
    public static final String CHANNEL_NAME = "PushGabrielHorn";

    @Override
    public void onCreate()
    {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Reward.class);
        ParseObject.registerSubclass(RetailLocation.class);

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "fy6nHyWuCJtnH0w2bPaCY4GO9e5F7UnDm1bz81oA", "uXCrbxPSX4YDEQkPuitrmBB1nF7WFOslT1cNWea1");
        ParsePush.subscribeInBackground(CHANNEL_NAME, new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if (e != null)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}

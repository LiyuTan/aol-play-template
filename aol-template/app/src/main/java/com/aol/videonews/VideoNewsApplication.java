package com.aol.videonews;

import android.app.Application;

import com.aol.aolon.sdk.Sdk;

public class VideoNewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Sdk.init(getApplicationContext(), Constants.AOL_ON_APP_SID, Constants.AOL_ON_APP_SKEY);
    }
}
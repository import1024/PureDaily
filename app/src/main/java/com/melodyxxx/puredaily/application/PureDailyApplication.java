package com.melodyxxx.puredaily.application;

import android.app.Application;

import org.xutils.x;

public class PureDailyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}

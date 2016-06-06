package com.melodyxxx.puredaily.application;

import android.app.Application;

import com.melodyxxx.puredaily.db.Dao;
import com.melodyxxx.puredaily.utils.CrashHandler;

import org.xutils.x;

public class PureDailyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        Dao.initialize(this);
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }
}

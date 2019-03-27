package com.aviad.footballwithfriends;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class AppManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}

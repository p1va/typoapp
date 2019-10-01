package com.github.p1va.typoapp;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Stefano Piva on 01/08/2018.
 */
public class TypoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //TODO:
        }
    }
}

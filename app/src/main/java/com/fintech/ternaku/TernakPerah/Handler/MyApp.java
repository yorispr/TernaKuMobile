package com.fintech.ternaku.TernakPerah.Handler;

import android.app.Application;

/**
 * Created by pandu on 10/20/16.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new LoggingExceptionHandler(this);
    }
}
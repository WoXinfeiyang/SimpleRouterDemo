package com.fxj.simplerouterdemo;

import android.app.Application;

import com.fxj.simplerouter.SimpleRouter;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SimpleRouter.getInstance().init(this);
    }
}

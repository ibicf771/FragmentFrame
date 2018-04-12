package com.architecture.android.fragmentframe;

import android.app.Application;

import com.architecture.android.fragmentframe.util.LeakCanaryUtil;

/**
 * Created by yangsimin on 2018/3/29.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanaryUtil.install(this);
    }
}

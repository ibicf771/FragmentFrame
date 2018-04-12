package com.architecture.android.fragmentframe.util;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by hongliang.xie on 2015/7/6.
 */
public class LeakCanaryUtil {
    static RefWatcher mRefWatcher;

    public static void install(Application app) {
        mRefWatcher = LeakCanary.install(app);
    }


    public static void watch(Object object) {
        if (mRefWatcher != null) {
            mRefWatcher.watch(object);
        }
    }

}

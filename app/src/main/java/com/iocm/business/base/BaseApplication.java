package com.iocm.business.base;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Administrator on 2016/3/22.
 */
public class BaseApplication extends Application {

    private static Context applicationContext;


    public static Context getContext() {
        return applicationContext;
    }

    public static void setContext(Context context) {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "UzXOtJPowBBMzJU99YAbaze8-gzGzoHsz", "7pifz4aAAc3noJ3YYfYklNsq");
    }
}

package com.iocm.business.utils;

import com.google.gson.Gson;

/**
 * Created by liubo on 15/6/22.
 */
public class GsonUtils {

    private static GsonUtils mGsonUtils;
    private Gson mGson;

    public static synchronized GsonUtils getInstance() {
        if (mGsonUtils == null) {
            mGsonUtils = new GsonUtils();
        }
        return mGsonUtils;
    }

    private GsonUtils() {
        mGson = new Gson();
    }

    public Gson getGson() {
        return mGson;
    }
}

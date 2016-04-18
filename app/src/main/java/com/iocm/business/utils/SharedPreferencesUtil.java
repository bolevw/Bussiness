package com.iocm.business.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.iocm.business.model.NameValue;

/**
 * Created by Jack on 2016/3/24.
 */
public class SharedPreferencesUtil<T> {

    private SharedPreferences mSharedPreferences;

    private Context context;

    private static SharedPreferencesUtil cache;


    private SharedPreferences.Editor mEditor;

    public static SharedPreferencesUtil getInstance(Context context) {
        if (cache == null) {
            cache = new SharedPreferencesUtil(context);
        }
        return cache;
    }

    private SharedPreferencesUtil(Context context) {

        mSharedPreferences = context.getSharedPreferences("Business", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

    }

    public void saveValue(NameValue<T> value) {
        if (value.getValue() instanceof String) {
            mEditor.putString(value.getName(), (String) value.getValue());
        } else if (value.getValue() instanceof Boolean) {
            mEditor.putBoolean(value.getName(), (Boolean) value.getValue());
        } else if (value.getValue() instanceof Integer) {
            mEditor.putInt(value.getName(), (Integer) value.getValue());
        }
        mEditor.commit();
    }


    public String getStringValue(String name) {
        return mSharedPreferences.getString(name, "");
    }

    public Boolean getBooleanValue(String name) {
        return mSharedPreferences.getBoolean(name, false);
    }

    public Integer getIntValue(String name) {
        return mSharedPreferences.getInt(name, -1);
    }


}

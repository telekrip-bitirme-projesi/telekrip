package com.dashboard.telekrip.Tools;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dashboard.telekrip.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SharedPrefences {

    private static SharedPreferences preferences = null;
    private static SharedPreferences.Editor editor = null;
    private static Date time = null;
    private static SimpleDateFormat df = null;

    public static String getDate() {
        if (time == null) {
            time = Calendar.getInstance().getTime();
            df = new SimpleDateFormat("dd-MMM-yyyy");
            return df.format(time);
        } else {
            return df.format(time);
        }
    }

    public static void setSharedPrefences(Context ctx, String key, Object value) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
            editor = preferences.edit();
        }
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.apply();
    }

    public static Object getSharedPrefences(Context ctx, String key, Class<?> type) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        }
        if (type == String.class) {
            return preferences.getString(key, null);
        } else if (type == Boolean.class) {
            return preferences.getBoolean(key, false);
        } else if (type == Integer.class) {
            return preferences.getInt(key, -1);
        } else if (type == Float.class) {
            return preferences.getFloat(key, -1);
        }
        return null;
    }

    public static void removeSharedPrefences(Context ctx,String key){
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
            editor = preferences.edit();
        }
        editor.remove(key);
        editor.apply();
    }

    public static void loggedInUser(Context ctx, User usr){
        setSharedPrefences(ctx,"id",usr.getId());
        setSharedPrefences(ctx,"username",usr.getUserName());
        setSharedPrefences(ctx,"avatar",usr.getAvatar());
    }
    public static void logoutUser(Context ctx){
        removeSharedPrefences(ctx,"id");
        removeSharedPrefences(ctx,"username");
        removeSharedPrefences(ctx,"avatar");
    }

}

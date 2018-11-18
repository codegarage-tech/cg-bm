package com.rc.buyermarket.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.common.reflect.TypeParameter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppPref {



    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getPreferences(Context context, String prefKey) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(prefKey, "");
    }

    public static void setBooleanSetting(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanSetting(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);
    }

    public static <T>  void saveObject(Context context,String key, T object) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String objectString = new Gson().toJson(object);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, objectString);
        editor.apply();
    }

    public static <T> T  getObject(Context context,String key, Class<T> classType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (isValidKey2(context,key)) {
            String objectString = sp.getString(key, null);
            if (objectString != null) {
                return new Gson().fromJson(objectString, classType);
            }
        }
        return null;
    }

    public static <T> void saveObjectsList(Context context,String key, List<T> objectList) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String objectString = new Gson().toJson(objectList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, objectString);
        editor.apply();
    }

    public static <T> void saveObjectsArrayList(Context context,String key, List<T> objectList) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String objectString = new Gson().toJson(objectList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, objectString);
        editor.apply();
    }

    public static <T> List<T> getObjectsList(Context context,String key, Class<T> classType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        if (isValidKey2(context,key)) {
            String objectString = sp.getString(key, null);
            if (objectString != null) {
                return new Gson().fromJson(objectString, new com.google.common.reflect.TypeToken<List<T>>() {
                }
                        .where(new TypeParameter<T>() {
                        }, classType)
                        .getType());
            }
        }

        return null;
    }

    public <T> ArrayList<T> getObjectsArrayList(Context context,String key, Class<T> classType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (isValidKey2(context,key)) {
            String objectString = sp.getString(key, null);
            if (objectString != null) {
                return new Gson().fromJson(objectString, new com.google.common.reflect.TypeToken<List<T>>() {
                }
                        .where(new TypeParameter<T>() {
                        }, classType)
                        .getType());
            }
        }

        return null;
    }

    public static  void clearSession(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean deleteValue(Context context,String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (isValidKey2(context,key)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.apply();
            return true;
        }

        return false;
    }

    private static boolean isValidKey2(Context context,String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> map = sp.getAll();
        if (map.containsKey(key)) {
            return true;
        } else {
            Log.e("SharePref", "No element founded in sharedPrefs with the key " + key);
            return false;
        }
    }

}

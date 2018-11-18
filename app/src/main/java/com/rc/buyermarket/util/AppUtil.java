package com.rc.buyermarket.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.rc.buyermarket.R;

import java.util.ArrayList;


/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AppUtil {

    public static boolean isDebug(Context context) {
//        return BuildConfig.DEBUG;
        return ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }

    //Toolbar
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static boolean isNullOrEmpty(String myString) {
        if (myString == null) {
            return true;
        } else {
            return myString.length() == 0 || myString.equalsIgnoreCase("null") || myString.equalsIgnoreCase("");
        }
    }

    public static String  SplitDateList (String str)    {

        String mainString="";
        if (str.equals("")){
            return "";
        }
        String[] numberArray = str.split("\\+");
        for (String item : numberArray)
        {
            mainString = item;
            Log.e("mainString",mainString+">>>");
            System.out.println("item = " + item);
        }
        return mainString;
    }


//    public static int getGridSpanCount(Activity activity) {
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        display.getMetrics(displayMetrics);
//        float screenWidth = displayMetrics.widthPixels;
//        float cellWidth = activity.getResources().getDimension(R.dimen.item_product_width);
//        return Math.round(screenWidth / cellWidth);
//    }


    public static String formatLocationInfo(String myString) {
        String location = "";
        if (myString != null && myString.trim().length() > 0) {
            location = myString.startsWith(",") ? myString.substring(1).trim().replaceAll(", ,", ",") : myString.replaceAll(", ,", ",");
        }
        return location;
    }




//    public static void displayImagePath(Context ctx, ImageView img, int path) {
//        try {
//
//            RequestOptions ro = new RequestOptions();
//            ro.diskCacheStrategy( DiskCacheStrategy.ALL);
//            ro.dontAnimate();
//            ro.dontTransform();
//            ro.encodeFormat( Bitmap.CompressFormat.PNG);
//            ro.format( DecodeFormat.PREFER_ARGB_8888);
//            ro.priority( Priority.HIGH);
//
//            Glide.with(ctx)
//                    .applyDefaultRequestOptions(ro)
//                    .load(path)
//                    .into(img);
//
//        } catch (Exception e) {
//        }
//    }
//
//    public static void displayImageOriginal(Context ctx, ImageView img, String url) {
//        try {
//
//            Log.e( "urlcardObj" ,url+"");
//
//            RequestOptions ro = new RequestOptions();
//            ro.circleCrop();
//            ro.circleCropTransform();
//            ro.diskCacheStrategy( DiskCacheStrategy.ALL);
//            ro.dontAnimate();
//            ro.dontTransform();
//            ro.encodeFormat( Bitmap.CompressFormat.PNG);
//            ro.format( DecodeFormat.PREFER_ARGB_8888);
//            ro.priority( Priority.HIGH);
//
//            Glide.with(ctx)
//                    .load(url)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(img);
//
//        } catch (Exception e) {
//        }
//    }
//


}
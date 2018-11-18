package com.rc.buyermarket.util;

import android.util.Log;

import com.rc.buyermarket.model.card.ParamCheckout;

import org.json.JSONObject;

import static android.support.constraint.Constraints.TAG;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AllUrls {
    private static String BASE_URL = "http://thebuyermarket.com/buyer_market/index.php/";

    public static String getSendOrderUrl() {
        String url = BASE_URL + "orders/add";
        Log.d(TAG, "getSendOrderUrl: " + url);
        return url;
    }

    public static JSONObject getSendOrderParam(ParamCheckout paramSendOrder) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(ParamCheckout.getResponseString(paramSendOrder));
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = new JSONObject();
        }
        Log.d(TAG, "getSendOrderParam: " + jsonObject.toString());
        return jsonObject;
    }

}
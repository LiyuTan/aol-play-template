package com.aol.videonews.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.aol.videonews.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Operations about user accounts
 */

public final class AccountsApi {

    public static boolean login(Context context, String provider, String providerAccessToken, String providerAccessTokenSecret, String deviceAccessToken) {
        if (null == context) return false;

        final String bodyStr = String.format(Locale.US, "provider=%s&providerAccessToken=%s&providerAccessTokenSecret=%s&deviceAccessToken=%s&device=%s",
                               provider, providerAccessToken, providerAccessTokenSecret, deviceAccessToken, ApiUtils.getDeviceParam(context)),
                     url = Constants.API_BASE_URL + "/app/accounts/login";

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpPost(context, url, bodyStr, false);
        if (null == dataObj) return false;

        final String accessToken = dataObj.optString("accessToken");

        if (!TextUtils.isEmpty(accessToken)) {
            ApiUtils.setAccessToken(context, accessToken, true);
            return true;
        }

        return false;
    }

    public static boolean isLoginToAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.AOL_ON_PREFS_NAME, 0);
        return prefs.getBoolean(Constants.AOL_ON_PREF_KEY_IS_LOGIN_TO_ACCOUNT, false);
    }

    public static boolean postAccountTags(Context context, String tags) {
        if (null == context) return false;

        final String bodyStr = TextUtils.isEmpty(tags) ? "tags=," : String.format(Locale.US, "tags=%s", tags),
                url = Constants.API_BASE_URL + "/app/accounts/tags";

        String responseStr = ApiUtils.fetchResponseStringWithHttpPost(context, url, bodyStr, true);

        return ApiUtils.isSucceeded(responseStr);
    }

    public static String getTags(Context context) {
        final String url = Constants.API_BASE_URL + "/app/accounts/info";

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpGet(context, url);
        if (null != dataObj) {

            JSONArray tagsArray = dataObj.optJSONArray("tags");
            if (null == tagsArray || 0 == tagsArray.length()) return null;

            StringBuilder sb = new StringBuilder();
            final int len = tagsArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    sb.append(tagsArray.getString(i));
                    sb.append(",");
                } catch (JSONException ex) {
                }
            }
            return sb.toString();
        }

        return null;
    }
}
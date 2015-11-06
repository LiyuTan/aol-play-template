package com.aol.videonews.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.aol.videonews.Constants;

import org.json.JSONObject;

import java.util.Locale;
import java.util.UUID;

/**
 * Operations about user devices
 */

public final class DevicesApi {
    public static boolean login(Context context) {
        if (null == context) return false;

        SharedPreferences prefs = context.getSharedPreferences(Constants.AOL_ON_PREFS_NAME, 0);
        String uuid = prefs.getString(Constants.AOL_ON_PREF_KEY_DEVICE_UUID, null);

        if (null == uuid) {
            uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.AOL_ON_PREF_KEY_DEVICE_UUID, uuid);
            editor.commit();
        }

        final String deviceName = ApiUtils.generateDeviceName(),
                     bodyStr = String.format(Locale.US, "uuid=%s&deviceName=%s", uuid, deviceName),
                     url = Constants.API_BASE_URL + "/app/devices/login";

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpPost(context, url, bodyStr, false);
        if (null == dataObj) return false;

        final String accessToken = dataObj.optString("accessToken");

        if (!TextUtils.isEmpty(accessToken)) {
            ApiUtils.setAccessToken(context, accessToken, false);
            return true;
        }

        return false;
    }
}

package com.aol.videonews.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.aol.videonews.Constants;

/**
 * Some API helpers
 */

public class Api {

    public static void logout(Context context) {
        ApiUtils.setAccessToken(context, null, false);
    }

    public static String getAccessToken(Context context) {
        return ApiUtils.getAccessToken(context);
    }

    public static boolean isLoginToAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.AOL_ON_PREFS_NAME, 0);
        return prefs.getBoolean(Constants.AOL_ON_PREF_KEY_IS_LOGIN_TO_ACCOUNT, false);
    }

    public static void useDevEnvironment() {
        Constants.API_BASE_URL = Constants.DEV_API_BASE_URL;
    }

    public static void useStagingEnvironment() {
        Constants.API_BASE_URL = Constants.STAGING_API_BASE_URL;
    }

    public static void usePrdEnvironment() {
        Constants.API_BASE_URL = Constants.PRD_API_BASE_URL;
    }
}

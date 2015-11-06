package com.aol.videonews.api;

import android.content.Context;

import com.aol.videonews.Constants;

import java.util.Locale;

public final class FavoriteApi {

    public static boolean addFavoriteVideo(Context context, String videoId) {
        final String url = Constants.API_BASE_URL + String.format(Locale.US, "/app/favorite/%s/%s?device=%s", Constants.API_APP_ID, videoId, ApiUtils.getDeviceParam(context)),
                responseStr = ApiUtils.fetchResponseStringWithHttpPost(context, url, "", true);
        return ApiUtils.isSucceeded(responseStr);
    }

    public static boolean deleteFavoriteVideo(Context context, String videoId) {
        final String url = Constants.API_BASE_URL + String.format(Locale.US, "/app/favorite/%s/%s", Constants.API_APP_ID, videoId),
                responseStr = ApiUtils.fetchResponseStringWithHttpDelete(context, url, true);
        return ApiUtils.isSucceeded(responseStr);
    }
}

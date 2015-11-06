package com.aol.videonews.api;

import android.content.Context;

import com.aol.videonews.Constants;

import java.util.Locale;

/**
 * Operations about user history
 */

public final class HistoryApi {

    public static boolean postHistory(Context context, String videoId, int videoOffsetInSecond
            , boolean finished) {
        final String url = Constants.API_BASE_URL + String.format(Locale.US, "/app/history/videos/%s", videoId),
                bodyStr = String.format(Locale.US, "videoOffset=%d&videoFinished=%s", videoOffsetInSecond, finished ? "true" : "false"),
                responseStr = ApiUtils.fetchResponseStringWithHttpPost(context, url, bodyStr, true);
        return ApiUtils.isSucceeded(responseStr);
    }

    public static boolean deleteAll(Context context) {
        final String url = Constants.API_BASE_URL + "/app/history/videos",
                     responseStr = ApiUtils.fetchResponseStringWithHttpDelete(context, url, true);
        return ApiUtils.isSucceeded(responseStr);
    }
}

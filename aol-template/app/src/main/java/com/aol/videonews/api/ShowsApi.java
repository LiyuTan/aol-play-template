package com.aol.videonews.api;

import android.content.Context;

import com.aol.videonews.Constants;
import com.aol.videonews.model.Show;

import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Operations about shows
 */

public final class ShowsApi {

    public static String getImageUrl(String showId, int width, int height) {
        return Constants.API_BASE_URL.replace("https://", "http://") + String.format(Locale.US, "/app/shows/%s/%s/images/%sx%s.%s",
                Constants.API_APP_ID, showId, width, height, Constants.DEFAULT_IMAGE_FORMAT);
    }

    /**
     * Return a show object which contains its playlists
     * @param showId
     * @return
     */
    public static Show getShowDetail(Context context, String showId) {
        final Date now = new Date();
        final TimeZone timeZone = TimeZone.getDefault();
        final int time = (int) (now.getTime() / 1000L),
                timezoneOffset = timeZone.getOffset(now.getTime()) / 1000;
        final String url = Constants.API_BASE_URL + String.format(Locale.US, "/app/shows/%s/%s/playlists?time=%d&timezoneOffset=%d",
                Constants.API_APP_ID, showId, time, timezoneOffset);

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpGet(context, url);
        return ApiUtils.parseShowObject(dataObj);
    }
}
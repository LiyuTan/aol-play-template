package com.aol.videonews.api;

import android.content.Context;
import android.text.TextUtils;

import com.aol.videonews.Constants;
import com.aol.videonews.model.Page;
import com.aol.videonews.model.Playlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.aol.videonews.R;

/**
 * Operations about app pages
 */
public final class PagesApi {
    /**
     * Return all pages of the current app.
     * @return
     */
    public static Page[] getPages(Context context) {
        if (null == context) return null;

        final String appId = context.getResources().getString(R.string.aolon_api_app_id);
        if (TextUtils.isEmpty(appId)) return null;

        final String url = Constants.API_BASE_URL + String.format(Locale.US, "/app/pages/%s?includeChannels=false&device=%s",
                Constants.API_APP_ID, ApiUtils.getDeviceParam(context));

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpGet(context, url);
        if (null != dataObj) {
            JSONArray pagesArray = dataObj.optJSONArray("pages");
            if (null == pagesArray || 0 == pagesArray.length()) return null;

            final int len = pagesArray.length();
            Page[] result = new Page[len];
            for (int i = 0; i < len; i++) {
                try {
                    JSONObject pageObj = pagesArray.getJSONObject(i);
                    if (null == pageObj) continue;

                    Page p = ApiUtils.parsePageObject(pageObj);
                    p.order = i;
                    result[i] = p;
                } catch (JSONException ex) {
                }
            }
            return result;
        }

        return null;
    }

    /**
     * Return all play-lists of a specific screen
     * @param subpageId
     * @return
     */
    public static Playlist[] getPlaylists(Context context, String subpageId) {
        final Date now = new Date();
        final TimeZone timeZone = TimeZone.getDefault();
        final int time = (int) (now.getTime() / 1000L),
                timezoneOffset = timeZone.getOffset(now.getTime()) / 1000;
        final String url = Constants.API_BASE_URL + String.format(Locale.US, "/app/pages/%s/%s/playlists?time=%d&timezoneOffset=%d&device=%s",
                Constants.API_APP_ID, subpageId, time, timezoneOffset, ApiUtils.getDeviceParam(context));

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpGet(context, url);
        if (null != dataObj) {
            JSONArray channelsArray = dataObj.optJSONArray("playlists");
            if (null == channelsArray || 0 == channelsArray.length()) return null;

            final int len = channelsArray.length();
            Playlist[] result = new Playlist[len];
            for (int i = 0; i < len; i++) {
                try {
                    result[i] = ApiUtils.parsePlaylistObject(channelsArray.getJSONObject(i));
                } catch (JSONException ex) {
                }
            }
            return result;
        }

        return null;
    }

}
package com.aol.videonews.api;

import android.content.Context;
import android.text.TextUtils;

import com.aol.videonews.Constants;
import com.aol.videonews.model.Playlist;

import org.json.JSONObject;

import java.util.Locale;

public final class PlaylistsApi {

    public static String getImageUrl(String playlistId, int width, int height) {
        return Constants.API_BASE_URL.replace("https://", "http://") + String.format(Locale.US, "/app/playlists/%s/%s/images/%sx%s.%s",
                Constants.API_APP_ID, playlistId, width, height, Constants.DEFAULT_IMAGE_FORMAT);
    }

    public static Playlist getPlaylisdtDetail(Context context, String playlistId) {
        if (TextUtils.isEmpty(playlistId) || null == context) return null;
        final String url = Constants.API_BASE_URL +
                String.format(Locale.US, "/app/playlists/%s/playlists_full/%s?device=%s", Constants.API_APP_ID, playlistId, ApiUtils.getDeviceParam(context));

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpGet(context, url);
        if (null != dataObj) {
            return ApiUtils.parsePlaylistObject(dataObj);
        }

        return null;
    }
}

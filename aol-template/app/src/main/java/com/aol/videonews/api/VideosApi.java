package com.aol.videonews.api;

import android.content.Context;
import android.text.TextUtils;

import com.aol.videonews.Constants;
import com.aol.videonews.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Operations about videos
 */

public final class VideosApi {

    public static String getImageUrl(String videoId, int width, int height) {
        return Constants.API_BASE_URL.replace("https://", "http://") + String.format(Locale.US, "/app/videos/%s/%s/images/%sx%s.%s",
                Constants.API_APP_ID, videoId, width, height, Constants.DEFAULT_IMAGE_FORMAT);
    }

//    public static String getRenditionUrl(String videoId, String videoQuality) {
//        if (videoQuality == null) {
//            videoQuality = Constants.DEFAULT_VIDEO_QUALITY;
//        }
//        return Constants.API_BASE_URL + String.format("/app/videos/%s/%s/renditions/%s.%s",
//                Constants.API_APP_ID, videoId, videoQuality, Constants.DEFAULT_VIDEO_FORMAT);
//    }

    public static String getPreviewUrl(String videoId, String videoQuality) {
        if (videoQuality == null) {
            videoQuality = Constants.DEFAULT_VIDEO_QUALITY;
        }
        return Constants.API_BASE_URL + String.format(Locale.US, "/app/videos/%s/%s/previews/%s.mp4", Constants.API_APP_ID, videoId, videoQuality);
    }

    public static Video getVideoDetail(Context context, String videoId, String videoContext) {
        if (TextUtils.isEmpty(videoId) || null == context) return null;
        final String url = Constants.API_BASE_URL +
                (TextUtils.isEmpty(videoContext) ?
                        String.format(Locale.US, "/app/videos/%s/%s/details?device=%s", Constants.API_APP_ID, videoId, ApiUtils.getDeviceParam(context)) :
                        String.format(Locale.US, "/app/videos/%s/%s/details?context=%s&device=%s", Constants.API_APP_ID, videoId, videoContext, ApiUtils.getDeviceParam(context)));

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpGet(context, url);
        if (null != dataObj) {
            return ApiUtils.parseVideoObject(dataObj);
        }

        return null;
    }

    // NOTE: not doing pagination right now, using limit=50 to get only one page of result with at most 50 items.

    public static Video[] search(Context context, String term, int offset, int limit) {
        if (TextUtils.isEmpty(term) || null == context) return null;

        String encodedTerm = term;
        try {
            encodedTerm = URLEncoder.encode(term, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }

        final String url = Constants.API_BASE_URL + String.format(Locale.US, "/app/videos/%s/search?term=%s&limit=50", Constants.API_APP_ID, encodedTerm) +
                (offset > 0 ? ("&offset="+offset) : "") +
                (limit > 0 ? ("&limit="+limit) : "");

        JSONObject dataObj = ApiUtils.fetchDataObjectWithHttpGet(context, url);
        if (null == dataObj) return null;

        JSONArray videosArray = dataObj.optJSONArray("videos");
        if (null == videosArray) return null;

        final int len = videosArray.length();
        if (len <= 0) return null;

        Video[] result = new Video[videosArray.length()];
        for (int i = 0; i < result.length; i++) {
            try {
                result[i] = ApiUtils.parseVideoObject(videosArray.getJSONObject(i));
            } catch (JSONException ex) {
            }
        }
        return result;
    }
}
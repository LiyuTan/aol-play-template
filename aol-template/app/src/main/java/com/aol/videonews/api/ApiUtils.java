package com.aol.videonews.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aol.videonews.Constants;
import com.aol.videonews.model.History;
import com.aol.videonews.model.Page;
import com.aol.videonews.model.Playlist;
import com.aol.videonews.model.PlaylistItem;
import com.aol.videonews.model.Rendition;
import com.aol.videonews.model.Show;
import com.aol.videonews.model.Subpage;
import com.aol.videonews.model.Subtitle;
import com.aol.videonews.model.SubtitleType;
import com.aol.videonews.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

final class ApiUtils {

    private static class CustomizedHeadersJsonObjectRequest extends JsonObjectRequest {
        private Context mContext;

        public CustomizedHeadersJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                                  Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Context context) {
            super(method, url, jsonRequest, listener, errorListener);
            mContext = context;
        }

        public CustomizedHeadersJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorListener, Context context) {
            super(url, jsonRequest, listener, errorListener);
            mContext = context;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return getAuthHeaders(super.getHeaders(), mContext);
        }
    }

    private static class CustomizedHeadersStringRequest extends StringRequest {
        private Context mContext;
        private boolean mAuthRequired;

        public CustomizedHeadersStringRequest(int method, String url, Response.Listener<String> listener,
                                              Response.ErrorListener errorListener, Context context, boolean authRequired) {
            super(method, url, listener, errorListener);
            mContext = context;
            mAuthRequired = authRequired;
        }

        public CustomizedHeadersStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Context context, boolean authRequired) {
            super(url, listener, errorListener);
            mContext = context;
            mAuthRequired = authRequired;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return mAuthRequired ? getAuthHeaders(super.getHeaders(), mContext) : super.getHeaders();
        }
    }

    private static RequestQueue mRequestQueue;
    private static String sAccessToken;

    private static RequestQueue getRequestQueue(Context context) {
        if (null == mRequestQueue && null != context) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    static JSONObject fetchDataObjectWithHttpGet(Context context, String url) {
        getRequestQueue(context);

        JSONObject responseObj;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        CustomizedHeadersJsonObjectRequest jsonRequest = new CustomizedHeadersJsonObjectRequest(url, null, future, future, context);
        mRequestQueue.add(jsonRequest);
        try {
            responseObj = future.get();
        } catch (Exception ex) {
            responseObj = null;
        }

        return getDataObjHelper(responseObj);
    }

    static String fetchResponseStringWithHttpDelete(Context context, String url, boolean authRequired) {
        getRequestQueue(context);

        RequestFuture<String> future = RequestFuture.newFuture();

        CustomizedHeadersStringRequest request = new CustomizedHeadersStringRequest(Request.Method.DELETE, url, future, future, context, authRequired);
        mRequestQueue.add(request);

        String responseStr = null;
        try {
            responseStr = future.get();
        } catch (Exception ex) {
        }
        return responseStr;
    }

    static String fetchResponseStringWithHttpPost(Context context, String url, final String bodyStr, boolean authRequired) {
        getRequestQueue(context);

        RequestFuture<String> future = RequestFuture.newFuture();

        CustomizedHeadersStringRequest request = new CustomizedHeadersStringRequest(Request.Method.POST, url, future, future, context, authRequired) {
            @Override
            public byte[] getBody() {
                byte[] body = null;
                try {
                    body = bodyStr.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException ex) {
                }
                return body;
            }
        };
        mRequestQueue.add(request);

        String responseStr = null;
        try {
            responseStr = future.get();
        } catch (Exception ex) {
        }
        return responseStr;
    }

    static JSONObject fetchDataObjectWithHttpPost(Context context, String url, final String bodyStr, boolean authRequired) {
        String responseStr = fetchResponseStringWithHttpPost(context, url, bodyStr, authRequired);

        try {
            if (!TextUtils.isEmpty(responseStr)) {
                return getDataObjHelper(new JSONObject(responseStr));
            }
        } catch (JSONException ex) {
        }

        return null;
    }

    private static JSONObject getDataObjHelper(JSONObject jsonObj) {
        if (null == jsonObj) return null;

        JSONObject responseObj = jsonObj.optJSONObject("response");
        if (null == responseObj) return null;

        if (200 != responseObj.optInt("statusCode")) return null;

        return responseObj.optJSONObject("data");
    }

    static final String getAccessToken(Context context) {
        if (TextUtils.isEmpty(sAccessToken) && null != context) {
            SharedPreferences prefs = context.getSharedPreferences(Constants.AOL_ON_PREFS_NAME, 0);
            sAccessToken = prefs.getString(Constants.AOL_ON_PREF_KEY_ACCESS_TOKEN, null);
        }
        return sAccessToken;
    }

    static final void setAccessToken(Context context, String accessToken, boolean isLoginToAccount) {
        if (null == context) return;

        sAccessToken = accessToken;

        SharedPreferences prefs = context.getSharedPreferences(Constants.AOL_ON_PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        if (!TextUtils.isEmpty(accessToken)) {
            editor.putString(Constants.AOL_ON_PREF_KEY_ACCESS_TOKEN, accessToken);
            editor.putBoolean(Constants.AOL_ON_PREF_KEY_IS_LOGIN_TO_ACCOUNT, isLoginToAccount);
        } else {
            editor.remove(Constants.AOL_ON_PREF_KEY_ACCESS_TOKEN);
            editor.remove(Constants.AOL_ON_PREF_KEY_IS_LOGIN_TO_ACCOUNT);
        }
        editor.commit();
    }

    static final Map<String, String> getAuthHeaders(Map<String, String> headers, Context context) {
        final String accessToken = getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            return headers;
        }
        HashMap<String, String> authHeaders = new HashMap<String, String>();
        if (null != headers) {
            authHeaders.putAll(headers);
        }
        authHeaders.put("Authorization", "Bearer " + accessToken);

        return authHeaders;
    }

    static Page parsePageObject(JSONObject jsonObject) {
        if (null == jsonObject) return null;

        Page page = new Page();
        JSONArray subpagesArray = jsonObject.optJSONArray("pages");

        if (null == subpagesArray || 0 == subpagesArray.length()) {
            page.hasSubpages = false;
        } else {
            page.hasSubpages = true;
            final int len = subpagesArray.length();
            page.subpages = new Subpage[len];
            for (int i = 0; i < len; i++) {
                try {
                    JSONObject subpageObj = subpagesArray.getJSONObject(i);
                    if (null == subpageObj) continue;

                    Subpage sp = new Subpage();
                    sp.id = subpageObj.optString("pageId");
                    sp.name = subpageObj.optString("pageName");
                    page.subpages[i] = sp;

                } catch (JSONException ex) {
                }
            }
        }

        page.id = jsonObject.optString("pageId");
        page.name = jsonObject.optString("pageName");

        return page;
    }

    static Playlist parsePlaylistObject(JSONObject jsonObject) {
        if (null == jsonObject) return null;

        PlaylistItem[] items = null;

        JSONArray itemsArray = jsonObject.optJSONArray("items");
        if (null != itemsArray) {
            final int len = itemsArray.length();
            if (len > 0) {
                items = new PlaylistItem[len];
                for (int i = 0; i < len; i++) {
                    try {
                        items[i] = parsePlaylistItemObject(itemsArray.getJSONObject(i));
                    } catch (JSONException ex) {
                    }
                }
            }
        }

        Playlist playlist = new Playlist();
        playlist.id = jsonObject.optString("id");
        playlist.name = jsonObject.optString("name");
        playlist.source = jsonObject.optString("source");
        playlist.type = jsonObject.optString("playlistType");
        playlist.seasonNumber = jsonObject.optInt("seasonNumber");
        playlist.items = items;

        return playlist;
    }

    static PlaylistItem parsePlaylistItemObject(JSONObject jsonObject) {
        if (null == jsonObject) return null;

        final String type = jsonObject.optString("type");
        if (TextUtils.equals(type, PlaylistItem.PLAYLIST_ITEM_TYPE_SHOW)) {
            JSONObject showJsonObject = jsonObject.optJSONObject("show");
            if (null != showJsonObject) {
                return parseShowObject(showJsonObject);
            }
        } else if (TextUtils.equals(type, PlaylistItem.PLAYLIST_ITEM_TYPE_VIDEO)) {
            JSONObject videoJsonObject = jsonObject.optJSONObject("video");
            if (null == videoJsonObject) {
                videoJsonObject = jsonObject.optJSONObject("abridgedVideo");
            }
            if (null != videoJsonObject) {
                Video videoObj = parseVideoObject(videoJsonObject);
                if (videoObj != null) {
                    videoObj.context = jsonObject.optString("context");
                    return videoObj;
                }
            }
        }
        return null;
    }

    static Show parseShowObject(JSONObject jsonObject) {
        if (null == jsonObject) return null;

        Show show = new Show();
        show.id = jsonObject.optString("id");
        show.title = jsonObject.optString("name", jsonObject.optString("title"));
        show.description = jsonObject.optString("description");
        show.year = jsonObject.optInt("year");

        JSONArray channelsArray = jsonObject.optJSONArray("playlists");
        Playlist[] playlists = null;
        if (null != channelsArray && channelsArray.length() > 0) {
            final int len = channelsArray.length();
            playlists = new Playlist[len];
            for (int i = 0; i < len; i++) {
                try {
                    playlists[i] = ApiUtils.parsePlaylistObject(channelsArray.getJSONObject(i));
                } catch (JSONException ex) {
                }
            }
        }
        show.playlists = playlists;

        return show;
    }

    static Video parseVideoObject(JSONObject jsonObject) {
        if (null == jsonObject) return null;

        Video video = new Video();

        video.id = jsonObject.optString("id");
        video.title = jsonObject.optString("title");
        video.videoType = jsonObject.optString("type");
        video.description = jsonObject.optString("description");
        video.duration = jsonObject.optInt("duration");
        video.episode = jsonObject.optInt("episode", Video.INVALID_EPISODE);
        video.season = jsonObject.optInt("season", Video.INVALID_SEASON);
        video.shareUrl = jsonObject.optString("shareUrl");
        video.videoMasterPlaylist = jsonObject.optString("videoMasterPlaylist");
        video.movieRating = jsonObject.optString("movieRating");
        video.language = jsonObject.optString("language");
        video.favorited = jsonObject.optBoolean("favorited");
        final JSONArray genresArray = jsonObject.optJSONArray("genres");
        if (genresArray != null) {
            video.genres = jsonArrayToStringHelper(genresArray);
        }
        final JSONArray actorsArray = jsonObject.optJSONArray("actors");
        if (actorsArray != null) {
            video.actors = jsonArrayToStringHelper(actorsArray);
        }
        final JSONArray directorsArray = jsonObject.optJSONArray("directors");
        if (directorsArray != null) {
            video.directors = jsonArrayToStringHelper(directorsArray);
        }

        JSONArray channelsArray = jsonObject.optJSONArray("playlists");
        Playlist[] playlists = null;
        if (null != channelsArray && channelsArray.length() > 0) {
            final int len = channelsArray.length();
            playlists = new Playlist[len];
            for (int i = 0; i < len; i++) {
                try {
                    playlists[i] = ApiUtils.parsePlaylistObject(channelsArray.getJSONObject(i));
                } catch (JSONException ex) {
                }
            }
        }
        video.playlists = playlists;

        JSONArray renditionsArray = jsonObject.optJSONArray("renditions");
        if (null != renditionsArray && renditionsArray.length() > 0) {
            int renditionsArrayLen = renditionsArray.length();
            video.renditions = new Rendition[renditionsArrayLen];
            for (int i = 0; i < renditionsArrayLen; i++) {
                Rendition rendition = new Rendition();
                try {
                    JSONObject renditionObj = renditionsArray.getJSONObject(i);
                    if (null == renditionObj) continue;
                    rendition.format = renditionObj.optString("format");
                    rendition.quality = renditionObj.optString("quality");
                    rendition.url = renditionObj.optString("url");
                } catch (JSONException ex) {
                    continue;
                }
                video.renditions[i] = rendition;
            }
        }

        JSONArray subtitlesArray = jsonObject.optJSONArray("subtitles");
        if (null != subtitlesArray && subtitlesArray.length() > 0) {
            int subtitlesArrayLen = subtitlesArray.length();
            video.subtitles = new Subtitle[subtitlesArrayLen];
            for (int i = 0; i < subtitlesArrayLen; i++) {
                try {
                    JSONObject subtitleObj = subtitlesArray.getJSONObject(i);
                    if (null == subtitleObj) continue;
                    Subtitle subtitle = new Subtitle();
                    subtitle.locale = subtitleObj.optString("locale");
                    JSONArray subtitlesTypesArray = subtitleObj.optJSONArray("types");
                    if (null != subtitlesTypesArray && subtitlesTypesArray.length() > 0) {
                        int subtitlesTypesArrayLen = subtitlesTypesArray.length();
                        subtitle.types = new SubtitleType[subtitlesTypesArrayLen];
                        for (int j = 0; j < subtitlesTypesArrayLen; j++) {
                            JSONObject subtitlesTypeObj = subtitlesTypesArray.getJSONObject(j);
                            if (null == subtitlesTypeObj) continue;

                            SubtitleType subtitleType = new SubtitleType();
                            subtitleType.type = subtitlesTypeObj.optString("type");
                            subtitleType.url = subtitlesTypeObj.optString("url");

                            subtitle.types[j] = subtitleType;
                        }
                    }
                    video.subtitles[i] = subtitle;
                } catch (JSONException ex) {
                }
            }
        }

        JSONObject historyObj = jsonObject.optJSONObject("history");
        if (historyObj != null) {
            History history = new History();
            history.offset = historyObj.optInt("offset");
            video.history = history;
        }

        return video;
    }

    // TODO find a better way to generate a more user friendly device name
    static final String generateDeviceName() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    static boolean isSucceeded(String responseStr) {
        if (null == responseStr) return false;

        try {
            JSONObject jsonObj = new JSONObject(responseStr);
            JSONObject responseObj = jsonObj.optJSONObject("response");
            if (null == responseObj) return false;

            if (200 != responseObj.optInt("statusCode")) return false;

            return true;

        } catch (JSONException ex) {
            return false;
        }
    }

    static String getDeviceParam(Context context) {
        if (null == context || null == context.getResources() || null == context.getResources().getConfiguration()) return null;

        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return "ANDROID_TABLET";
        } else {
            return "ANDROID_PHONE";
        }
    }

    private static String jsonArrayToStringHelper(JSONArray jsonArray) {
        StringBuilder sb = new StringBuilder();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    sb.append(jsonArray.getString(i));
                    if (i != len-1) {
                        sb.append(", ");
                    }
                } catch (JSONException ex) {
                }
            }
        }
        return sb.toString();
    }
}

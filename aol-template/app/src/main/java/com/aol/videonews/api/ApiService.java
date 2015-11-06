package com.aol.videonews.api;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aol.videonews.Constants;
import com.aol.videonews.model.Playlist;

public final class ApiService extends IntentService {

    public static final String ACTION_GET_PLAYLIST_DETAIL = "action_get_playlist_detail";

    private RequestQueue mRequestQueue;

    public ApiService() {
        super("ApiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (null == intent) return;
        final String action = intent.getAction();
        if (null == action) return;

        if (null == mRequestQueue) {
            mRequestQueue = Volley.newRequestQueue(this);
        }

        if (action.equals(ACTION_GET_PLAYLIST_DETAIL)) {
            final String playlistId = intent.getStringExtra(Constants.EXTRA_PLAYLIST_ID);
            Playlist playlist = PlaylistsApi.getPlaylisdtDetail(this, playlistId);

            Intent localBroadcast = new Intent(Constants.BROADCAST_GET_PLAYLIST_DETAIL_DONE);
            localBroadcast.putExtra(Constants.EXTRA_SUCCEEDED, playlist != null);
            localBroadcast.putExtra(Constants.EXTRA_PLAYLIST, playlist);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localBroadcast);
        }
    }

}
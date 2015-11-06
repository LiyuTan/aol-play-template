package com.aol.videonews;

import android.content.Context;
import android.content.Intent;

import com.aol.videonews.api.ApiService;

public class Utils {

    public static void getPlaylistDetail(Context context, String playlistId) {
        Intent intent = new Intent(context, ApiService.class);
        intent.setAction(ApiService.ACTION_GET_PLAYLIST_DETAIL);
        intent.putExtra(Constants.EXTRA_PLAYLIST_ID, playlistId);
        context.startService(intent);
    }

}

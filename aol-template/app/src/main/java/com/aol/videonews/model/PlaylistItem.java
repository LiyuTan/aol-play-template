package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class PlaylistItem implements Parcelable {

    // playlist item are either "VIDEO" or "SHOW"
    // for "VIDEO" type playlist item, it could be a video, episode or movie, you can check
    // Video object's videoType to get that info.

    public static final String PLAYLIST_ITEM_TYPE_VIDEO = "VIDEO";
    public static final String PLAYLIST_ITEM_TYPE_SHOW = "SHOW";

    public String id;
    public String itemType;
    public String title;
    public String context;
    public String shareUrl;

    public PlaylistItem() {
    }

    public PlaylistItem(Parcel in) {
        this.id = in.readString();
        this.itemType = in.readString();
        this.title = in.readString();
        this.context = in.readString();
        this.shareUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.itemType);
        dest.writeString(this.title);
        dest.writeString(this.context);
        dest.writeString(this.shareUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PlaylistItem> CREATOR
            = new Parcelable.Creator<PlaylistItem>() {

        @Override
        public PlaylistItem createFromParcel(Parcel in) {
            return new PlaylistItem(in);
        }

        @Override
        public PlaylistItem[] newArray(int size) {
            return new PlaylistItem[size];
        }
    };

    public boolean isVideo() {
        return TextUtils.equals(itemType, PLAYLIST_ITEM_TYPE_VIDEO);
    }

    public boolean isShow() {
        return TextUtils.equals(itemType, PLAYLIST_ITEM_TYPE_SHOW);
    }
}
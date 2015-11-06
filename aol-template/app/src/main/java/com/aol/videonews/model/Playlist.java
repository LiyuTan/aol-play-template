package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Arrays;

public final class Playlist implements Parcelable {

    public static final String PLAYLIST_TYPE_NORMAL = "NORMAL";
    public static final String PLAYLIST_TYPE_EPISODES = "EPISODES";
    public static final String PLAYLIST_TYPE_BONUS = "BONUS";
    public static final String PLAYLIST_TYPE_NEXTUP = "NEXTUP";
    public static final String PLAYLIST_TYPE_RELATED = "RELATED";
    public static final String PLAYLIST_TYPE_RECENT = "RECENT";

    public String id;
    public String name;
    public String source;
    public String type; // NORMAL, EPISODES, BONUS, RECENT
    public int seasonNumber;
    public PlaylistItem[] items;

    public Playlist() {
    }

    public Playlist(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.source = in.readString();
        this.type = in.readString();
        this.seasonNumber = in.readInt();

        Parcelable[] parcelableArray = in.readParcelableArray(PlaylistItem.class.getClassLoader());
        if (parcelableArray != null) {
            this.items = Arrays.copyOf(parcelableArray, parcelableArray.length, PlaylistItem[].class);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.source);
        dest.writeString(this.type);
        dest.writeInt(this.seasonNumber);
        dest.writeParcelableArray(this.items, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Playlist> CREATOR
            = new Parcelable.Creator<Playlist>() {

        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    public boolean isEpisodes() {
        return TextUtils.equals(PLAYLIST_TYPE_EPISODES, this.type);
    }

    public boolean isBonus() {
        return TextUtils.equals(PLAYLIST_TYPE_BONUS, this.type);
    }

    public boolean isNextUp() { return TextUtils.equals(PLAYLIST_TYPE_NEXTUP, this.type); }

    public boolean isRelated() { return TextUtils.equals(PLAYLIST_TYPE_RELATED, this.type); }

    public boolean isRecent() { return TextUtils.equals(PLAYLIST_TYPE_RECENT, this.type); }
}
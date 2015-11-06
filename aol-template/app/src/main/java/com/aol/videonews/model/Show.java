package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public final class Show extends PlaylistItem implements Parcelable {
    public Playlist[] playlists;

    public String description;
    public int year;

    public Show() {
        super();

        this.itemType = PlaylistItem.PLAYLIST_ITEM_TYPE_SHOW;
    }

    public Show(Parcel in) {
        super(in);

        this.description = in.readString();
        this.year = in.readInt();

        Parcelable[] parcelableArray = in.readParcelableArray(Playlist.class.getClassLoader());
        if (parcelableArray != null) {
            this.playlists = Arrays.copyOf(parcelableArray, parcelableArray.length, Playlist[].class);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeString(this.description);
        dest.writeInt(this.year);
        dest.writeParcelableArray(this.playlists, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Show> CREATOR
            = new Parcelable.Creator<Show>() {

        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };
}
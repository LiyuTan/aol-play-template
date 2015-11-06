package com.aol.videonews.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class Video extends PlaylistItem implements Parcelable {

    public static final int INVALID_EPISODE = -1;
    public static final int INVALID_SEASON = -1;

    public static final String VIDEO_TYPE_VIDEO = "VIDEO";
    public static final String VIDEO_TYPE_MOVIE = "MOVIE";
    public static final String VIDEO_TYPE_EPISODE = "EPISODE";

    public String description;
    public int duration;
    public int episode;
    public int season;
    public String videoType;
    public String videoMasterPlaylist;
    public String language;
    public String movieRating;
    public String genres;
    public String actors;
    public String directors;
    public boolean favorited;
    public String playlistId; // the id of the play list that this video is within
    public String pageId;  // the id of the page that this video is within

    public History history;

    public Rendition[] renditions;
    public Subtitle[] subtitles;
    public Playlist[] playlists;

    @IntDef({VIDEO_QUALITY_NULL, VIDEO_QUALITY_LD, VIDEO_QUALITY_SD, VIDEO_QUALITY_HD, VIDEO_QUALITY_FHD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VideoQuality {}

    public static final int VIDEO_QUALITY_NULL = 0;
    public static final int VIDEO_QUALITY_LD = 1;
    public static final int VIDEO_QUALITY_SD = 2;
    public static final int VIDEO_QUALITY_HD = 3;
    public static final int VIDEO_QUALITY_FHD = 4;

    public Video() {
        super();

        this.episode = INVALID_EPISODE;
        this.season = INVALID_SEASON;

        this.itemType = PlaylistItem.PLAYLIST_ITEM_TYPE_VIDEO;
    }

    public Video(Parcel in) {
        super(in);

        this.description = in.readString();
        this.duration = in.readInt();
        this.episode = in.readInt();
        this.season = in.readInt();
        this.videoType = in.readString();
        this.videoMasterPlaylist = in.readString();
        this.language = in.readString();
        this.movieRating = in.readString();
        this.genres = in.readString();
        this.actors = in.readString();
        this.directors = in.readString();
        this.favorited = in.readInt() > 0 ? true : false;
        this.playlistId = in.readString();
        this.pageId = in.readString();

        this.history = in.readParcelable(History.class.getClassLoader());

        Parcelable[] parcelableArray = in.readParcelableArray(Rendition.class.getClassLoader());
        if (parcelableArray != null) {
            this.renditions = Arrays.copyOf(parcelableArray, parcelableArray.length, Rendition[].class);
        }

        parcelableArray = in.readParcelableArray(Subtitle.class.getClassLoader());
        if (parcelableArray != null) {
            this.subtitles = Arrays.copyOf(parcelableArray, parcelableArray.length, Subtitle[].class);
        }

        parcelableArray = in.readParcelableArray(Playlist.class.getClassLoader());
        if (parcelableArray != null) {
            this.playlists = Arrays.copyOf(parcelableArray, parcelableArray.length, Playlist[].class);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeString(this.description);
        dest.writeInt(this.duration);
        dest.writeInt(this.episode);
        dest.writeInt(this.season);
        dest.writeString(this.videoType);
        dest.writeString(this.videoMasterPlaylist);
        dest.writeString(this.language);
        dest.writeString(this.movieRating);
        dest.writeString(this.genres);
        dest.writeString(this.actors);
        dest.writeString(this.directors);
        dest.writeInt(this.favorited ? 1 : 0);
        dest.writeString(this.playlistId);
        dest.writeString(this.pageId);
        dest.writeParcelable(this.history, flags);
        dest.writeParcelableArray(this.renditions, flags);
        dest.writeParcelableArray(this.subtitles, flags);
        dest.writeParcelableArray(this.playlists, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Video> CREATOR
            = new Parcelable.Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    /**
     * Return true if this video is an episode of a show.
     * A video who has valid "season" and "episode" property value is an episode.
     * @return
     */
    public boolean isEpisode() {
        return TextUtils.equals(VIDEO_TYPE_EPISODE, this.videoType);
    }

    public boolean isMovie() {
        return TextUtils.equals(VIDEO_TYPE_MOVIE, this.videoType);
    }

    public String getRenditionUrl(Context context) {
        if (this.renditions == null || this.renditions.length <= 0) return null;

        final boolean isConnectedToWifi;

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isConnectedToWifi = wifiInfo.isConnected();

        final @VideoQuality int maxVideoQuality;
        if (isConnectedToWifi) {
            maxVideoQuality = VIDEO_QUALITY_HD;
        } else {
            maxVideoQuality = VIDEO_QUALITY_SD;
        }

        String bestRenditionUrl = null;
        @VideoQuality int bestRenditionVideoQuality = VIDEO_QUALITY_NULL;
        for (Rendition rendition : this.renditions) {
            if (rendition != null && rendition.isMp4()) {
                @VideoQuality int currentRenditionVideoQuality = VIDEO_QUALITY_NULL;
                if (rendition.isFHD()) {
                    currentRenditionVideoQuality = VIDEO_QUALITY_FHD;
                } else if (rendition.isHD()) {
                    currentRenditionVideoQuality = VIDEO_QUALITY_HD;
                } else if (rendition.isSD()) {
                    currentRenditionVideoQuality = VIDEO_QUALITY_SD;
                } else if (rendition.isLD()) {
                    currentRenditionVideoQuality = VIDEO_QUALITY_LD;
                }
                if (currentRenditionVideoQuality <= maxVideoQuality &&
                        currentRenditionVideoQuality >= bestRenditionVideoQuality) {
                    bestRenditionVideoQuality = currentRenditionVideoQuality;
                    bestRenditionUrl = rendition.url;
                }
            }
        }
        return bestRenditionUrl;
    }

}
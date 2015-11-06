package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public final class Rendition implements Parcelable {

    public static final String LOW_DEFINITION = "LD";
    public static final String STANDARD_DEFINITION = "SD";
    public static final String HIGH_DEFINITION = "HD";
    public static final String FULL_HIGH_DEFINITION = "FHD";

    public String format;
    public String quality;
    public String url;

    public Rendition() {
    }

    public Rendition(Parcel in) {
        this.format = in.readString();
        this.quality = in.readString();
        this.url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.format);
        dest.writeString(this.quality);
        dest.writeString(this.url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Rendition> CREATOR
            = new Parcelable.Creator<Rendition>() {

        @Override
        public Rendition createFromParcel(Parcel in) {
            return new Rendition(in);
        }

        @Override
        public Rendition[] newArray(int size) {
            return new Rendition[size];
        }
    };

    public boolean isLD() {
        return TextUtils.equals(quality, LOW_DEFINITION);
    }

    public boolean isSD() {
        return TextUtils.equals(quality, STANDARD_DEFINITION);
    }

    public boolean isHD() {
        return TextUtils.equals(quality, HIGH_DEFINITION);
    }

    public boolean isFHD() {
        return TextUtils.equals(quality, FULL_HIGH_DEFINITION);
    }

    public boolean isMp4() {
        return TextUtils.equals(format, "mp4");
    }

    public boolean isWebM() {
        return TextUtils.equals(format, "webm");
    }
}

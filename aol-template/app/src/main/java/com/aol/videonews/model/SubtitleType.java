package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubtitleType implements Parcelable {

    public String type;
    public String url;

    public SubtitleType() {
    }

    public SubtitleType(Parcel in) {
        this.type = in.readString();
        this.url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SubtitleType> CREATOR
            = new Parcelable.Creator<SubtitleType>() {

        @Override
        public SubtitleType createFromParcel(Parcel in) {
            return new SubtitleType(in);
        }

        @Override
        public SubtitleType[] newArray(int size) {
            return new SubtitleType[size];
        }
    };
}

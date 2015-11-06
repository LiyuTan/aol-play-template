package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Subtitle implements Parcelable {

    public String locale;
    public SubtitleType[] types;

    public Subtitle() {
    }

    public Subtitle(Parcel in) {
        this.locale = in.readString();

        Parcelable[] parcelableArray = in.readParcelableArray(SubtitleType.class.getClassLoader());
        if (parcelableArray != null) {
            this.types = Arrays.copyOf(parcelableArray, parcelableArray.length, SubtitleType[].class);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locale);
        dest.writeParcelableArray(this.types, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Subtitle> CREATOR
            = new Parcelable.Creator<Subtitle>() {

        @Override
        public Subtitle createFromParcel(Parcel in) {
            return new Subtitle(in);
        }

        @Override
        public Subtitle[] newArray(int size) {
            return new Subtitle[size];
        }
    };
}

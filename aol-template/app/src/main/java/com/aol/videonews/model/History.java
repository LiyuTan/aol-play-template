package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class History implements Parcelable{

    // this is offset in seconds
    public int offset;

    public History() {
    }

    public History(Parcel in) {
        this.offset = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.offset);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<History> CREATOR
            = new Parcelable.Creator<History>() {

        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}
package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Subpage implements Parcelable {
    public String id;
    public String name;

    public Subpage() {
    }

    public Subpage(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Subpage> CREATOR
            = new Parcelable.Creator<Subpage>() {

        @Override
        public Subpage createFromParcel(Parcel in) {
            return new Subpage(in);
        }

        @Override
        public Subpage[] newArray(int size) {
            return new Subpage[size];
        }
    };
}

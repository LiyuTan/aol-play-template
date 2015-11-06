package com.aol.videonews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 *
 */
public final class Page implements Parcelable {
    public String id;
    public String name;
    public int order;
    public boolean hasSubpages;
    public Subpage[] subpages;

    public Page() {
    }

    public Page(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.order = in.readInt();
        this.hasSubpages = in.readByte() != 0;

        Parcelable[] parcelableArray = in.readParcelableArray(Subpage.class.getClassLoader());
        if (parcelableArray != null) {
            this.subpages = Arrays.copyOf(parcelableArray, parcelableArray.length, Subpage[].class);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.order);
        dest.writeByte((byte) (this.hasSubpages?1:0));
        dest.writeParcelableArray(this.subpages, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Page> CREATOR
            = new Parcelable.Creator<Page>() {

        @Override
        public Page createFromParcel(Parcel in) {
            return new Page(in);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };
}


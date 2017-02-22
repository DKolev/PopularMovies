package com.example.android.popularmovies.trailers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kolev on 11-Feb-17.
 */

public class Trailer implements Parcelable {

    private String key;
    private String name;

    public Trailer (String key, String name) {
        this.key = key;
        this.name = name;

    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
    }

    protected Trailer(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}

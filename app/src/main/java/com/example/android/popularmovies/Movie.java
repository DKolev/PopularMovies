package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This model class stores the details for each movie in one JSON object. Each variable corresponds
 * to the same JSON object in the JSON response when querying the API.
 */

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String release_date;
    private String poster_path;
    private String vote_average;
    private String overview;

    public Movie() {
    }

    public Movie(int id, String title, String release_date, String poster_path, String vote_average, String overview) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.overview = overview;
    }

    public int getMovieId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.release_date);
        dest.writeString(this.poster_path);
        dest.writeString(this.vote_average);
        dest.writeString(this.overview);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.release_date = in.readString();
        this.poster_path = in.readString();
        this.vote_average = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

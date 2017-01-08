package com.example.android.popularmovies;

/**
 * This model class stores the details for each movie in one JSON object. Each variable corresponds
 * to the same JSON object in the JSON response when querying the API.
 */

public class Movie {

    private String title;
    private String release_date;
    private String poster_path;
    private String vote_average;
    private String overview;

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
}

package com.example.android.popularmovies.movies;

/**
 * This class has only one JSON object "results" with child JSON array. This array can be stored in the Movie
 * model class.
 */

public class JSONResponseMovie {

    private Movie[] results;

    public Movie[] getMovie() {
        return results;
    }


}

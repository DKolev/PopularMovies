package com.example.android.popularmovies;

/**
 * This class has only one JSON object "results" with child JSON array. This array can be stored in the Movie
 * model class.
 */

public class JSONResponse {

    private Movie[] results;

    public Movie[] getMovie() {
        return results;
    }


}

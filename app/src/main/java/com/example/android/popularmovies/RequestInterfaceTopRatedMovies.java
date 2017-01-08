package com.example.android.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This interface defines the endpoint of the requested URL for the top rated movies.
 */

public interface RequestInterfaceTopRatedMovies {

    @GET("top_rated?api_key=d03767a891a06d9289296f6c08a79f81&language=en-US&page=1")
    Call<JSONResponse> getJSON();
}

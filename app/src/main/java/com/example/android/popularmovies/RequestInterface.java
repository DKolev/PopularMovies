package com.example.android.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * This interface defines the endpoint of the requested URL for the popular movies.
 */

public interface RequestInterface {

    @GET("popular?api_key=d03767a891a06d9289296f6c08a79f81&language=en-US&page=1")
    Call<JSONResponse> getJSONPopular();

    @GET("top_rated?api_key=d03767a891a06d9289296f6c08a79f81&language=en-US&page=1")
    Call<JSONResponse> getJSONTopRated();

    @GET
    Call<JSONResponseTrailer> getJSONTrailer(@Url String url);


}

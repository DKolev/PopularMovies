package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.movies.JSONResponseMovie;
import com.example.android.popularmovies.reviews.JSONResponseReview;
import com.example.android.popularmovies.trailers.JSONResponseTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * This interface defines the endpoint of the requested URL for the popular movies.
 */

public interface RequestInterface {

    @GET("popular?api_key=d03767a891a06d9289296f6c08a79f81&language=en-US&page=1")
    Call<JSONResponseMovie> getJSONPopular();

    @GET("top_rated?api_key=d03767a891a06d9289296f6c08a79f81&language=en-US&page=1")
    Call<JSONResponseMovie> getJSONTopRated();

    @GET
    Call<JSONResponseTrailer> getJSONTrailer(@Url String url);

    @GET
    Call<JSONResponseReview> getJSONReview(@Url String url);


}

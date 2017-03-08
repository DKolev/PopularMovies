package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kolev on 26-Dec-16.
 */

public class NetworkUtils {

    private final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private final static String VIDEOS = "videos";

    private final static String REVIEWS = "reviews";

    private final static String QUERY_POPULAR = "popular";

    private final static String QUERY_TOP_RATED = "top_rated";

    private final static String API_KEY = "api_key";

    private final static String ACTUAL_KEY = "";

    private final static String LANGUAGE = "language";

    private final static String ACTUAL_LANGUAGE = "en-US";

    private final static String PAGE = "page";

    private static int actual_page = 1;

    public static URL buildMovieTrailersUrlEndpoint () {
        Uri buildUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendEncodedPath(VIDEOS)
                .appendQueryParameter(API_KEY, ACTUAL_KEY)
                .appendQueryParameter(LANGUAGE, ACTUAL_LANGUAGE)
                .build();

        URL movieTrailersUrl = null;
        try {
            movieTrailersUrl = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return movieTrailersUrl;
    }

    public static URL buildMovieReviewsUrlEndpoint () {
        Uri buildUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY, ACTUAL_KEY)
                .appendQueryParameter(LANGUAGE, ACTUAL_LANGUAGE)
                .build();

        URL movieReviewsUrl = null;
        try {
            movieReviewsUrl = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return movieReviewsUrl;
    }

    public static URL buildPopularMoviesUrl() {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(QUERY_POPULAR)
                .appendQueryParameter(API_KEY, ACTUAL_KEY)
                .appendQueryParameter(LANGUAGE, ACTUAL_LANGUAGE)
                .appendQueryParameter(PAGE, Integer.toString(actual_page))
                .build();

        URL popularMoviesUrl = null;
        try {
            popularMoviesUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return popularMoviesUrl;

    }

    public static URL buildTopRatedMoviesUrl () {
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(QUERY_TOP_RATED)
                .appendQueryParameter(API_KEY, ACTUAL_KEY)
                .appendQueryParameter(LANGUAGE, ACTUAL_LANGUAGE)
                .appendQueryParameter(PAGE, Integer.toString(actual_page))
                .build();

        URL topRatedMoviesUrl = null;
        try {
            topRatedMoviesUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return topRatedMoviesUrl;
    }

}

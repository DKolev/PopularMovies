package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Kolev on 26-Dec-16.
 */

public class NetworkUtils {

    final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    final static String QUERY_POPULAR = "popular";

    final static String QUERY_TOP_RATED = "top_rated";

    final static String API_KEY = "api_key";

    final static String ACTUAL_KEY = "d03767a891a06d9289296f6c08a79f81";

    final static String LANGUAGE = "language";

    final static String ACTUAL_LANGUAGE = "en-US";

    final static String PAGE = "page";

    private static int actual_page = 1;

    ///TODO Make the page number variable to change ...
//    final static int PAGE_NUMBER = 1;

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

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

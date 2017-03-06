package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kolev on 25-Feb-17.
 */

public class FavMoviesContract {

    // Declaring the Content Authority, Base Uri and Path

    public static final String AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";



    public static final class FavMoviesEntry implements BaseColumns {

        // Base CONTENT_URI I use to query the Movies table from the content provider
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Declaring the table name and all the column names inside id
        public static final String TABLE_NAME = "favorite_movies";

        public static final String MOVIE_TITLE = "movie_title";

        public static final String MOVIE_RELEASE_DATE = "movie_release_date";

        public static final String MOVIE_POSTER_PATH = "movie_poster_path";

        public static final String MOVIE_VOTE_AVERAGE = "movie_vote_average";

        public static final String MOVIE_OVERVIEW = "movie_overview";

    }


}

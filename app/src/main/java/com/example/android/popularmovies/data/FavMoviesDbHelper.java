package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmovies.data.FavMoviesContract.FavMoviesEntry;

/**
 * Created by Kolev on 25-Feb-17.
 */

public class FavMoviesDbHelper extends SQLiteOpenHelper{

    // The name of the database
    private static final String DATABASE_NAME = "favorite_movies.db";

    // The version of the database
    private static final int DATABASE_VERSION = 1;

    public FavMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // The string containing the SQL statement for creating the table
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " +
                FavMoviesEntry.TABLE_NAME + " (" +
                FavMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavMoviesEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                FavMoviesEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                FavMoviesEntry.MOVIE_POSTER_PATH + " TEXT, " +
                FavMoviesEntry.MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavMoviesEntry.MOVIE_OVERVIEW  + " TEXT NOT NULL" +
                ");";

        // Executing the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + FavMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}

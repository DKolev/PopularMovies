package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.data.FavMoviesContract.FavMoviesEntry;

/**
 * Created by Kolev on 25-Feb-17.
 */

public class FavMoviesContentProvider extends ContentProvider {

    // Defining constants for the directory of movies and a single item
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    // The UriMatcher I''ll be using in this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * This method creates the UriMatcher that will match each URI to the MOVIES and
     * MOVIES_WITH_ID constants
     * @return A Uri matcher that correctly matches the constants for MOVIES and MOVIES_WITH_ID
     */
    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // directory
        uriMatcher.addURI(FavMoviesContract.AUTHORITY, FavMoviesContract.PATH_MOVIES, MOVIES);

        // single item
        uriMatcher.addURI(FavMoviesContract.AUTHORITY, FavMoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;

    }

    // An instance of FavMoviesDbHelper
    private FavMoviesDbHelper mFavMoviesDbHelper;

    @Override
    public boolean onCreate() {
        // Initialising the mFavMoviesDbHelper
        mFavMoviesDbHelper = new FavMoviesDbHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase database = mFavMoviesDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        // Query for the movies directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case MOVIES:
                returnCursor = database.query(FavMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            // Query for a single movie
            case MOVIES_WITH_ID:
                selection = FavMoviesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                returnCursor = database.query(FavMoviesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Set a notification URI on the Cursor and return that Cursor
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        // Get access to the task database (to write new data to)
        final SQLiteDatabase database = mFavMoviesDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                // Insert values into the table
                long id = database.insert(FavMoviesEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(FavMoviesEntry.CONTENT_URI, id);

                } else throw new android.database.SQLException("Failed to insert row into " + uri);

                break;

            // Set the value for the returnedUri and write the default case for unknown URI's
            default:
                // Default case throws an UnsupportedOperationException
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        // Getting access to the database
        final SQLiteDatabase database = mFavMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        // Keep track of the number of deleted movies
        int moviesDeleted;


        switch (match) {
            // Deleting a single row
            case MOVIES_WITH_ID:
                //Getting the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Filtering for this ID
                moviesDeleted = database.delete(FavMoviesEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            // Deleting all rows
            case MOVIES:
                moviesDeleted = database.delete(FavMoviesEntry.TABLE_NAME, null, null);
                break;
            default:
                // Default case throws an UnsupportedOperationException
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notifying the resolver of a change and returning the number of deleted item
        if (moviesDeleted != 0) {
            // A movie was deleted, set a notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returning the number of deleted movies
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

package com.example.android.popularmovies;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.FavMoviesContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kolev on 27-Feb-17.
 */

public class FavoriteMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.sort_option) TextView mSortOption;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    private static final int MOVIE_LOADER_ID = 0;
    private static final String TAG = FavoriteMoviesActivity.class.getSimpleName();

    private CustomFavMoviesCursorAdapter mAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSortOption.setText(R.string.my_favorite_movies);

        int orientation = this.getResources().getConfiguration().orientation;
        // Setting a GridLayout with 3 columns if the orientation is PORTRAIT
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Creating a new layoutManagerPortrait with GridLayout and 3 columns
            RecyclerView.LayoutManager layoutManagerPortrait = new GridLayoutManager(this,3);
            // Setting the layoutManagerPortrait to the RecyclerView
            mRecyclerView.setLayoutManager(layoutManagerPortrait);
        } else {
            // If the orientation is LANDSCAPE, I set a GridLayout with 5 columns
            RecyclerView.LayoutManager layoutManagerLandscape = new GridLayoutManager(this, 5);
            mRecyclerView.setLayoutManager(layoutManagerLandscape);
        }
        mAdapter = new CustomFavMoviesCursorAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFavMovies = null;

            @Override
            protected void onStartLoading() {
                if (mFavMovies != null) {
                    deliverResult(mFavMovies);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FavMoviesContract.FavMoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to async load data");
                    e.printStackTrace();
                    return null;
                }
            }


            public void deliverResult(Cursor data) {
                mFavMovies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_and_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemWasClicked = item.getItemId();

        switch (itemWasClicked) {
            case R.id.delete_movie:
                int rowsDeleted = getContentResolver().delete(FavMoviesContract.FavMoviesEntry.CONTENT_URI, null, null);
                Toast.makeText(this, rowsDeleted + " movies deleted from Favorites", Toast.LENGTH_LONG).show();
                mAdapter.swapCursor(cursor);
                return true;

            case R.id.share_movie:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

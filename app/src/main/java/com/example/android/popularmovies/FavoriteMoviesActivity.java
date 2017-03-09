package com.example.android.popularmovies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    @BindView(R.id.sort_option)
    TextView mSortOption;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

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

        // Getting the current orientation of the device
        int orientation = this.getResources().getConfiguration().orientation;
        // Setting a GridLayout with 3 columns if the orientation is PORTRAIT
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Creating a new layoutManagerPortrait with GridLayout and 3 columns
            RecyclerView.LayoutManager verticalLinearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            // Setting the layoutManagerPortrait to the RecyclerView
            mRecyclerView.setLayoutManager(verticalLinearLayout);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(mRecyclerView);
        } else {
            // If the orientation is LANDSCAPE, I set a GridLayout with 5 columns
            RecyclerView.LayoutManager gridLayout = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(gridLayout);
        }

        mAdapter = new CustomFavMoviesCursorAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();
                // Build appropriate uri with String row id appended
                String stringID = Integer.toString(id);
                Uri uri = FavMoviesContract.FavMoviesEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringID).build();

                // Delete a single row of data

                getContentResolver().delete(uri, null, null);
                Toast.makeText(FavoriteMoviesActivity.this, "Movie deleted from Favorites", Toast.LENGTH_SHORT).show();

                // Restart the loader to re-query for all tasks after a deletion
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, FavoriteMoviesActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        // Initializing the loader
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
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemWasClicked = item.getItemId();

        switch (itemWasClicked) {
            case R.id.delete_movies:
                showWarningDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showWarningDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warning_dialog_msg_all_movies);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(FavMoviesContract.FavMoviesEntry.CONTENT_URI, null, null);
                mAdapter.swapCursor(cursor);
                Toast.makeText(getBaseContext(), " All movies deleted from Favorites", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

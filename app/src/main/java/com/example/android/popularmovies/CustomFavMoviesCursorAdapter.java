package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.FavMoviesContract;
import com.example.android.popularmovies.movies.MovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kolev on 27-Feb-17.
 */

public class CustomFavMoviesCursorAdapter extends CursorRecyclerViewAdapter<CustomFavMoviesCursorAdapter.MovieViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private MovieAdapter.OnItemClickListener listener;

    public CustomFavMoviesCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(MovieAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, Cursor cursor) {

        int idIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry._ID);
        int titleIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.MOVIE_TITLE);
        int release_dateIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.MOVIE_RELEASE_DATE);
        int vote_averageIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.MOVIE_VOTE_AVERAGE);
        int overviewIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.MOVIE_OVERVIEW);

        final int id = cursor.getInt(idIndex);
        String movie_title = cursor.getString(titleIndex);
        String release_date = cursor.getString(release_dateIndex);
        String vote_average = cursor.getString(vote_averageIndex);
        String overview = cursor.getString(overviewIndex);

        viewHolder.itemView.setTag(id);
        viewHolder.title.setText(movie_title);
        viewHolder.release_date.setText(release_date);
        viewHolder.vote_average.setText(vote_average);
        viewHolder.plot_synopsis.setText(overview);

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.movie_title) TextView title;
        @BindView(R.id.movie_release_data) TextView release_date;
        @BindView(R.id.movie_vote_average) TextView vote_average;
        @BindView(R.id.movie_plot_synopsis) TextView plot_synopsis;
        @BindView(R.id.movie_poster) ImageView poster;

        public MovieViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Setting an OnClickListener on the poster image
            poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}

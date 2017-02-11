package com.example.android.popularmovies.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kolev on 28-Dec-16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // Defining some variables
    private Context context;
    private ArrayList<Movie> movie;
    private OnItemClickListener listener;

    private final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";

    private final static String POSTER_SIZE = "w185";

    private String posterURL;

    /**
     * Creates a Movie Adapter
     */
    public MovieAdapter(Context context, ArrayList<Movie> movie) {
        this.context = context;
        this.movie = movie;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created.
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType
     * @return A new MovieAdapterViewHolder that holds the Views for each grid item
     */
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_grid_item, viewGroup,
                shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     * @param viewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final MovieAdapter.ViewHolder viewHolder, int position) {

        viewHolder.title.setText(movie.get(position).getTitle());
        viewHolder.release_date.setText(movie.get(position).getRelease_date());
        posterURL = BASE_POSTER_URL + POSTER_SIZE + movie.get(position).getPoster_path();
        Picasso.with(context).load(posterURL).into(viewHolder.poster);
        viewHolder.vote_average.setText(movie.get(position).getVote_average());
        viewHolder.plot_synopsis.setText(movie.get(position).getOverview());

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     * @return The number of items available.
     */
    @Override
    public int getItemCount() {
        return movie.size();
    }

    /**
     * Cache of the children views for a movie grid item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_title) TextView title;
        @BindView(R.id.movie_release_data) TextView release_date;
        @BindView(R.id.movie_poster) ImageView poster;
        @BindView(R.id.movie_vote_average) TextView vote_average;
        @BindView(R.id.movie_plot_synopsis) TextView plot_synopsis;


        public ViewHolder(final View itemView) {
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

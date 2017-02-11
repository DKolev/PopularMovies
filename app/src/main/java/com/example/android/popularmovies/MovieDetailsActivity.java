package com.example.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.movies.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kolev on 02-Jan-17.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    // Defining some variables
    final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";

    final static String POSTER_SIZE = "w500";

    public static int MOVIE_ID;

    // Binding views with ButterKnife
    @BindView(R.id.movie_title) TextView mTitleTextView;
    @BindView(R.id.movie_release_data) TextView mReleaseDateTextView;
    @BindView(R.id.movie_poster) ImageView mPosterImageView;
    @BindView(R.id.movie_vote_average) TextView mVoteAverageTextView;
    @BindView(R.id.movie_plot_synopsis) TextView mMovieOverviewTextView;
    @BindView(R.id.poster_loading_indicator) ProgressBar mProgressBar;
    @BindView(R.id.error_loading_poster) TextView mErrorLoadingPoster;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);

        // Setting the Loading Indicator to VISIBLE
        mProgressBar.setVisibility(View.VISIBLE);

        // Getting the information about the movie from the MainActivity
        Movie movieDetails = getIntent().getParcelableExtra("movieDetails");

        // Getting the movie ID and setting it on the MOVIE_ID variable
        MOVIE_ID = (movieDetails.getMovieId());
        Log.v(String.valueOf(MovieDetailsActivity.this), "id is " + MOVIE_ID);

        // Setting the information into the corresponding views
        mTitleTextView.setText(movieDetails.getTitle());
        mReleaseDateTextView.setText(movieDetails.getRelease_date());
        // Adding a loading indicator while the image is loaded and an error message if there is a problem
        // while loading it
        Picasso.with(context).load(BASE_POSTER_URL + POSTER_SIZE + movieDetails.getPoster_path()).into(mPosterImageView, new Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mProgressBar.setVisibility(View.GONE);
                mErrorLoadingPoster.setVisibility(View.VISIBLE);
            }
        });
        mVoteAverageTextView.setText(movieDetails.getVote_average());
        mMovieOverviewTextView.setText(movieDetails.getOverview());


    }
}

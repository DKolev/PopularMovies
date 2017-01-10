package com.example.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
        Bundle extras = getIntent().getExtras();
        String movieTitle = extras.getString("MOVIE_TITLE");
        String movieReleaseDate = extras.getString("RELEASE_DATE");
        String moviePoster = extras.getString("POSTER");
        String movieVoteAverage = extras.getString("VOTE_AVERAGE");
        String movieOverview = extras.getString("OVERVIEW");

        // Setting the information into the corresponding views
        mTitleTextView.setText(movieTitle);
        mReleaseDateTextView.setText(movieReleaseDate);
        // Adding a loading indicator while the image is loaded and an error message if there is a problem
        // while loading it
        Picasso.with(context).load(BASE_POSTER_URL + POSTER_SIZE + moviePoster).into(mPosterImageView, new Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mErrorLoadingPoster.setVisibility(View.VISIBLE);
            }
        });
        mVoteAverageTextView.setText(movieVoteAverage);
        mMovieOverviewTextView.setText(movieOverview);


    }
}

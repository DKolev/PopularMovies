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

/**
 * Created by Kolev on 02-Jan-17.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    // Defining some variables
    final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";

    final static String POSTER_SIZE = "w500";

    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private ImageView mPosterImageView;
    private TextView mVoteAverageTextView;
    private TextView mMovieOverviewTextView;
    private ProgressBar mProgressBar;
    private TextView mErrorLoadingPoster;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        // Finding the corresponding views in the xml
        mTitleTextView = (TextView) findViewById(R.id.movie_title);
        mReleaseDateTextView = (TextView) findViewById(R.id.movie_release_data);
        mPosterImageView = (ImageView) findViewById(R.id.movie_poster);
        mVoteAverageTextView = (TextView) findViewById(R.id.movie_vote_average);
        mMovieOverviewTextView = (TextView) findViewById(R.id.movie_plot_synopsis);
        mProgressBar = (ProgressBar) findViewById(R.id.poster_loading_indicator);
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorLoadingPoster = (TextView) findViewById(R.id.error_loading_poster);


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

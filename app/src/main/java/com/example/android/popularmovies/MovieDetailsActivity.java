package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.movies.Movie;
import com.example.android.popularmovies.trailers.JSONResponseTrailer;
import com.example.android.popularmovies.trailers.Trailer;
import com.example.android.popularmovies.trailers.TrailerAdapter;
import com.example.android.popularmovies.utilities.RequestInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kolev on 02-Jan-17.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    // Defining some variables

    final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    final static String MOVIE_DB_TRAILER_ENDPOINT_NO_ID = "/videos?api_key=d03767a891a06d9289296f6c08a79f81&language=en-US";

    final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";

    final static String YOU_TUBE_URL_NO_KEY = "https://www.youtube.com/watch?v=";

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

    @BindView(R.id.movie_trailer_recycler_view) RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<Trailer> trailer;

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

        loadJsonTrailers();

        mTrailerRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerLinear = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManagerLinear);

    }

    private void loadJsonTrailers() {

        String baseUrl = MOVIE_DB_BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        String movieIdString = Integer.toString(MOVIE_ID);
        String trailersEndPoint =  movieIdString + MOVIE_DB_TRAILER_ENDPOINT_NO_ID;
        Call<JSONResponseTrailer> call = request.getJSONTrailer(trailersEndPoint);
        call.enqueue(new retrofit2.Callback<JSONResponseTrailer>() {
            @Override
            public void onResponse(Call<JSONResponseTrailer> call, Response<JSONResponseTrailer> response) {
                JSONResponseTrailer jsonResponseTrailer = response.body();
                trailer = new ArrayList<>(Arrays.asList(jsonResponseTrailer.getTrailer()));
                mTrailerAdapter = new TrailerAdapter(trailer);
                mTrailerAdapter.setOnItemClickListener(new TrailerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        String trailerKey = trailer.get(position).getKey();
                        Log.v(String.valueOf(MovieDetailsActivity.this), " key is " + trailerKey);
                        String youtubeUrl = YOU_TUBE_URL_NO_KEY + trailerKey;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(youtubeUrl));
                        startActivity(intent);
                    }
                });

                mTrailerRecyclerView.setAdapter(mTrailerAdapter);
            }

            @Override
            public void onFailure(Call<JSONResponseTrailer> call, Throwable t) {
                Log.e("Trailer info", "Error");
                t.printStackTrace();
            }
        });

    }
}

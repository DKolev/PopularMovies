package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Toast;

import com.example.android.popularmovies.data.FavMoviesContract;
import com.example.android.popularmovies.movies.Movie;
import com.example.android.popularmovies.reviews.JSONResponseReview;
import com.example.android.popularmovies.reviews.Review;
import com.example.android.popularmovies.reviews.ReviewAdapter;
import com.example.android.popularmovies.trailers.JSONResponseTrailer;
import com.example.android.popularmovies.trailers.Trailer;
import com.example.android.popularmovies.trailers.TrailerAdapter;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.RequestInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
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

    final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";

    final static String YOU_TUBE_URL_NO_KEY = "https://www.youtube.com/watch?v=";

    final static String POSTER_SIZE = "w500";

    public static String MOVIE_ID;

    // Binding views with ButterKnife
    @BindView(R.id.movie_title) TextView mTitleTextView;
    @BindView(R.id.movie_release_data) TextView mReleaseDateTextView;
    @BindView(R.id.movie_poster) ImageView mPosterImageView;
    @BindView(R.id.movie_vote_average) TextView mVoteAverageTextView;
    @BindView(R.id.movie_plot_synopsis) TextView mMovieOverviewTextView;
    @BindView(R.id.poster_loading_indicator) ProgressBar mProgressBar;
    @BindView(R.id.error_loading_poster) TextView mErrorLoadingPoster;
    @BindView(R.id.fav_star) ImageView mFavStarImageView;
    private Context context;

    @BindView(R.id.movie_trailer_recycler_view) RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<Trailer> trailerList;

    @BindView(R.id.movie_review_recycler_view) RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<Review> reviewList;

    @BindView(R.id.trailers_count) TextView mTrailersCount;
    @BindView(R.id.reviews_count) TextView mReviewsCount;

    static final String TRAILERS_KEY = "trailersKey";
    static final String REVIEWS_KEY = "reviewsKey";

    private Movie movieDetails;

    Uri movieUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_constraint);
        ButterKnife.bind(this);


        // Setting the Loading Indicator to VISIBLE
        mProgressBar.setVisibility(View.VISIBLE);

        // Getting the information about the movie from the MainActivity
        movieDetails = getIntent().getParcelableExtra("movieDetails");

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

        showTrailersAndReviews();

        mTrailerRecyclerView.setHasFixedSize(true);
        // Getting the current orientation of the device
        int orientation = this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView.LayoutManager layoutManagerLinearTrailers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mTrailerRecyclerView.setLayoutManager(layoutManagerLinearTrailers);
        } else {

            RecyclerView.LayoutManager layoutManagerLinearTrailers = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mTrailerRecyclerView.setLayoutManager(layoutManagerLinearTrailers);
        }

        mReviewRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerLinearReviews = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviewRecyclerView.setLayoutManager(layoutManagerLinearReviews);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRAILERS_KEY, trailerList);
        outState.putParcelableArrayList(REVIEWS_KEY, reviewList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        trailerList = savedInstanceState.getParcelableArrayList(TRAILERS_KEY);
        reviewList = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        showTrailersAndReviews();
        super.onResume();
    }

    private void loadJsonTrailers() {
        // Getting the trailers endpoint
        URL movieDbTrailerUrlEndpoint = NetworkUtils.buildMovieTrailersUrlEndpoint();
        // Converting it into a string
        String movieDbTrailerEndpointString = String.valueOf(movieDbTrailerUrlEndpoint);
        // Splitting the string to extract the base url
        String[] splittedUrl = movieDbTrailerEndpointString.split("videos");
        String baseUrl = splittedUrl[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        String trailersEndPoint =  MOVIE_ID + "/videos" + splittedUrl[1];
        Call<JSONResponseTrailer> call = request.getJSONTrailer(trailersEndPoint);
        call.enqueue(new retrofit2.Callback<JSONResponseTrailer>() {
            @Override
            public void onResponse(Call<JSONResponseTrailer> call, Response<JSONResponseTrailer> response) {
                JSONResponseTrailer jsonResponseTrailer = response.body();
                trailerList = new ArrayList<>(Arrays.asList(jsonResponseTrailer.getTrailer()));
                mTrailersCount.setText("(" + Integer.toString(trailerList.size()) + ")");
                mTrailerAdapter = new TrailerAdapter(trailerList);
                mTrailerAdapter.setOnItemClickListener(new TrailerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        String trailerKey = trailerList.get(position).getKey();
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

    private void loadJSONReviews () {
        // Getting the trailers endpoint
        URL movieDbReviewsUrlEndpoint = NetworkUtils.buildMovieReviewsUrlEndpoint();
        // Converting it into a string
        String movieDbReviewsEndpointString = String.valueOf(movieDbReviewsUrlEndpoint);
        // Splitting the string to extract the base url
        String[] splittedUrl = movieDbReviewsEndpointString.split("reviews");
        String baseUrl = splittedUrl[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        String reviewEndPoint = MOVIE_ID + "/reviews" + splittedUrl[1];
        Log.d(String.valueOf(MovieDetailsActivity.this), " endpoint is " + reviewEndPoint);
        Call<JSONResponseReview> call = request.getJSONReview(reviewEndPoint);
        call.enqueue(new retrofit2.Callback<JSONResponseReview>() {
            @Override
            public void onResponse(Call<JSONResponseReview> call, Response<JSONResponseReview> response) {
                JSONResponseReview jsonResponseReview = response.body();
                reviewList = new ArrayList<>(Arrays.asList(jsonResponseReview.getReview()));
                mReviewsCount.setText("(" + Integer.toString(reviewList.size()) + ")");
                mReviewAdapter = new ReviewAdapter(reviewList);
                mReviewAdapter.setOnItemClickListener(new TrailerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        String urlToFollow = reviewList.get(position).getUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urlToFollow));
                        startActivity(intent);
                    }
                });

                mReviewRecyclerView.setAdapter(mReviewAdapter);
            }

            @Override
            public void onFailure(Call<JSONResponseReview> call, Throwable t) {
                Log.e("Review info", "Error");
                t.printStackTrace();
            }
        });
    }

    public void showTrailersAndReviews() {
        if (trailerList != null && reviewList != null) {
            mTrailerAdapter = new TrailerAdapter(trailerList);
            mTrailersCount.setText("(" + Integer.toString(trailerList.size()) + ")");
            mTrailerAdapter.setOnItemClickListener(new TrailerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    String trailerKey = trailerList.get(position).getKey();
                    String youtubeUrl = YOU_TUBE_URL_NO_KEY + trailerKey;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(youtubeUrl));
                    startActivity(intent);
                }
            });
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);

            mReviewAdapter = new ReviewAdapter(reviewList);
            mReviewsCount.setText("(" + Integer.toString(reviewList.size()) + ")");
            mReviewAdapter.setOnItemClickListener(new TrailerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    String urlToFollow = reviewList.get(position).getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlToFollow));
                    startActivity(intent);
                }
            });

            mReviewRecyclerView.setAdapter(mReviewAdapter);

        } else {
            loadJsonTrailers();
            loadJSONReviews();
        }
    }

    public void addToFavorites(View view) {

        String title = (String) mTitleTextView.getText();
        String release_date = (String) mReleaseDateTextView.getText();
        String vote_average = (String) mVoteAverageTextView.getText();
        String overview = (String) mMovieOverviewTextView.getText();
        String poster_path = movieDetails.getPoster_path();


        ContentValues contentValues = new ContentValues();

        contentValues.put(FavMoviesContract.FavMoviesEntry.MOVIE_TITLE, title);
        contentValues.put(FavMoviesContract.FavMoviesEntry.MOVIE_ID, MOVIE_ID);
        contentValues.put(FavMoviesContract.FavMoviesEntry.MOVIE_POSTER_PATH, poster_path);
        contentValues.put(FavMoviesContract.FavMoviesEntry.MOVIE_RELEASE_DATE, release_date);
        contentValues.put(FavMoviesContract.FavMoviesEntry.MOVIE_VOTE_AVERAGE, vote_average);
        contentValues.put(FavMoviesContract.FavMoviesEntry.MOVIE_OVERVIEW, overview);

        movieUri= getContentResolver().insert(FavMoviesContract.FavMoviesEntry.CONTENT_URI, contentValues);

        if (movieUri != null) {
            Toast.makeText(this, "Movie successfully added to favorites", Toast.LENGTH_LONG).show();
            mFavStarImageView.setImageResource(R.drawable.ic_star_black_36dp);

        }
    }

}

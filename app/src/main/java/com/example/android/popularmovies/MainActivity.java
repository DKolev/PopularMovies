package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Defining some variables
    @BindView(R.id.error_message) TextView mErrorMessageTextView;
    @BindView(R.id.loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.sort_option) TextView mSortOption;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        // Getting a reference to the Sort option TextView in the Option menu
//        mSortOption = (TextView) findViewById(R.id.sort_option);
//        // Getting a reference to the Error message TextView
//        mErrorMessageTextView = (TextView) findViewById(R.id.error_message);
//        // Getting a reference to the Loading indicator ProgressBar
//        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
//        // Getting a reference to the RecyclerView from xml.
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Setting a fixed size for the child layout
        mRecyclerView.setHasFixedSize(true);
        // Getting the current orientation of the device
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
        // Trying to add some slide animations (without any success) :(
        mRecyclerView.setItemAnimator(new SlideInDownAnimator());

        // Getting a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Getting details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // If there is a network connection, load the movies. By default, the app will load the popular movies
        // on themoviedb.org and the order can be changed to see the top rated ones by clicking a button
        // in the options menu
        if (networkInfo != null && networkInfo.isConnected()) {
            showMovies();
        } else {
            // If there is no connection, show the error message
            showErrorMessage();
        }
    }


    /**
     * This method loads the popular movies from themoviedb.org
     */
    private void loadJSONPopularMovies() {
        // Setting the loading indicator to be visible
        mLoadingIndicator.setVisibility(View.VISIBLE);
        // Getting the popular movies URL
        URL popularMoviesUrl = NetworkUtils.buildPopularMoviesUrl();
        // Converting the URL into a string
        String popularMoviesString = String.valueOf(popularMoviesUrl);
        // Splitting the string so I can use the baseUrl in defining Retrofit
        String[] splittedUrl = popularMoviesString.split("popular");
        String baseUrl = splittedUrl[0];
        // Defining Retrofit
        // !!!IMPORTANT!!! This is the first time I'm using Retrofit so I had to rely on some additional info
        // from this great tutorial https://www.learn2crack.com/2016/02/recyclerview-json-parsing.html#
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // RequestInterfacePopularMovies object is created
        RequestInterfacePopularMovies request = retrofit.create(RequestInterfacePopularMovies.class);
        // Creating Call object from the RequestInterfacePopularMovies by calling getJSON() method
        Call<JSONResponse> call = request.getJSON();
        // Executing the Async request
        call.enqueue(new Callback<JSONResponse>() {

            // This is called if the request is successful
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                // Hiding the loading indicator
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                // Obtaining the JSONResponse object by calling body() method on the Response object
                JSONResponse jsonResponse = response.body();
                // From the JSON response object I get the Movie array object and convert it to ArrayList
                movie = new ArrayList<>(Arrays.asList(jsonResponse.getMovie()));
                Context context = getApplicationContext();
                // Creating a new MovieAdapter
                mAdapter = new MovieAdapter(context, movie);
                // Setting an OnItemClickListener so I can pass the info about the movie to the
                // MovieDetailsActivity
                mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                        // Packing everything into a bundle
                        Bundle extras = new Bundle();
                        String movieName = movie.get(position).getTitle();
                        String movieReleaseDate = movie.get(position).getRelease_date();
                        String moviePoster = movie.get(position).getPoster_path();
                        String movieVoteAverage = movie.get(position).getVote_average();
                        String movieOverview = movie.get(position).getOverview();
                        extras.putString("MOVIE_TITLE", movieName);
                        extras.putString("RELEASE_DATE", movieReleaseDate);
                        extras.putString("POSTER", moviePoster);
                        extras.putString("VOTE_AVERAGE", movieVoteAverage);
                        extras.putString("OVERVIEW", movieOverview);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
                // Setting the adapter on the RecyclerView
                mRecyclerView.setAdapter(mAdapter);

            }

            // This is called if the request is failed
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    /**
     * This method loads the top rated movies from themoviedb.org
     */
    private void loadJSONTopRatedMovies() {
        // Setting the loading indicator to be visible
        mLoadingIndicator.setVisibility(View.VISIBLE);
        // Getting the popular movies URL
        URL topRatedMoviesUrl = NetworkUtils.buildTopRatedMoviesUrl();
        // Converting the URL into a string
        String topRatedMoviesString = String.valueOf(topRatedMoviesUrl);
        // Splitting the string so I can use the baseUrl in defining Retrofit
        String [] splittedUrl = topRatedMoviesString.split("top_rated");
        String baseUrl = splittedUrl[0];
        // Defining Retrofit
        // !!!IMPORTANT!!! This is the first time I'm using Retrofit so I had to rely on some additional info
        // from this great tutorial https://www.learn2crack.com/2016/02/recyclerview-json-parsing.html#
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // RequestInterfaceTopRatedMovies object is created
        RequestInterfaceTopRatedMovies request = retrofit.create(RequestInterfaceTopRatedMovies.class);
        // Creating Call object from the RequestInterfaceTopRatedMovies by calling getJSON() method
        Call<JSONResponse> call = request.getJSON();
        // Executing the Async request
        call.enqueue(new Callback<JSONResponse>() {

            // This is called if the request is successful
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                // Hiding the loading indicator
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                // Obtaining the JSONResponse object by calling body() method on the Response object
                JSONResponse jsonResponse = response.body();
                // From the JSON response object I get the Movie array object and convert it to ArrayList
                movie = new ArrayList<>(Arrays.asList(jsonResponse.getMovie()));
                Context context = getApplicationContext();
                // Creating a new MovieAdapter
                mAdapter = new MovieAdapter(context, movie);
                // Setting an OnItemClickListener so I can pass the info about the movie to the
                // MovieDetailsActivity
                mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                        // Packing everything into a bundle
                        Bundle extras = new Bundle();
                        String movieName = movie.get(position).getTitle();
                        String movieReleaseDate = movie.get(position).getRelease_date();
                        String moviePoster = movie.get(position).getPoster_path();
                        String movieVoteAverage = movie.get(position).getVote_average();
                        String movieOverview = movie.get(position).getOverview();
                        extras.putString("MOVIE_TITLE", movieName);
                        extras.putString("RELEASE_DATE", movieReleaseDate);
                        extras.putString("POSTER", moviePoster);
                        extras.putString("VOTE_AVERAGE", movieVoteAverage);
                        extras.putString("OVERVIEW", movieOverview);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
                // Setting the adapter on the RecyclerView
                mRecyclerView.setAdapter(mAdapter);
            }

            // This is called if the request is failed
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }

        });
    }

    /**
     * This method loads the popular movies (by default) and sets the error message text view to be invisible
     */
    private void showMovies() {
        loadJSONPopularMovies();
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    /**
     * This method sets the error message to be visible and hides the sort option TextView and RecyclerView
     */
    private void showErrorMessage() {
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mSortOption.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemWasClicked = item.getItemId();
        if (itemWasClicked == R.id.sort_by_popularity) {
            loadJSONPopularMovies();
            mSortOption.setText(R.string.popular_movies_on_themoviedb_org);
            return true;
        } else if (itemWasClicked == R.id.sort_by_rating) {
            loadJSONTopRatedMovies();
            mSortOption.setText(R.string.top_rated_movies_on_the_moviedb_org);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

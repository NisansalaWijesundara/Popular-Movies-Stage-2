package com.example.android.popularmovies_stage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies_stage1.ImageAdapter.ImageAdapterOnClickHandler;

/*
* Class displays the popular & top rated movie posters.
* */

public class MainActivity extends AppCompatActivity implements ImageAdapterOnClickHandler {


    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String movie_image__url = "http://api.themoviedb.org/3/discover/movie?api_key=0090c4fcfc45046c3af17ef9b93c4b6d";
    private static final String topRated_movies = "http://api.themoviedb.org/3/movie/top_rated?api_key=0090c4fcfc45046c3af17ef9b93c4b6d";
    private static final String popular_movies = "http://api.themoviedb.org/3/movie/popular?api_key=0090c4fcfc45046c3af17ef9b93c4b6d";
    private static List<Image> mMovieList;
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;

    private ProgressBar mLoadingIndicator;

    private TextView emptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.recyclerview_movies);


        mImageAdapter = new ImageAdapter(this, this);
        mRecyclerView.setAdapter(mImageAdapter);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        emptyStateTextView = findViewById(R.id.empty_state);


        loadMovieDetails(movie_image__url);


    }

    private void loadMovieDetails(String movie_image__url) {
        new MovieData().execute(movie_image__url);

    }


    @Override
    public void onClick(Image thisMovie) {

        Context context = this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        Bundle bundle = new Bundle();

        bundle.putString("POSTER_NAME", thisMovie.getTitle());
        bundle.putString("POSTER", thisMovie.getImage());
        bundle.putString("RELEASE_DATE", thisMovie.getReleaseDate());
        bundle.putString("RATE", thisMovie.getVoteAvg());
        bundle.putString("DESCRIPTION", thisMovie.getSynopsis());

        intentToStartDetailActivity.putExtras(bundle);
        startActivity(intentToStartDetailActivity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            switch (item.getItemId()) {
                case R.id.popular:
                    loadMovieDetails(popular_movies);
                    return true;


                case R.id.topRated:
                    loadMovieDetails(topRated_movies);
                    return true;


                default:

            }

        } else {
            View loadingIndicator = findViewById(R.id.pb_loading_indicator);
            loadingIndicator.setVisibility(View.GONE);


            emptyStateTextView.setText(R.string.no_internet_connection);
        }
        return super.onOptionsItemSelected(item);
    }

    class MovieData extends AsyncTask<String, Void, List<Image>> {

        public MovieData() {
            mMovieList = new ArrayList<Image>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Image> doInBackground(String... url) {

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


            if (mMovieList == null) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                return null;
            }

            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    String posterURL = url[0];
                    return NetworkUtils.fetchMovieData(posterURL);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            } else {
                View loadingIndicator = findViewById(R.id.pb_loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                emptyStateTextView.setText(R.string.no_internet_connection);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Image> images) {

            if (images != null) {
                mImageAdapter.setImageURLs(images);
                mRecyclerView.setAdapter(mImageAdapter);
                mImageAdapter.notifyDataSetChanged();
                mLoadingIndicator.setVisibility(View.GONE);

            } else {
                return;
            }
        }
    }

}












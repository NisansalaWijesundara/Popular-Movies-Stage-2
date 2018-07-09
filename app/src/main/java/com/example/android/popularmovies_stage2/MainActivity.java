package com.example.android.popularmovies_stage2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies_stage2.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies_stage2.ImageAdapter.ImageAdapterOnClickHandler;

/*
* Class displays the popular & top rated movie posters.
* */

public class MainActivity extends AppCompatActivity implements ImageAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Image>> {


    private static final String API_KEY = "";
    private static final String movie_image__url = "http://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY;
    private static final String topRated_movies = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    private static final String popular_movies = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    private static final int MOVIES_LOADER_ID = 0;
    @BindView(R.id.recyclerview_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.empty_state)
    TextView emptyStateTextView;
    private List<Image> mMovieList;
    private Parcelable mMovieState;
    private String MOVIE_STATE_KEY;
    private ImageAdapter mImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private TextView mMovieName;
    private Image movieImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mImageAdapter = new ImageAdapter(this, this);
        mRecyclerView.setAdapter(mImageAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        int loaderId = MOVIES_LOADER_ID;
        LoaderManager.LoaderCallbacks<List<Image>> callback = MainActivity.this;
        Bundle bundleForLoader = new Bundle();
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        loadMovieDetails(movie_image__url);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_STATE_KEY, mMovieState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mMovieState = savedInstanceState.getParcelable(MOVIE_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMovieState != null) {
            gridLayoutManager.onRestoreInstanceState(mMovieState);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void loadMovieDetails(String movie_image_url) {
        new MovieData().execute(movie_image_url);
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

        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        } else {
            View loadingIndicator = findViewById(R.id.pb_loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Image>> onCreateLoader(int id, final Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> data) {
    }

    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {
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
                String posterURL = url[0];
                return NetworkUtils.fetchMovieData(posterURL);
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












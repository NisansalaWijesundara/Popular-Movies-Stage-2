package com.example.android.popularmovies_stage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Image>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int MOVIE_LOADER_ID = 0;
    private final String movie_image__url = "https://api.themoviedb.org/3/movie/550?api_key=0090c4fcfc45046c3af17ef9b93c4b6d";
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private List<Image> movieList;

    /**
     * public MainActivity(RecyclerView mRecyclerView, TextView mErrorMessageDisplay, ProgressBar mLoadingIndicator, ImageAdapter.MovieAdapterOnClickHandler handler, ArrayList<Image> movieList) {
     * this.mRecyclerView = mRecyclerView;
     * this.mErrorMessageDisplay = mErrorMessageDisplay;
     * this.mLoadingIndicator = mLoadingIndicator;
     * this.movieList = movieList;
     * }
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ///mImageAdapter = new ImageAdapter(getApplicationContext(), R.layout.movie_posters, movieList, handler);
        mImageAdapter = new ImageAdapter(this, (ImageAdapter.MovieAdapterOnClickHandler) new ArrayList<Image>());


        mRecyclerView.setAdapter(mImageAdapter);

        int loaderId = MOVIE_LOADER_ID;


        LoaderManager.LoaderCallbacks<List<Image>> callback = MainActivity.this;


        Bundle bundleForLoader = null;


        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, (android.support.v4.app.LoaderManager.LoaderCallbacks<Object>) callback);
    }


    //@SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Image>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Image>>(this) {

            List<Image> mMovieList = null;

            @Override
            protected void onStartLoading() {
                if (mMovieList != null) {
                    deliverResult(mMovieList);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override//error
            public List<Image> loadInBackground() {


                List<Image> images = new ArrayList<Image>();
                if (images == null || TextUtils.isEmpty((CharSequence) images)) {
                    return null;
                }
                try {
                    String posterURL = new String(movie_image__url);
                    return NetworkUtils.fetchMovieData(posterURL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> data) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mImageAdapter.setImageURLs(data);


    }

    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {

    }

    @Override
    public void onClick(Image thisMovie) {

    }
}





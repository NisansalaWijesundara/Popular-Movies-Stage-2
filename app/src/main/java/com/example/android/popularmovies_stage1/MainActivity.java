package com.example.android.popularmovies_stage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies_stage1.ImageAdapter.ImageAdapterOnClickHandler;

public class MainActivity extends AppCompatActivity implements ImageAdapterOnClickHandler {
//implements ImageAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Image>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int MOVIE_LOADER_ID = 0;
    private static final String movie_image__url = "";
    private static final String topRated_movies = "";
    private static final String popular_movies = "";
    private static List<Image> mMovieList;
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private MovieLoader mMovieLoader;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private TextView movie_name;

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


        //mMovieLoader = new MovieLoader(getApplicationContext(),movie_image__url);


        mRecyclerView = findViewById(R.id.recyclerview_movies);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        //mImageAdapter = new ImageAdapter(this,R.layout.movie_posters, movieList,this);
        //mImageAdapter = new ImageAdapter(this, (ImageAdapter.ImageAdapterOnClickHandler) new ArrayList<Image>());

        mImageAdapter = new ImageAdapter(this, this);
        mRecyclerView.setAdapter(mImageAdapter);


        //int loaderId = MOVIE_LOADER_ID;


        // LoaderManager.LoaderCallbacks<List<Image>> callback = MainActivity.this;


        // Bundle bundleForLoader = null;

        //calling error
        //getSupportLoaderManager().initLoader(loaderId, bundleForLoader,callback).forceLoad();

        Log.v("MainActivity", "onCreateFinish");

        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        Log.v("MainActivity", "onCreateFinish2");

        loadMovieDetails(movie_image__url);
    }

    private void loadMovieDetails(String movie_image__url) {
        new MovieData().execute(movie_image__url);
        Log.v("MainActivity", "loadMovieDetails");
    }

    //@Override
    //public void onClick(Image thisMovie) {

    //}


/*
    public AsyncTaskLoader<List<Image>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Image>>(this) {

     private List<Image>mMovieList = null;


        @Override
            protected void onStartLoading() {
               if (mMovieList != null) {
                    deliverResult(mMovieList);
                   Log.v("AsyncTaskLoader","onStartLoading");

                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            };


            @Override
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
                Log.v("AsyncTaskLoader","loadInBackground");

                return null;

            }

            public void deliverResult(List<Image>data) {
                mMovieList = data;
                super.deliverResult(data);
            }


        };*/

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
        Log.v("MainActivity", "onClick");
    }


     /*   @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> data) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mImageAdapter.setImageURLs(data);


    }

    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {

    }*/

    /* @Override
     public Loader<List<Image>> onCreateLoader(int id, Bundle args) {
         return new FetchMovieData(this);
     }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.popular:
                loadMovieDetails(popular_movies);
                return true;


            case R.id.topRated:
                loadMovieDetails(topRated_movies);
                return true;


            default:

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
        }

        @Override
        protected List<Image> doInBackground(String... url) {
            //List<Image> images = new ArrayList<Image>();
            if (mMovieList == null) {
                return null;
            }
            try {
                Log.v("AsyncTaskLoader", "loadInBackground");
                String posterURL = url[0];
                return NetworkUtils.fetchMovieData(posterURL);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("AsyncTaskLoader", "loadInBackground2");
                return null;
            }


        }

        @Override
        protected void onPostExecute(List<Image> images) {
            if (images != null) {
                mImageAdapter.setImageURLs(images);
                Log.v("AsyncTaskLoader", "onPostExecute1");
                mRecyclerView.setAdapter(mImageAdapter);
                Log.v("AsyncTaskLoader", "onPostExecute2");
                //mImageAdapter.notifyDataSetChanged();
                //mLoadingIndicator.setVisibility(View.GONE);
                Log.v("AsyncTaskLoader", "onPostExecute3");
            } else {
                return;
            }
        }
    }
}












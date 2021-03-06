package com.example.android.popularmovies_stage2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.CursorLoader;
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
import android.widget.Toast;

import com.example.android.popularmovies_stage2.data.MovieContract;
import com.example.android.popularmovies_stage2.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies_stage2.ImageAdapter.ImageAdapterOnClickHandler;

/*
* Class displays the popular & top rated movie posters.
* */

public class MainActivity extends AppCompatActivity implements ImageAdapterOnClickHandler,android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {


    public static final String API_KEY = "";
    private static final String movie_image__url = "http://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY;
    private static final String topRated_movies = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    private static final String popular_movies = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    private static final int MOVIES_LOADER_ID = 0;
    private static final String TAG = MainActivity.class.getName();
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
    private ImageAdapter dbImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private TextView mMovieName;
    private Image movieImage;
    private SQLiteDatabase mDb;
    private Boolean dbMovies;
    private String CASE = "Popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mImageAdapter = new ImageAdapter(this, this);
        dbImageAdapter = new ImageAdapter(this, this);
        mRecyclerView.setAdapter(mImageAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        loadMovieDetails(movie_image__url);

        if(savedInstanceState!=null) {
            if (savedInstanceState.getString("CASE") == "Popular") {
                loadMovieDetails(popular_movies);
                dbMovies = false;
            } else if (savedInstanceState.getString("CASE") == "Top") {
                loadMovieDetails(topRated_movies);
                dbMovies = false;
            } else if (savedInstanceState.getString("CASE") == "Favorite") {
                dbMovies = true;

            }
        }
        else {
            loadMovieDetails(movie_image__url);
            dbMovies = false;
        }

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
       /* if (mMovieState != null) {
           gridLayoutManager.onRestoreInstanceState(mMovieState);
       }
        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID,null,this);*/

        if (CASE == "Popular") {
            dbMovies = false;
            return;
        } else if (CASE == "Top") {
            dbMovies = false;
            return;
        } else if (CASE == "Favorite") {
            dbMovies = true;
            return;
        }

        else {
            dbMovies = false;
            return;
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
                    dbMovies = false;
                    loadMovieDetails(popular_movies);
                    return true;

                case R.id.topRated:
                    dbMovies = false;
                    loadMovieDetails(topRated_movies);
                    return true;

                case R.id.favourite:
                    dbMovies = true;
                    getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null,  this);
                    return true;

                default:
                    dbMovies = false;
                    loadMovieDetails(popular_movies);
                    return true;
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
    public void onClick(Image thisMovie) {
       if(dbMovies == false) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = connMgr.getActiveNetworkInfo();

            if (nInfo == null || !nInfo.isConnected()) {
                Toast.makeText(this, "Please check your connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Context context = this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        Bundle bundle = new Bundle();
        bundle.putBoolean("LOAD_FROM_DATABASE", dbMovies);
      if(dbMovies==false) {
            bundle.putString("ID", thisMovie.getId());
            bundle.putString("POSTER", thisMovie.getImage());
       }
        if(dbMovies==true){
            bundle.putByteArray("POSTER", thisMovie.getmoviePosterDb());
        }

        bundle.putString("ID" ,thisMovie.getId());
        bundle.putString("POSTER_NAME", thisMovie.getTitle());
        //bundle.putString("POSTER", thisMovie.getImage());
        bundle.putString("RELEASE_DATE", thisMovie.getReleaseDate());
        bundle.putString("RATE", thisMovie.getVoteAvg());
        bundle.putString("DESCRIPTION", thisMovie.getSynopsis());
        bundle.putString("REVIEW_AUTHOR", thisMovie.getReviewAuthor());
        bundle.putString("REVIEW_CONTENT", thisMovie.getReviewContent());
        bundle.putString("REVIEW_URL", thisMovie.getReviewLink());
        bundle.putString("YOUTUBE_URL", thisMovie.getTrailerLink());

        intentToStartDetailActivity.putExtras(bundle);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

      String projection[] = {MovieContract.MoviesEntry._ID,
              MovieContract.MoviesEntry.COLUMN_MOVIE_ID,
              MovieContract.MoviesEntry.COLUMN_TITLE,
              MovieContract.MoviesEntry.COLUMN_PORTER_URI,
              MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
              MovieContract.MoviesEntry.COLUMN_TRAILER_LINK,
              MovieContract.MoviesEntry.COLUMN_RELEASE_DATE,
              MovieContract.MoviesEntry.COLUMN_OVERVIEW,
              MovieContract.MoviesEntry.COLUMN_CONTENT,
              MovieContract.MoviesEntry.COLUMN_AUTHOR,
              MovieContract.MoviesEntry.COLUMN_REVIEW_LINK};

return new CursorLoader(this,MovieContract.MoviesEntry.CONTENT_URI,
        projection,
        null,
        null,
        null);

    }





    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Image> moviesFromDB = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                int movieNameColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TITLE);
                int moviePosterColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_PORTER_URI);
                int movieRatingColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE);
                int movieTrailerColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TRAILER_LINK);
                int movieReleaseDateColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE);
                int movieSynopsisColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_OVERVIEW);
                int movieReviewContentColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_CONTENT);
                int movieReviewAuthorColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_AUTHOR);
                int movieReviewLinkColumnIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_REVIEW_LINK);

                Image movie = new Image(
                        cursor.getString(movieNameColumnIndex),
                        cursor.getString(movieReleaseDateColumnIndex),
                        cursor.getString(movieRatingColumnIndex),
                        cursor.getString(movieSynopsisColumnIndex),
                        cursor.getBlob(moviePosterColumnIndex),
                        cursor.getString(movieReviewAuthorColumnIndex),
                        cursor.getString(movieReviewContentColumnIndex),
                        cursor.getString(movieReviewLinkColumnIndex),
                        cursor.getString(movieTrailerColumnIndex)
                );
                moviesFromDB.add(movie);
                cursor.moveToNext();
            }
            setupDbView(moviesFromDB);
        }

    }

    private void setupDbView(ArrayList<Image> moviesFromDB) {
        if(moviesFromDB==null){
            mLoadingIndicator.setVisibility(View.VISIBLE);
                return;
        }
        dbImageAdapter.setImageBitmaps(moviesFromDB);
        mLoadingIndicator.setVisibility(View.GONE);
        mRecyclerView.setAdapter(dbImageAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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

        /*    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (mMovieList == null) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                return null;
            }
            if (networkInfo != null && networkInfo.isConnected()) {
                String posterURL = url[0];
                return NetworkUtils.fetchMovieData(posterURL,API_KEY);
            } else {
                View loadingIndicator = findViewById(R.id.pb_loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                emptyStateTextView.setText(R.string.no_internet_connection);
            }
            return null;*/
            if (url.length == 0) {
                return null;
            }

            String URL = url[0];

            try {
                return NetworkUtils.fetchMovieData(URL, API_KEY);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
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












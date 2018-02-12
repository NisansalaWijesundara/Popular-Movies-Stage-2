package com.example.android.popularmovies_stage1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Nisansala on 1/8/2018.
 */

public class MovieLoader extends AsyncTaskLoader<List<Image>> {


    private static final String LOG_TAG = MovieLoader.class.getName();


    private String mUrl = "http://api.themoviedb.org/3/movie/popular?api_key=0090c4fcfc45046c3af17ef9b93c4b6d";


    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.v("MovieLoader", "MovieLoader");
    }


    @Override
    public List<Image> loadInBackground() {
        if (mUrl == null) {
            Log.v("MovieLoader", "loadInBackground1");
            return null;

        }


        List<Image> movies = null;
        try {
            movies = NetworkUtils.fetchMovieData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("MovieLoader", "loadInBackground2");
        return movies;
    }

}

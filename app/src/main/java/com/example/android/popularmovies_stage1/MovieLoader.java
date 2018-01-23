package com.example.android.popularmovies_stage1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Nisansala on 1/8/2018.
 */

public class MovieLoader extends AsyncTaskLoader<List<Image>> {


    private static final String LOG_TAG = MovieLoader.class.getName();


    private String mUrl;


    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Image> loadInBackground() {
        if (mUrl == null) {
            return null;
        }


        List<Image> movies = null;
        try {
            movies = NetworkUtils.fetchMovieData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

}

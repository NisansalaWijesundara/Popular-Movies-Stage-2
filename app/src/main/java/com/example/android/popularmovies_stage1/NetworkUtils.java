package com.example.android.popularmovies_stage1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisansala on 12/26/2017.
 *
 * Class helps to perform the HTTP request and parse response.
 */

public final class NetworkUtils {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();


    private NetworkUtils() {
    }

    /**
     * Return a list of {@link Image} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<Image> extractFeatureFromJson(String movieJSON) {


        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }


        List<Image> images = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentMovie = resultsArray.getJSONObject(i);

                Image movie = new Image();
                Log.v("NetworkUtils", "extractFeatureFromJson4.3");
                movie.setTitle(currentMovie.getString("title"));
                movie.setReleaseDate(currentMovie.getString("release_date"));
                movie.setVoteAvg(currentMovie.getString("vote_average"));
                movie.setSynopsis(currentMovie.getString("overview"));
                movie.setImage("http://image.tmdb.org/t/p/w342/" + currentMovie.getString("poster_path"));


                images.add(movie);

            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return images;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }

        return jsonResponse;

    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Image> fetchMovieData(String requestUrl) throws JSONException {


        URL url = createUrl(requestUrl);


        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        List<Image> images = extractFeatureFromJson(jsonResponse);


        return images;
    }
}

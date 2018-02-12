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
 */

public final class NetworkUtils {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link NetworkUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private NetworkUtils() {
    }

    /**
     * Return a list of {@link Image} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<Image> extractFeatureFromJson(String movieJSON) {
        // If the JSON string is empty or null, then return early.

        Log.v("NetworkUtils", "extractFeatureFromJson");
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        // Create an empty ArrayList to start adding images to
        List<Image> images = new ArrayList<>();
        Log.v("NetworkUtils", "extractFeatureFromJson2");
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.


        try {
            Log.v("NetworkUtils", "extractFeatureFromJson3");
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
            Log.v("NetworkUtils", "extractFeatureFromJson4.1");
            for (int i = 0; i < resultsArray.length(); i++) {
                Log.v("NetworkUtils", "extractFeatureFromJson4.2");
                JSONObject currentMovie = resultsArray.getJSONObject(i);

                Image movie = new Image();
                Log.v("NetworkUtils", "extractFeatureFromJson4.3");
                movie.setTitle(currentMovie.getString("title"));
                movie.setReleaseDate(currentMovie.getString("release_date"));
                movie.setVoteAvg(currentMovie.getString("vote_average"));
                movie.setSynopsis(currentMovie.getString("overview"));
                movie.setImage("http://image.tmdb.org/t/p/w300/" + currentMovie.getString("backdrop_path"));
                Log.v("NetworkUtils", "extractFeatureFromJson4.4");

                images.add(movie);
                Log.v("NetworkUtils", "extractFeatureFromJson4.5");
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        Log.v("NetworkUtils", "extractFeatureFromJson5");
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
        Log.v("NetworkUtils", "makeHttpRequest");
        // If the URL is null, then return early.
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
        Log.v("NetworkUtils", "makeHttpRequest2");
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

        Log.v("NetworkUtils", "fetchMovieData");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.v("NetworkUtils", "fetchMovieData2");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of images
        List<Image> images = extractFeatureFromJson(jsonResponse);
        Log.v("NetworkUtils", "fetchMovieData3");

        return images;
    }
}

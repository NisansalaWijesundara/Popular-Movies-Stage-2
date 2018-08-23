package com.example.android.popularmovies_stage2.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies_stage2.MainActivity;
import com.example.android.popularmovies_stage2.Review;

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
 * Created by Nisansala on 7/25/2018.
 */

public class NetworkReviewUtils {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();


    public NetworkReviewUtils() {
        throw new AssertionError("No instances");
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


    private static List<Review> extractReviewsFromJson(String movieJSON, Boolean isReview) {
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }
        List<Review> reviews = new ArrayList<>();
        if (isReview == true) {
            try {
                JSONObject baseJsonResponse = new JSONObject(movieJSON);
                JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
                for (int i = 1; i < resultsArray.length(); i++) {
                    JSONObject currentReview = resultsArray.getJSONObject(i);
                    reviews.add(new Review(
                            currentReview.getString("url"),
                            currentReview.getString("author"),
                            currentReview.getString("content")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return reviews;
    }

    public static List<Review> networkReqForReviews(String id, String apiKey) throws JSONException {
        Log.e(LOG_TAG, "onNetworkReq");
        String urlForReviews = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + apiKey;
        URL URL = createUrl(urlForReviews);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(URL);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Review> reviewList = extractReviewsFromJson(jsonResponse, true);
        return reviewList;
    }
}


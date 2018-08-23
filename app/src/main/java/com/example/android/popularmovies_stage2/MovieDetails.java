package com.example.android.popularmovies_stage2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies_stage2.util.NetworkReviewUtils;
import com.example.android.popularmovies_stage2.util.NetworkTrailerUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies_stage2.util.NetworkReviewUtils.LOG_TAG;

/**
 * Created by Nisansala on 12/28/2017.
 *
 * Displays the details of the clicked movie poster.
 */

public class MovieDetails extends AppCompatActivity {

    ArrayList<Trailer> mTrailers;
    TrailerAdapter trailersAdapter;
    ReviewAdapter reviewsAdapter;
    private ImageView mMoviePoster;
    private TextView mMovieName;
    private TextView mMovieReleaseDate;
    private TextView mMovieRate;
    private TextView mMovieDescription;
    private ImageButton movie_trailer_button;
    private ListView mTrailersListView;
    private ListView mReviewsListView;
    private TextView movie_review_content;
    private TextView movie_review_author;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);



        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        mMovieName = findViewById(R.id.movie_title);
        mMovieName.setText(bundle.getString("POSTER_NAME"));


        mMoviePoster = findViewById(R.id.movie_image);
        Picasso.with(this).load(bundle.getString("POSTER")).fit().centerCrop().into(mMoviePoster);

        mMovieReleaseDate = findViewById(R.id.release_date);
        mMovieReleaseDate.setText("Release Date  : " + bundle.getString("RELEASE_DATE"));

        mMovieRate = findViewById(R.id.vote_average);
        mMovieRate.setText("Vote Average  : " + bundle.getString("RATE"));

        mMovieDescription = findViewById(R.id.movie_description);
        mMovieDescription.setText(bundle.getString("DESCRIPTION"));
        mTrailersListView = findViewById(R.id.trailer_list);
        trailersAdapter = new TrailerAdapter(MovieDetails.this, new ArrayList<Trailer>());
        TrailerClass trailerTask = new TrailerClass();
        trailerTask.execute(bundle.getString("ID"));
        movie_trailer_button = findViewById(R.id.trailer_play);
        movie_trailer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = bundle.getString("YOUTUBE_URL");
                if (id != null && id != "") {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(id));
                    startActivity(webIntent);

                } else {
                    Toast.makeText(MovieDetails.this, "No trailer found!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        reviewsAdapter = new ReviewAdapter(MovieDetails.this, new ArrayList<Review>());
        ReviewClass reviewTask = new ReviewClass();
        reviewTask.execute(bundle.getString("ID"));
        mReviewsListView = findViewById(R.id.reviews_list);
        movie_review_content = findViewById(R.id.review_content);
        String review_URL = bundle.getString("REVIEW_URL");
        if (review_URL == null) {
            movie_review_content.setText("No reviews found.");
        } else {
            movie_review_content.setText("'" + bundle.getString("REVIEW_CONTENT") + "'");
        }
        movie_review_author = findViewById(R.id.review_author);
        movie_review_author.setText(bundle.getString("REVIEW_AUTHOR"));
        mReviewsListView = findViewById(R.id.reviews_list);
        mReviewsListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mReviewsListView.setSelection(i);
                String review_URL = bundle.getString("REVIEW_URL");
                if (review_URL != null && review_URL != "") {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(review_URL));
                    startActivity(webIntent);

                } else {
                    Toast.makeText(MovieDetails.this, "No review found!", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    public class ReviewClass extends AsyncTask<String, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String id = strings[0];
            try {
                return NetworkReviewUtils.networkReqForReviews(id, MainActivity.API_KEY);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Null returned");
                e.printStackTrace();
                return null;
            }
        }
    }

    public class TrailerClass extends AsyncTask<String, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String id = strings[0];
            try {
                return NetworkTrailerUtils.networkReqForTrailers(id, MainActivity.API_KEY);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Null returned");
                e.printStackTrace();
                return null;
            }
        }
    }
}


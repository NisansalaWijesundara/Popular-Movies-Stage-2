package com.example.android.popularmovies_stage2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies_stage2.data.MovieContract;
import com.example.android.popularmovies_stage2.util.NetworkReviewUtils;
import com.example.android.popularmovies_stage2.util.NetworkTrailerUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
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
    private FloatingActionButton favoriteMovieButton;
    private byte[] movie_poster;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();

        final Bundle bundle = intent.getExtras();
        Boolean loadFromDB = bundle.getBoolean("LOAD_FROM_DATABASE");
        mMovieName = findViewById(R.id.movie_title);
        mMovieName.setText(bundle.getString("POSTER_NAME"));
        mMoviePoster = findViewById(R.id.movie_image);
        //Picasso.with(this).load(bundle.getString("POSTER")).fit().centerCrop().into(mMoviePoster);
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
       /* favoriteMovieButton = findViewById(R.id.favourite_fab);
        favoriteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMovie(bundle);
                // finish();
            }
        });
    }*/
      if(loadFromDB==false) {
            Picasso.with(this).load(bundle.getString("POSTER")).fit().centerCrop().into(mMoviePoster);
            favoriteMovieButton = findViewById(R.id.favourite_fab);
            favoriteMovieButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveMovie(bundle);
                }
            });
            ReviewClass review = new ReviewClass();
            review.execute(bundle.getString("ID"));
            TrailerClass trailer = new TrailerClass();
            trailer.execute(bundle.getString("ID"));
        }
        if(loadFromDB==true){
            byte[] bitmapData = bundle.getByteArray("POSTER");
            if(bitmapData!=null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                mMoviePoster.setImageBitmap(bitmap);
            }

        }
    }


    private void saveMovie(Bundle bundle) {
        String movie_id = bundle.getString("ID");
        String movieName = bundle.getString("POSTER_NAME");
        Picasso.with(this).load(bundle.getString("POSTER")).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                movie_poster = stream.toByteArray();


            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        String movieRating = bundle.getString("RATE");
        String movieTrailer = bundle.getString("YOUTUBE_URL");
        String movieReleaseDate = bundle.getString("RELEASE_DATE");
        String movieSynopsis = bundle.getString("DESCRIPTION");
        String movieReviewContent = bundle.getString("REVIEW_CONTENT");
        String movieReviewAuthor = bundle.getString("REVIEW_AUTHOR");
        String movieReviewLink = bundle.getString("REVIEW_URL");
        ContentValues values = new ContentValues();
        values.put(MovieContract.MoviesEntry.COLUMN_MOVIE_ID, movie_id);
        values.put(MovieContract.MoviesEntry.COLUMN_TITLE, movieName);
        values.put(MovieContract.MoviesEntry.COLUMN_PORTER_URI, movie_poster);
        values.put(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movieRating);
        values.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, movieReleaseDate);
        values.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, movieSynopsis);
        values.put(MovieContract.MoviesEntry.COLUMN_TRAILER_LINK, movieTrailer);
        values.put(MovieContract.MoviesEntry.COLUMN_REVIEW_LINK, movieReviewLink);
        values.put(MovieContract.MoviesEntry.COLUMN_AUTHOR, movieReviewAuthor);
        values.put(MovieContract.MoviesEntry.COLUMN_CONTENT, movieReviewContent);
     /*   values.put(MovieContract.TrailersEntry.COLUMN_TRAILER_LINK, movieTrailer);
        values.put(MovieContract.ReviewsEntry.COLUMN_CONTENT, movieReviewContent);
        values.put(MovieContract.ReviewsEntry.COLUMN_AUTHOR, movieReviewAuthor);
        values.put(MovieContract.ReviewsEntry.COLUMN_REVIEW_LINK, movieReviewLink);*/
        Uri newUri = getContentResolver().insert(MovieContract.MoviesEntry.CONTENT_URI, values);
       // Uri trailerUri = getContentResolver().insert(MovieContract.TrailersEntry.CONTENT_URI, values);
      //  Uri reviewUri = getContentResolver().insert(MovieContract.ReviewsEntry.CONTENT_URI, values);
      //  if (newUri == null && trailerUri == null && reviewUri == null) {
        if (newUri == null){
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "Insertion failed. Already added.", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Inserted item", Toast.LENGTH_SHORT).show();
        }
        //finish();
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


package com.example.android.popularmovies_stage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Nisansala on 12/28/2017.
 *
 * Displays the details of the clicked movie poster.
 */

public class MovieDetails extends AppCompatActivity {

    private ImageView mMoviePoster;
    private TextView mMovieName;
    private TextView mMovieReleaseDate;
    private TextView mMovieRate;
    private TextView mMovieDescription;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

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

    }

}

package com.example.android.popularmovies_stage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nisansala on 7/11/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "favorite_movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MovieContract.MoviesEntry.TABLE_NAME
                + " (" + MovieContract.MoviesEntry._ID + " TEXT PRIMARY KEY, "
                + MovieContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, "
                + MovieContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.MoviesEntry.COLUMN_PORTER_URI + " TEXT NOT NULL "
                + " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE "
                + MovieContract.TrailersEntry.TABLE_NAME
                + " (" + MovieContract.TrailersEntry._ID + " TEXT PRIMARY KEY, "
                + MovieContract.TrailersEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieContract.TrailersEntry.COLUMN_KEY + " TEXT NOT NULL, "
                + MovieContract.TrailersEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + MovieContract.TrailersEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MoviesEntry.TABLE_NAME + " (" + MovieContract.MoviesEntry._ID + ") ON DELETE CASCADE"
                + " );";
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE "
                + MovieContract.ReviewsEntry.TABLE_NAME
                + " (" + MovieContract.ReviewsEntry._ID + " TEXT PRIMARY KEY, "
                + MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieContract.ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + MovieContract.ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MoviesEntry.TABLE_NAME + " (" + MovieContract.MoviesEntry._ID + ") ON DELETE CASCADE"
                + " );";
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.TABLE_NAME + MovieContract.TrailersEntry.TABLE_NAME + MovieContract.ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

package com.example.android.popularmovies_stage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.popularmovies_stage2.data.MovieContract.MoviesEntry.TABLE_NAME;

/**
 * Created by Nisansala on 7/18/2018.
 */

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    public static final int TRAILERS = 200;
    public static final int TRAILERS_WITH_ID = 201;
    public static final int REVIEWS = 300;
    public static final int REVIEWS_WITH_ID = 301;
    private static UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;
    public static final String LOG_TAG = MovieContentProvider.class.getSimpleName();
    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "#", MOVIES_WITH_ID);
      /*  uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_TRAILERS, TRAILERS);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_TRAILERS + "#", TRAILERS_WITH_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_REVIEWS, REVIEWS);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_REVIEWS + "#", REVIEWS_WITH_ID);*/
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query( Uri uri,  String[] projection, String selection,  String[] selectionArgs,  String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match){

            case MOVIES:
               returnCursor = db.query(TABLE_NAME,
                       projection,
                       selection,
                       selectionArgs,
                       null,
                       null,
                       sortOrder);
               break;
            case MOVIES_WITH_ID:
                selection = MovieContract.MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                returnCursor = db.query(MovieContract.MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
break;
        /*    case TRAILERS:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case REVIEWS:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;*/
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                return MovieContract.MoviesEntry.CONTENT_TYPE;
            case MOVIES_WITH_ID:
                return MovieContract.MoviesEntry.CONTENT_TYPE;
          /*  case TRAILERS:
                return MovieContract.TrailersEntry.CONTENT_TYPE;
            case TRAILERS_WITH_ID:
                return MovieContract.TrailersEntry.CONTENT_TYPE;
            case REVIEWS:
                return MovieContract.ReviewsEntry.CONTENT_TYPE;
            case REVIEWS_WITH_ID:
                return MovieContract.ReviewsEntry.CONTENT_TYPE;*/

            default:
                throw new IllegalStateException("Unknown URI "+uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
    // final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
       // Uri returnUri;
        //long id = db.insert(MovieContract.MoviesEntry.TABLE_NAME,null,contentValues);
      //  long id2 = db.insert(MovieContract.TrailersEntry.TABLE_NAME,null,contentValues);
       // long id3 = db.insert(MovieContract.ReviewsEntry.TABLE_NAME,null,contentValues);

                switch (match){
                    case MOVIES:
                        return insertMovie(uri,contentValues);
                    default:
                        throw new IllegalArgumentException("Insertion not supported for: "+ uri);
                }
               // if (id>0){
                   // returnUri = ContentUris.withAppendedId(MovieContract.MoviesEntry.CONTENT_URI,id);
               // }
                //else {
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
               // }
               // break;
           /* case TRAILERS:
                if (id>0){
                    returnUri =ContentUris.withAppendedId(MovieContract.TrailersEntry.CONTENT_URI,id2);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case REVIEWS:
                if (id>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.ReviewsEntry.CONTENT_URI,id3);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;*/
                //default:
                    //throw new UnsupportedOperationException("Unknown uri:" + uri);
       // }


    // getContext().getContentResolver().notifyChange(uri,null);
//return ContentUris.withAppendedId(returnUri,id + id2 + id3);
        //return ContentUris.withAppendedId(returnUri,id);
                //return uri;
    }
    private Uri insertMovie(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        long id = database.insert(MovieContract.MoviesEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

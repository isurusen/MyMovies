package com.isuru.mymovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.isuru.mymovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isuru Senanayake on 17/03/2019.
 *
 * -- Adaptor class of the SQLite database
 * -- Contains all the insert, delete, select operations of the database
 *
 */

public class DBAdapter {

    private DBHelper dbHelper = null;
    private final Context context = null;
    private SQLiteDatabase db = null;
    public static final String DBLOG = "dblog";

    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Get a new instance of the database for reading and writing.
     */
    public void open() {

        try {
            db = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException e) {
            Log.i(DBLOG, e.toString());
        }
    }

    /**
     * Close the database instance.
     */
    public void close() {
        if (db != null)
            db.close();
    }

    /**
     * Insert the selected favorite movie.
     */
    public void addFavorite(Movie movie){

        ContentValues values = new ContentValues();
        values.put(DBConstant.COLUMN_MOVIE_ID, movie.getId());
        values.put(DBConstant.COLUMN_TITLE, movie.getTitle());
        values.put(DBConstant.COLUMN_RATING, movie.getRating());
        values.put(DBConstant.COLUMN_IMG_URL, movie.getThumbImgUrl());
        values.put(DBConstant.COLUMN_DESC, movie.getDescription());
        values.put(DBConstant.COLUMN_LANGUAGE, movie.getLanguage());
        synchronized (DBAdapter.class) {
            db.insert(DBConstant.TABLE_NAME_FAVORITE, null, values);
        }
        close(); // Closing the database connection
    }

    /**
     * Select all items of the Favorite table.
     */
    public List<Movie> getAllFavoriteMovies() {
        List<Movie> allFavMovies = new ArrayList<Movie>();
        Cursor cur = null;
        synchronized (DBAdapter.class) {
            cur = db.query(DBConstant.TABLE_NAME_FAVORITE, null, null, null, null, null, null);
        }
        if (cur != null) {
            cur.moveToFirst();
            if (cur.getCount() > 0) {
                do {
                    Movie favMovie = new Movie();
                    favMovie.setId(cur.getString(0));
                    favMovie.setTitle(cur.getString(1));
                    favMovie.setRating(cur.getString(2));
                    favMovie.setThumbImgUrl(cur.getString(3));
                    favMovie.setDescription(cur.getString(4));
                    favMovie.setLanguage(cur.getString(5));
                    allFavMovies.add(favMovie);
                } while (cur.moveToNext());
            }
            cur.close();
        }
        return allFavMovies;
    }

    /**
     * Select movie item by movie ID.
     */
    public Movie getFavoriteMovieId(String movieId) {
        Movie favMovie = new Movie();
        Cursor cur = null;
        synchronized (DBAdapter.class) {
            // Ensure the string is properly escaped
            cur = db.query(DBConstant.TABLE_NAME_FAVORITE, null, DBConstant.COLUMN_MOVIE_ID + " = ?", new String[] { movieId }, null, null, null);
        }
        if (cur != null) {
            cur.moveToFirst();
            if (cur.getCount() > 0) {
                favMovie.setId(cur.getString(0));
                favMovie.setTitle(cur.getString(1));
                favMovie.setRating(cur.getString(2));
                favMovie.setThumbImgUrl(cur.getString(3));
                favMovie.setDescription(cur.getString(4));
                favMovie.setLanguage(cur.getString(5));
            }
            cur.close();
        }
        return favMovie;
    }

    /**
     * Delete selected favorite movie item
     */
    public void deleteFavorite(String movieId) {
        synchronized (DBAdapter.class) {
            db.delete(DBConstant.TABLE_NAME_FAVORITE, DBConstant.COLUMN_MOVIE_ID + "='" + movieId + "'", null);
        }
    }

}

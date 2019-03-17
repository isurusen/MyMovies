package com.isuru.mymovies.data;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 *
 * -- All the constants related to database
 */

public class DBConstant{

    public static final String TABLE_NAME_FAVORITE = "favorite";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_IMG_URL = "url";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_LANGUAGE = "language";

    /**
     * Create table query of the Favorite table.
     */
    public static final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_NAME_FAVORITE + " (" +
            COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_RATING + " REAL NOT NULL, " +
            COLUMN_IMG_URL + " TEXT NOT NULL, " +
            COLUMN_DESC + " TEXT NOT NULL, " +
            COLUMN_LANGUAGE + " TEXT NOT NULL" +
            "); ";
}

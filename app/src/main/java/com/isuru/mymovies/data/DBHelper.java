package com.isuru.mymovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Isuru Senanayake on 16/03/2019.
 * -- Helper class to create and control the database
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mymovies.db";    //Name of the local database
    private static final int DB_VERSION = 1;    //Version of the database, need to increase the version to apply changes of the database structure

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase fav_db;
    public static final String DBLOG = "dblog";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);  //Creating the database file
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstant.CREATE_FAVORITE_TABLE);   //Creating the FAVORITE table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        fav_db.execSQL("DROP TABLE IF EXISTS " + DBConstant.CREATE_FAVORITE_TABLE); //Drop all the tables and create a fresh database when increase the version
        onCreate(db);
    }

    /**
     * Opening the database connection.
     */
    public void open(){
        fav_db = dbHelper.getWritableDatabase();
        Log.i(DBLOG, "Database Opened");
    }

    /**
     * Closiing the database connection.
     */
    public void close(){
        dbHelper.close();
        Log.i(DBLOG, "Database Closed");
    }
}

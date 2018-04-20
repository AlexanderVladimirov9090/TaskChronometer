package com.gmail.alexander.taskchronometer.databasetools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 *         <alexandervladimirov1902@gmail.com>
 *         This is basic database class for the application.
 *         AppProvider will be the one who use this class.
 */

 public class AppDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TasksTimer.db";
    public static final int DATABASE_VERSION = 1;
    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get instance of the database helper.
     *
     * @param context
     * @return a SQLite database helper object
     */
    public static AppDatabase getInstance(Context context) {
        Log.d(TAG, "getInstance: Starts");
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sSQL;
        // sSQL = "CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NOT NULL, Name TEXT NOT NULL, Description TEXT, SortOrder INTEGER, CategoryID INTEGER);";
        sSQL = "CREATE TABLE        " + TaskContract.TABLE_NAME + " ("
                + TaskContract.Columns._ID + "INTEGER PRIMARY KEY NOT NULL,"
                + TaskContract.Columns.TASKS_NAME + " TEXT NOT NULL,"
                + TaskContract.Columns.TASKS_DESCRIPTION + " TEXT, SortOrder INTEGER,"
                + TaskContract.Columns.TASKS_SORTORDER + "INTEGER);";
        db.execSQL(sSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                //upgrade
                break;
            default:
                throw new IllegalStateException("Unknown new version: "+ newVersion);
        }
    }
}

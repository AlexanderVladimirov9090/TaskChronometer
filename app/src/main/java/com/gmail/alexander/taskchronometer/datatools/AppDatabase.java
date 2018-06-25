package com.gmail.alexander.taskchronometer.datatools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This is basic database class for the application.
 * AppProvider will be the one who use this class.
 */

public class AppDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskTimer.db";
    private static final int DATABASE_VERSION = 1;

    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get an instance of the app's singleton database helper object
     *
     * @param context the content providers context.
     * @return a SQLite database helper object
     */
    static AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = new AppDatabase(context);
        }

        return instance;
    }

    /**
     * This is where TaskS table is created.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String statement;

        statement = "CREATE TABLE " + TasksContract.TABLE_NAME + " ("
                + TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, "
                + TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, "
                + TasksContract.Columns.TASKS_SORTORDER + " INTEGER);";

        db.execSQL(statement);
    }

    /**
     * This is used for when there is an upgrade to the database.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                // upgrade logic from version 1
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }
    }
}
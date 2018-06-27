package com.gmail.alexander.taskchronometer.datatools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

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
    private static final int DATABASE_VERSION = 3;

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
     * This is where tables are created.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTaskTable(db);
        addTimingTable(db);
        addDurationsView(db);
    }

    private void createTaskTable(SQLiteDatabase db) {
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
     * @param db databse that is used for the update.
     * @param oldVersion older version of the database.
     * @param newVersion newer version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                // this is where update from version 1 is executed.
                addTimingTable(db);
                // fall trough, to include version 2 upgrade logic.
            case 2:
                addDurationsView(db);
                break;

            default:
                throw new IllegalStateException("onUpgrade() with unknown newVersion: " + newVersion);
        }

    }

    private void addTimingTable(SQLiteDatabase db) {
        Log.d(TAG, "addTimingTable: Starts");
        String statement = "CREATE TABLE " + TimingsContract.TABLE_NAME + " ("
                + TimingsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TimingsContract.Columns.TIMINGS_TASK_ID + " INTEGER NOT NULL, "
                + TimingsContract.Columns.TIMINGS_START_TIME + " INTEGER, "
                + TimingsContract.Columns.TIMINGS_DURATION + "  INTEGER);";
        db.execSQL(statement);

        statement = "CREATE TRIGGER Remove_Task"
                + " AFTER DELETE ON " + TasksContract.TABLE_NAME
                + " FOR EACH ROW"
                + " BEGIN"
                + " DELETE FROM " + TimingsContract.TABLE_NAME
                + " WHERE " + TimingsContract.Columns.TIMINGS_TASK_ID + " = OLD." + TasksContract.Columns._ID + ";"
                + " END;";
        db.execSQL(statement);

        Log.d(TAG, "addTimingTable: Ends");
    }

    private void addDurationsView(SQLiteDatabase db) {
        /*
        CREATE VIEW vwTaskDuration AS
        SELECT Timings._id,
      Tasks.Name,
      Tasks.Description,
      Timings.StartTime,
      DATE(Timings.StartTime, 'unixepoch') AS StartDate,
      SUM(Timings.Duration) AS Duration
      FROM Task INNER JOIN Timings
      ON Task._id = Timings.TaskId
      GROUP BY Tasks._id, StartDate;
      */

        String statement = " CREATE VIEW " + DurationsContract.TABLE_NAME
                + " AS SELECT " + TimingsContract.TABLE_NAME + "." + TimingsContract.Columns._ID + ", "
                + TimingsContract.TABLE_NAME + "." + TasksContract.Columns.TASKS_NAME + ", "
                + TasksContract.TABLE_NAME + "." + TasksContract.Columns.TASKS_DESCRIPTION + ", "
                + TimingsContract.TABLE_NAME + "." + TimingsContract.Columns.TIMINGS_START_TIME + ","
                + " DATE(" + TimingsContract.TABLE_NAME + "." + TimingsContract.Columns.TIMINGS_START_TIME + ", 'unixepoch')"
                + " AS " + DurationsContract.Columns.DURATION_START_DATE + ","
                + " SUM(" + TimingsContract.TABLE_NAME + "." + TimingsContract.Columns.TIMINGS_DURATION + ")"
                + " AS " + DurationsContract.Columns.DURATION_DURATION
                + " FROM " + TasksContract.TABLE_NAME + " JOIN " + TimingsContract.TABLE_NAME
                + " ON " + TasksContract.TABLE_NAME + "." + TasksContract.Columns._ID + " = "
                + TimingsContract.TABLE_NAME + "." + TimingsContract.Columns.TIMINGS_TASK_ID
                + " GROUP BY " + DurationsContract.Columns.DURATION_START_DATE + ", " + DurationsContract.Columns.DURATIONS_NAME
                + ";";
        Log.d(TAG, "addDurationsView: " + statement);
        db.execSQL(statement);
    }
}
package com.gmail.alexander.taskchronometer.databasetools;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */

public class AppProvider extends ContentProvider {
    private AppDatabase openHelper;
    public static final UriMatcher uriMatcher = buildUriMatcher();
    static final String CONTENT_AUTHORITY = "com.gmail.alexander.taskchronometer";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int TASKS = 100;
    private static final int TASKS_ID = 101;
    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;

    /*
        private static final int TASK_TIMINGS= 300;
        private static final int TASK_TIMINGS_ID=301;
    */
    public static final int TASK_DURATIONS = 400;
    public static final int TASK_DURATIONS_ID = 401;

    @Override
    public boolean onCreate() {
        openHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: called with URI: " + uri);
        final int match = uriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (match) {
            case TASKS:
                queryBuilder.setTables(TaskContract.TABLE_NAME);
                break;
            case TASKS_ID:
                queryBuilder.setTables(TaskContract.TABLE_NAME);
                long taskId = TaskContract.getTaskId(uri);
                queryBuilder.appendWhere(TaskContract.Columns._ID + "=" + taskId);
                break;
/*
            case TIMINGS:
                queryBuilder.setTables(TimingsContract.TABLE_NAME);
                break;
            case TIMINGS_ID:
                queryBuilder.setTables(TimingsContract.TABLE_NAME);
                long timingId= TimingContract.getTimingId(uri);
                queryBuilder.appendWhere(TimingsContract.Columns._ID+ "="+timingId);
                break;

            case TASK_DURATIONS:
                queryBuilder.setTables(DurationsContract.TABLE_NAME);
                break;
            case TASK_DURATIONS_ID:
                queryBuilder.setTables(DurationsContract.TABLE_NAME);
                long durationId= DurationsContract.getDurationId(uri);
                queryBuilder.appendWhere(DurationsContract.Columns._ID+ "="+durationId);
                break;
*/
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return TaskContract.CONTENT_TYPE;
            case TASKS_ID:
                return TaskContract.CONTENT_ITEM_TYPE;

          /*  case TIMINGS:
                return TimingsContract.Timings.CONTENT_TYPE;
            case TIMINGS_ID:
                return TimingsContract.Timings.CONTENT_ITEM_TYPE;

            case TASK_DURATIONS:
                return DurationsContract.TaskDurations.CONTENT_TYPE;
            case TASK_DURATIONS_ID:
                return DurationsContract.TaskDurations.CONTENT_ITEM_TYPE;
*/
            default:
                throw new IllegalArgumentException("unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "Entering insert, called with uri: " + uri);
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase database;
        Uri returnUri = null;
        long recordId;

        switch (match) {
            case TASKS:
                database = openHelper.getWritableDatabase();
                recordId = database.insert(TaskContract.TABLE_NAME, null, values);

                if (recordId >= 0) {
                    returnUri = TaskContract.buildTaskUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            case TIMINGS:
               /* database = openHelper.getWritableDatabase();
                recordId = database.insert(TimingsContract.Timings.buildTimingUri(recordId));
                if(recordId >= 0 ){
                    returnUri = TimingsContract.Timings.buildTimingUri(recordId);
                }else{
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
*/
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Log.d(TAG, "insert: Exiting");

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update: called with uri: " + uri);
        final int match = uriMatcher.match(uri);

        final SQLiteDatabase database;
        int count;
        String selectionCriteria;
        switch (match) {
            case TASKS:
                database = openHelper.getWritableDatabase();
                count = database.delete(TaskContract.TABLE_NAME, selection, selectionArgs);
                break;

            case TASKS_ID:
                database = openHelper.getWritableDatabase();
                long taskId = TaskContract.getTaskId(uri);
                selectionCriteria = TaskContract.Columns._ID + " = " + taskId;
                if (selection != null) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = database.delete(TaskContract.TABLE_NAME,selectionCriteria, selectionArgs);
                break;

               /* case TIMINGS:
                database = openHelper.getWritableDatabase();
                count = database.delete(TimingsContract.TABLE_NAME, selection, selectionArgs);
                break;

            case TIMINGS_ID:
                database = openHelper.getWritableDatabase();
                long timingId = TimingsContract.getTimingId(uri);
                selectionCriteria = TimingsContract.Columns._ID + " = " + timingId;
                if (selection != null) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = database.delete(TimingsContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;*/
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "update: Returning" + count);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update: called with uri: " + uri);
        final int match = uriMatcher.match(uri);

        final SQLiteDatabase database;
        int count;
        String selectionCriteria;
        switch (match) {
            case TASKS:
                database = openHelper.getWritableDatabase();
                count = database.update(TaskContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TASKS_ID:
                database = openHelper.getWritableDatabase();
                long taskId = TaskContract.getTaskId(uri);
                selectionCriteria = TaskContract.Columns._ID + " = " + taskId;
                if (selection != null) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = database.update(TaskContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;

               /* case TIMINGS:
                database = openHelper.getWritableDatabase();
                count = database.update(TimingsContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TIMINGS_ID:
                database = openHelper.getWritableDatabase();
                long timingId = TimingsContract.getTimingId(uri);
                selectionCriteria = TimingsContract.Columns._ID + " = " + timingId;
                if (selection != null) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = database.update(TimingsContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;*/
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "update: Returning" + count);
        return count;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, TaskContract.TABLE_NAME, TASKS);
        matcher.addURI(CONTENT_AUTHORITY, TaskContract.TABLE_NAME + "/#", TASKS_ID);
/*
        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS);
        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME+ "/#", TIMINGS_ID);

        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS);
        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME+ "/#", TASK_DURATIONS_ID);
   */
        return matcher;
    }
}
package com.gmail.alexander.taskchronometer.datatools;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.gmail.alexander.taskchronometer.datatools.AppProvider.CONTENT_AUTHORITY;
import static com.gmail.alexander.taskchronometer.datatools.AppProvider.CONTENT_AUTHORITY_URI;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class DurationsContract {
    static final String TABLE_NAME = "vwTaskDurations";

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String DURATIONS_NAME = TasksContract.Columns.TASKS_NAME;
        public static final String DURATIONS_DESCRIPTION = TasksContract.Columns.TASKS_DESCRIPTION;
        public static final String DURATIONS_START_TIME =TimingsContract.Columns.TIMINGS_START_TIME;
        public static final String DURATION_START_DATE = "StartDate";
        public static final String DURATION_DURATION= TimingsContract.Columns.TIMINGS_DURATION;


        private Columns() {

        }
    }

    /**
     * URI that is used to access the Durations view.
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    /**
     * Gets id of the Duration.
     *
     * @param uri uri of the Duration.
     * @return id as long.
     */
   public static long getDurationId(Uri uri) {
        return ContentUris.parseId(uri);
    }

}

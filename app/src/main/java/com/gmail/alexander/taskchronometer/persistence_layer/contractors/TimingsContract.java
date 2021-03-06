package com.gmail.alexander.taskchronometer.persistence_layer.contractors;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.gmail.alexander.taskchronometer.persistence_layer.contractors.AppProvider.CONTENT_AUTHORITY;
import static com.gmail.alexander.taskchronometer.persistence_layer.contractors.AppProvider.CONTENT_AUTHORITY_URI;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class TimingsContract {
    static final String TABLE_NAME = "Timings";

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String TIMINGS_TASK_ID = "TaskID";
        public static final String TIMINGS_START_TIME = "StartTime";
        public static final String TIMINGS_DURATION = "Duration";

        private Columns() {

        }
    }

    /**
     * URI that is used to access the Timings table.
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    /**
     * Build the URI used for single Timing
     *
     * @param TimingId id of the Timing
     * @return URI for the single Timing.
     */
    public static Uri buildTimingUri(long TimingId) {
        return ContentUris.withAppendedId(CONTENT_URI, TimingId);
    }

    /**
     * Gets id of the Timing.
     *
     * @param uri uri of the Timing.
     * @return id as long.
     */
   public static long getTimingId(Uri uri) {
        return ContentUris.parseId(uri);
    }

}

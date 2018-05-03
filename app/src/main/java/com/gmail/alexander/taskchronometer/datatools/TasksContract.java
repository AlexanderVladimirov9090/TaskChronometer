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

public class TasksContract {
    static final String TABLE_NAME = "Tasks";

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_NAME = "Name";
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORTORDER = "SortOrder";

        private Columns() {

        }
    }

    /**
     * URI that is used to access the Tasks table.
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    /**
     * Build the URI used for single task
     * @param taskId id of the task
     * @return URI for the single task.
     */
    public static Uri buildTaskUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }

    /**
     * Gets id of the task.
     * @param uri uri of the task.
     * @return id as long.
     */
    static long getTaskId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}

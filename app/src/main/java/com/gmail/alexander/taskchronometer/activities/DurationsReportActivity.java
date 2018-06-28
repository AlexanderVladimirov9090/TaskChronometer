package com.gmail.alexander.taskchronometer.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.adapters.DurationsRecViewAdapter;
import com.gmail.alexander.taskchronometer.datatools.DurationsContract;

import java.security.InvalidParameterException;
import java.util.GregorianCalendar;

public class DurationsReportActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String CURRENT_DATE = "CURRENT_DATE";
    public static final String DISPLAY_WEEK = "DISPLAY_WEEK";

    private static final String TAG = "DurationsReportActivity";
    private static final int LOADER_ID = 1;
    private static final String SELECTION_PARAM = "SELECTION";
    private static final String SELECION_ARG_PARAM = "SELECTION_ARGS";
    private static final String SORT_ORDER_PARAM = "SORT_ORDER";

    private Bundle args = new Bundle();
    private boolean displayWeek = true;
    private DurationsRecViewAdapter adapter;
    private final GregorianCalendar calendar = new GregorianCalendar();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durations_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = findViewById(R.id.td_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (adapter == null) {
            adapter = new DurationsRecViewAdapter(this, null);

        }
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(LOADER_ID, args, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                String[] projection = {BaseColumns._ID,
                        DurationsContract.Columns.DURATIONS_NAME,
                        DurationsContract.Columns.DURATIONS_DESCRIPTION,
                        DurationsContract.Columns.DURATIONS_START_TIME,
                        DurationsContract.Columns.DURATIONS_START_DATE,
                        DurationsContract.Columns.DURATIONS_DURATION};
                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = null;

                if (args != null) {
                    selection = args.getString(SELECTION_PARAM);
                    selectionArgs = args.getStringArray(SELECION_ARG_PARAM);
                    sortOrder = args.getString(SORT_ORDER_PARAM);
                }
                return new CursorLoader(this, DurationsContract.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
            default:
                throw new InvalidParameterException(TAG + " .onCreateLoader called with invalid loader id: " + id);
        }
    }

    /**
     * Executes when data is loaded.
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: ");

        adapter.swapCursor(data);
        int count = adapter.getItemCount();

        Log.d(TAG, "onLoadFinished: Count is: " + count);
    }

    /**
     * Loader is reset here for next reloading.
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: Starts");
        adapter.swapCursor(null);
    }

}

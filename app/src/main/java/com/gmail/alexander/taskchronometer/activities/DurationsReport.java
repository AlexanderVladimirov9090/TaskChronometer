package com.gmail.alexander.taskchronometer.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.adapters.DurationsRecViewAdapter;

import java.util.GregorianCalendar;

public class DurationsReport extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String CURRENT_DATE = "CURRENT_DATE";
    public static  final String DISPLAY_WEEK = "DISPLAY_WEEK";

    private static final  String TAG = "DurationsReport";
    private static final int LOADER_ID=1;
    private static final String SELECTION_PARAM="SELECTION";
    private static final String SELECION_ARG_PARAM="SELECTION_ARGS";
    private static final String SORT_ORDER_PARAM = "SORT_ORDER";

    private Bundle args= new Bundle();
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
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = findViewById(R.id.td_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(adapter==null){
            adapter = new DurationsRecViewAdapter(this,null);

        }
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(LOADER_ID,args,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
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

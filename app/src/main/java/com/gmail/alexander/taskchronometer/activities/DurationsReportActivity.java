package com.gmail.alexander.taskchronometer.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.adapters.DurationsRecViewAdapter;
import com.gmail.alexander.taskchronometer.contractors.DurationsContract;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DurationsReportActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, DatePickerDialog.OnDateSetListener {
    public static final String CURRENT_DATE = "CURRENT_DATE";
    public static final String DISPLAY_WEEK = "DISPLAY_WEEK";

    public static final int DIALOG_FILTER = 1;
    public static final int DIALOG_DELETE = 2;

    private static final int LOADER_ID = 1;
    private static final String TAG = "DurationsReportActivity";
    private static final String SELECTION_PARAM = "SELECTION";
    private static final String SELECTION_ARG_PARAM = "SELECTION_ARGS";
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
        applyFilter();
        RecyclerView recyclerView = findViewById(R.id.td_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (adapter == null) {
            adapter = new DurationsRecViewAdapter(this, null);

        }
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(LOADER_ID, args, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.rm_filter_period:
                displayWeek = !displayWeek;
                applyFilter();
                invalidateOptionsMenu();
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                return true;
            case R.id.rm_filter_date:
                showDatePickerDialog("Select date for report", DIALOG_FILTER);
                return true;
            case R.id.rm_delete:
                //TODO showDatePickerDialog(); //Actucal Deleting will be done in onDataSer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.rm_filter_period);
        if (item != null) {
            if (displayWeek) {
                item.setIcon(R.mipmap.ic_day);
                item.setTitle(R.string.rm_title_filter_day);
            } else {
                item.setIcon(R.mipmap.ic_week);
                item.setTitle(R.string.rm_title_filter_week);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void showDatePickerDialog(String title, int dialogId) {
        Log.d(TAG, "showDatePickerDialog: starts");
        DialogFragment dialogFragment = new PickedDateFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(PickedDateFragment.DATE_PICKER_ID, dialogId);
        arguments.putString(PickedDateFragment.DATE_PICKER_TITLE, title);
        arguments.putSerializable(PickedDateFragment.DATE_PICKER_DATE, calendar.getTime());
        dialogFragment.setArguments(arguments);
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, "onDateSet: called");
        //Check the id
        int dialogId = (int) view.getTag();
        switch (dialogId) {
            case DIALOG_FILTER:
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                applyFilter();
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                break;
            case DIALOG_DELETE:
                break;
            default:
                throw new IllegalArgumentException("Invalid mode when receiving DataPickerDialog result!");
        }
    }

    private void applyFilter() {
        Log.d(TAG, "applyFilter: starts");

        if (displayWeek) {
            //Shows entries by week.

            Date currentDate = calendar.getTime();// saves current date
            int dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
            int weekStart = calendar.getFirstDayOfWeek();
            Log.d(TAG, "applyFilter: First day of the week: " + weekStart);
            Log.d(TAG, "applyFilter: Day of the week: " + dayOfWeek);
            Log.d(TAG, "applyFilter: Date is: " + calendar.getTime());
            //calculate week start and end dates.
            calendar.set(GregorianCalendar.DAY_OF_WEEK, weekStart);
            String startDate = String.format(Locale.US, "%04d-%02d-%02d",
                    calendar.get(GregorianCalendar.YEAR),
                    calendar.get(GregorianCalendar.MONTH) + 1,
                    calendar.get(GregorianCalendar.DAY_OF_MONTH));

            calendar.add(GregorianCalendar.DATE, 6); // move forward 6 days to get the last day of the week.

            String endDate = String.format(Locale.US, "%04d-%02d-%02d",
                    calendar.get(GregorianCalendar.YEAR),
                    calendar.get(GregorianCalendar.MONTH) + 1,
                    calendar.get(GregorianCalendar.DAY_OF_MONTH));
            String[] selectionArgs = new String[]{startDate, endDate};

            //put calendar back to where it was before we started jumping back and forth.
            calendar.setTime(currentDate);
            Log.d(TAG, "applyFilter: Start Date is: " + startDate + "End date: " + endDate);
            args.putString(SELECTION_PARAM, "StartDate Between ? AND ?");
            args.putStringArray(SELECTION_ARG_PARAM, selectionArgs);

        } else {
            //day entries.
            String startDate = String.format(Locale.US, "%04d-%02d-%02d",
                    calendar.get(GregorianCalendar.YEAR),
                    calendar.get(GregorianCalendar.MONTH) + 1,
                    calendar.get(GregorianCalendar.DAY_OF_MONTH));
            String[] selectionArgs = new String[]{startDate};
            Log.d(TAG, "applyFilter: Start date: " + startDate);
            args.putString(SELECTION_PARAM, "StartDate= ?");
            args.putStringArray(SELECTION_ARG_PARAM, selectionArgs);
        }
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
                    selectionArgs = args.getStringArray(SELECTION_ARG_PARAM);
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

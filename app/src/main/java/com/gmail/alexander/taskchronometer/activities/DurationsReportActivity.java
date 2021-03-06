package com.gmail.alexander.taskchronometer.activities;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.adapters.DurationsRecViewAdapter;
import com.gmail.alexander.taskchronometer.dialogs.AppDialog;
import com.gmail.alexander.taskchronometer.dialogs.DialogEvents;
import com.gmail.alexander.taskchronometer.persistence_layer.contractors.DurationsContract;
import com.gmail.alexander.taskchronometer.persistence_layer.contractors.TimingsContract;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DurationsReportActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        DatePickerDialog.OnDateSetListener,
        DialogEvents, View.OnClickListener {
    public static final String CURRENT_DATE = "CURRENT_DATE";
    public static final String DISPLAY_WEEK = "DISPLAY_WEEK";

    public static final int DIALOG_FILTER = 1;
    public static final int DIALOG_DELETE = 2;
    public static final String DELETION_DATE = "DELETION_DATE";
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
        if (savedInstanceState != null) {
            long timeInMillis = savedInstanceState.getLong(CURRENT_DATE, 0);
            //when the Activity is created it will be 0.
            if (timeInMillis != 0) {
                calendar.setTimeInMillis(timeInMillis);
                //Make sure that the time is cleared.
                calendar.clear(GregorianCalendar.HOUR_OF_DAY);
                calendar.clear(GregorianCalendar.MINUTE);
                calendar.clear(GregorianCalendar.SECOND);
            }
            displayWeek = savedInstanceState.getBoolean(DISPLAY_WEEK, true);
        }

        // Set the listener for the buttons so we can sort the report.
        TextView taskName = findViewById(R.id.td_name_heading);
        taskName.setOnClickListener(this);

        TextView taskDesc = findViewById(R.id.td_description_heading);
        if(taskDesc != null) {
            taskDesc.setOnClickListener(this);
        }

        TextView taskDate = findViewById(R.id.td_start_heading);
        taskDate.setOnClickListener(this);

        TextView taskDuration = findViewById(R.id.td_duration_heading);
        taskDuration.setOnClickListener(this);

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURRENT_DATE, calendar.getTimeInMillis());
        outState.putBoolean(DISPLAY_WEEK, displayWeek);
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
                showDatePickerDialog(getString(R.string.date_title_filter), DIALOG_FILTER);
                return true;
            case R.id.rm_delete:
                showDatePickerDialog(getString(R.string.date_title_delete), DIALOG_DELETE);
                //  onDateSet();
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
        calendar.set(year, month, dayOfMonth, 0, 0, 0);

        switch (dialogId) {
            case DIALOG_FILTER:
                applyFilter();
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                break;
            case DIALOG_DELETE:
                String fromDate = android.text.format.DateFormat.getDateFormat(this).format(calendar.getTimeInMillis());
                AppDialog dialog = new AppDialog();
                Bundle args = new Bundle();
                args.putInt(AppDialog.DIALOG_ID, 1);
                args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.delete_timings_message, fromDate));
                args.putLong(DELETION_DATE, calendar.getTimeInMillis());
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), null);
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

    @NonNull
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
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
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: Starts");
        adapter.swapCursor(null);
    }

    private void deleteRecords(Long deleteDate) {
        Log.d(TAG, "deleteRecords: Starts");
        long longDate = deleteDate / 1000;
        String[] selectionArgs = new String[]{Long.toString(longDate)};
        String selection = TimingsContract.Columns.TIMINGS_START_TIME + " < ?";
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(TimingsContract.CONTENT_URI, selection, selectionArgs);
        applyFilter();
        getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
        Log.d(TAG, "deleteRecords: Finished");
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: Starts");
        Long deleteDate = args.getLong(DELETION_DATE);
        deleteRecords(deleteDate);
        //re-query, to refresh the view.
        getSupportLoaderManager().restartLoader(LOADER_ID, args, this);

    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {

    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }

    /**
     * Modify the order of timings data.
     * By Name.
     * By Description.
     * By Start Time.
     * By Duration of a task.
     * @param v view that is tapped.
     */
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: Started");
        switch(v.getId()) {
            case R.id.td_name_heading:
                args.putString(SORT_ORDER_PARAM, DurationsContract.Columns.DURATIONS_NAME);
                break;
            case R.id.td_description_heading:
                args.putString(SORT_ORDER_PARAM, DurationsContract.Columns.DURATIONS_DESCRIPTION);
                break;
            case R.id.td_start_heading:
                args.putString(SORT_ORDER_PARAM, DurationsContract.Columns.DURATIONS_START_DATE);
                break;
            case R.id.td_duration_heading:
                args.putString(SORT_ORDER_PARAM, DurationsContract.Columns.DURATIONS_DURATION);
                break;
        }

        getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
    }
}

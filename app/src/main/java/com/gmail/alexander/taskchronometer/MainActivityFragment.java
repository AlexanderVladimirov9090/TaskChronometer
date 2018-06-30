package com.gmail.alexander.taskchronometer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.alexander.taskchronometer.adapters.CursorRecyclerViewAdapter;
import com.gmail.alexander.taskchronometer.persistence_layer.contractors.TasksContract;
import com.gmail.alexander.taskchronometer.persistence_layer.contractors.TimingsContract;
import com.gmail.alexander.taskchronometer.domain_layer.Task;
import com.gmail.alexander.taskchronometer.domain_layer.Timing;
import com.gmail.alexander.taskchronometer.listeners.OnTaskClickListener;

import java.security.InvalidParameterException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskClickListener {
    private static final String TAG = "MainActivityFragment";
    public static final int LOADER_ID = 0;
    private CursorRecyclerViewAdapter adapter;
    private Timing currentTiming = null;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        //Activity containing this fragment must implement its callbacks.
        if (!(activity instanceof OnTaskClickListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName() + " must be implemented OnClickListener interface.");
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
        setTimingText(currentTiming);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Starts");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (adapter == null) {
            //This fragment implements OnClickListener that is why this is called here.
            adapter = new CursorRecyclerViewAdapter(null, this);
        } /*else {
            adapter.setOnTaskClickListener((OnTaskClickListener) getActivity());
        }*/
        recyclerView.setAdapter(adapter);
        return view;
    }

    /**
     * The creation of the loader.
     *
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts with  id: " + id);
        String[] projection = {
                TasksContract.Columns._ID,
                TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION,
                TasksContract.Columns.TASKS_SORTORDER};
        String sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME + " COLLATE NOCASE";
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getActivity(),
                        TasksContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        sortOrder);

            default:
                throw new InvalidParameterException(TAG + "onCreateLoader called with invalid id: " + id);
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


    @Override
    public void onEditClick(@NonNull Task task) {
        OnTaskClickListener listener = (OnTaskClickListener) getActivity();
        if (listener != null) {
            listener.onEditClick(task);
        }
    }

    @Override
    public void onDeleteClick(@NonNull Task task) {
        OnTaskClickListener listener = (OnTaskClickListener) getActivity();
        if (listener != null) {
            listener.onDeleteClick(task);
        }
    }

    @Override
    public void onTaskLongClick(@NonNull Task task) {
        if (currentTiming != null) {
            if (task.getId() == currentTiming.getTask().getId()) {
                //The current task was tapped a second time, so stop timing.
                saveTiming(currentTiming);
                currentTiming = null;
                setTimingText(null);
            } else {
                // a new task is being timed, so stop the old one first.
                saveTiming(currentTiming);
                currentTiming = new Timing(task);
                setTimingText(currentTiming);

            }
        } else {
            currentTiming = new Timing(task);
            setTimingText(currentTiming);
        }
    }

    /**
     * Saves Timings of a task to the database.
     *
     * @param currentTiming
     */
    private void saveTiming(@NonNull Timing currentTiming) {

        //If we have an open timing set duration to save.
        currentTiming.setDuration();
        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(TimingsContract.Columns.TIMINGS_TASK_ID, currentTiming.getTask().getId());
        values.put(TimingsContract.Columns.TIMINGS_START_TIME, currentTiming.getStartTime());
        values.put(TimingsContract.Columns.TIMINGS_DURATION, currentTiming.getDuration());
        contentResolver.insert(TimingsContract.CONTENT_URI, values);

    }

    private void setTimingText(Timing timing) {
        TextView taskName = getActivity().findViewById(R.id.curent_task);

        if (timing != null) {
            taskName.setText("Timing " + currentTiming.getTask().getName());
        } else {
            taskName.setText(R.string.no_task_message);
        }
    }
}

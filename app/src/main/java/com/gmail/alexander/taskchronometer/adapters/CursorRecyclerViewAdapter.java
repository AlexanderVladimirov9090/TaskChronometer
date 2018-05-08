package com.gmail.alexander.taskchronometer.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.datatools.TasksContract;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private static final String TAG = "CursorRecycleAdapter";
    private Cursor cursor;

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: New view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Starts");
        if ((cursor == null) || cursor.getCount() == 0) {
            Log.d(TAG, "onBindViewHolder: Providing Instructions");
            holder.name.setText("Instructions");
            holder.decription.setText("Use the add button (+) in the toolbar above to create new task.\nTask with lower sort order will be placed higher up the list.\nTask with the same sort order will be sorted alphabetically.\nTapping a task will start the timer fot that task (and will stop the timer for previously task that was being timed).\nEach task has Edit and Delete buttons if you want to change details or remote the task.");
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);

        } else {
            if (!cursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn`t move cursor to position: " + position);
            }
            holder.name.setText(cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)));
            holder.decription.setText(cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)));
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

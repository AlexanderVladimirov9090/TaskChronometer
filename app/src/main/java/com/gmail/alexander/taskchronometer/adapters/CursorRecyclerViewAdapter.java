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

    public CursorRecyclerViewAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

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
            holder.decription.setText(R.string.user_instruction);
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
        Log.d(TAG, "getItemCount: Starts");
        if (cursor == null || cursor.getCount() == 0) {
            return 1;//fib, because we populate a single ViewHolder with instructions.
        } else {
            return cursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     * The returned old Cursor is not closed.
     *
     * @param newCursor the new cursor to be used.
     * @return Returns the previously set Cursor, or null if is there wasn`t one.
     * If the given new Cursor is the same instance as the previously set Cursor, null is also returned.
     */
   public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        cursor = newCursor;
        if (newCursor != null) {
            //notify the observers about the new cursor.
            notifyDataSetChanged();
        } else {
            // notify the observers about the lack of a data set.
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }
}

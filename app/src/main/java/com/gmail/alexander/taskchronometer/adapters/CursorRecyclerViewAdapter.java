package com.gmail.alexander.taskchronometer.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.datatools.TasksContract;
import com.gmail.alexander.taskchronometer.domain_layer.Task;
import com.gmail.alexander.taskchronometer.listeners.OnTaskClickListener;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This adapter is used for the Recycler View.
 */
public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private Cursor cursor;
    private OnTaskClickListener onTaskClickListener;

    public CursorRecyclerViewAdapter(Cursor cursor, OnTaskClickListener onTaskClickListener) {
        this.cursor = cursor;
        this.onTaskClickListener = onTaskClickListener;
    }

 /*   public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }
*/
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items, parent, false);

        return new TaskViewHolder(view);
    }

    /**
     * This is where the binding data to the view.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        if ((cursor == null) || cursor.getCount() == 0) {

            holder.name.setText("Instructions");
            holder.description.setText(R.string.user_instruction);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);

        } else {
            if (!cursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn`t move cursor to position: " + position);
            }

            final Task task = new Task(cursor.getLong(cursor.getColumnIndex(TasksContract.Columns._ID)),
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)),
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(TasksContract.Columns.TASKS_SORTORDER)));

            holder.name.setText(task.getName());
            holder.description.setText(task.getDescription());
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);

            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.til_edit:
                            if (onTaskClickListener != null) {
                                onTaskClickListener.onEditClick(task);
                            }
                            break;
                        case R.id.til_delete:
                            if (onTaskClickListener != null) {
                                onTaskClickListener.onDeleteClick(task);
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown button id: " + view.getId());
                    }
                }
            };

            holder.editButton.setOnClickListener(buttonListener);
            holder.deleteButton.setOnClickListener(buttonListener);
        }

    }

    /**
     * This is where counting of the items is done.
     *
     * @return counted times.
     */
    @Override
    public int getItemCount() {

        if (cursor == null || cursor.getCount() == 0) {
            return 1;
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
package com.gmail.alexander.taskchronometer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.persistence_layer.contractors.DurationsContract;

import java.util.Locale;


/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class DurationsRecViewAdapter extends RecyclerView.Adapter<DurationViewHolder> {
    private Cursor cursor;
    private final java.text.DateFormat dateFormat;

    public DurationsRecViewAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        this.dateFormat = DateFormat.getDateFormat(context);
    }

    @Override
    public DurationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_duration_item, parent, false);

        return new DurationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DurationViewHolder holder, int position) {
        if ((cursor != null) && (cursor.getCount() != 0)) {
            if (!cursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }
            String name = cursor.getString(cursor.getColumnIndex(DurationsContract.Columns.DURATIONS_NAME));
            String description = cursor.getString(cursor.getColumnIndex(DurationsContract.Columns.DURATIONS_DESCRIPTION));
            Long startTime = cursor.getLong(cursor.getColumnIndex(DurationsContract.Columns.DURATIONS_START_TIME));
            long totalDuration = cursor.getLong(cursor.getColumnIndex(DurationsContract.Columns.DURATIONS_DURATION));

            holder.name.setText(name);
            if (holder.description != null) {    // Description is not present in portrait
                holder.description.setText(description);
            }

            String userDate = dateFormat.format(startTime * 1000); // The database stores seconds, we need milliseconds
            String totalTime = formatDuration(totalDuration);

            holder.startDate.setText(userDate);
            holder.duration.setText(totalTime);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    /**
     * Convert duration to formatted date.
     *
     * @param duration
     * @return
     */
    private String formatDuration(long duration) {
        long hours = duration / 3600;
        long remainder = duration - (hours * 3600);
        long minutes = remainder / 60;
        long seconds = remainder - (minutes * 60);

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
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

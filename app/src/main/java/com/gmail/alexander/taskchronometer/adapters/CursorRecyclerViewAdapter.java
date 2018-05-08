package com.gmail.alexander.taskchronometer.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<TaskViewHolder>{
    private static final String TAG ="CursorRecycleViewAdapter";
    private Cursor cursor;
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

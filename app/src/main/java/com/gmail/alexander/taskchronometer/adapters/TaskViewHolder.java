package com.gmail.alexander.taskchronometer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.alexander.taskchronometer.R;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 *     This is the task view holder used for Recycler View Adapter.
 */
class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView name = null;
    TextView description = null;
    ImageButton editButton = null;
    ImageButton deleteButton = null;

    public TaskViewHolder(View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.til_name);
        this.description = (TextView) itemView.findViewById(R.id.til_description);
        this.editButton = (ImageButton) itemView.findViewById(R.id.til_edit);
        this.deleteButton = (ImageButton) itemView.findViewById(R.id.til_delete);
    }
}

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
 * This is the task view holder used for Recycler View Adapter.
 */
class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView description;
    ImageButton editButton;
    ImageButton deleteButton;
    View itemView;
    TaskViewHolder(View itemView) {

        super(itemView);

        this.name = itemView.findViewById(R.id.til_name);
        this.description = itemView.findViewById(R.id.til_description);
        this.editButton = itemView.findViewById(R.id.til_edit);
        this.deleteButton = itemView.findViewById(R.id.til_delete);
        this.itemView = itemView;
    }
}

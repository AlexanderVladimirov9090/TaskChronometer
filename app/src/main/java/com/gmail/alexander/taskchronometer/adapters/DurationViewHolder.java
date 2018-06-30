package com.gmail.alexander.taskchronometer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gmail.alexander.taskchronometer.R;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class DurationViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView description;
    TextView startDate;
    TextView duration;

    public DurationViewHolder(View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.td_name);
        this.description = itemView.findViewById(R.id.td_description);
        this.startDate = itemView.findViewById(R.id.td_start);
        this.duration = itemView.findViewById(R.id.td_duration);
    }
}

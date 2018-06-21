package com.gmail.alexander.taskchronometer.listeners;

import android.support.annotation.NonNull;

import com.gmail.alexander.taskchronometer.domain_layer.Task;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This interface is used for Editing and Deleting actions.
 *
 *
 */
public interface OnTaskClickListener {
    /**
     * Handles on edit action.
     * @param task
     */
    void onEditClick(@NonNull Task task);

    /**
     * Handles on delete action.
     * @param task
     */
    void onDeleteClick(@NonNull Task task);

    /**
     * Handles on long click actions.
     * @param task
     */
    void onTaskLongClick(@NonNull Task task);
}

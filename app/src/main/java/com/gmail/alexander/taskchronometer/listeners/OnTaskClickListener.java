package com.gmail.alexander.taskchronometer.listeners;

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
    void onEditClick(Task task);

    /**
     * Handles on delete action.
     * @param task
     */
    void onDeleteClick(Task task);
}

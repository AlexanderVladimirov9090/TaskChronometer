package com.gmail.alexander.taskchronometer.listeners;

import com.gmail.alexander.taskchronometer.domain_layer.Task;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This is the on click listener for edit and delete buttons.
 *
 */
public interface OnTaskClickListener {

    void onEditClick(Task task);

    void onDeleteClick(Task task);
}

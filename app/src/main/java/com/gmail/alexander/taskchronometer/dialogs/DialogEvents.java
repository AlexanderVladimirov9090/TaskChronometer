package com.gmail.alexander.taskchronometer.dialogs;

import android.os.Bundle;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This interface is used as a callback to notify of user selected result
 * Deletion, confirmation or cencellation.
 */
public interface DialogEvents {

    void onPositiveDialogResult(int dialogId, Bundle args);

    void onNegativeDialogResult(int dialogId, Bundle args);

    void onDialogCancelled(int dialogId);

}

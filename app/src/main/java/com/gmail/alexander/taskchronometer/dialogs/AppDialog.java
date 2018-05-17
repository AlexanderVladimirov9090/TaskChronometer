package com.gmail.alexander.taskchronometer.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class AppDialog extends DialogFragment {
    private static final String TAG = "AppDialog";
    public static final String DIALOG_ID = "id";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";
    private DialogEvents dialogEvents;

    /**
     * Assign value to dialogEvents.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: Etering onAttach, activity is " + context.toString());
        super.onAttach(context);
        if (!(context instanceof DialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement AddDialog interface");
        }
        dialogEvents = (DialogEvents) context;
    }

    /**
     * Resets dialogEvents.
     */
    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: Entering...");
        super.onDetach();
        dialogEvents = null;
    }
}



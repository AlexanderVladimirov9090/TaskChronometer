package com.gmail.alexander.taskchronometer.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.gmail.alexander.taskchronometer.R;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * This is where Dialog boxes are configurated.
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
        Log.d(TAG, "onAttach: Entering onAttach, activity is " + context.toString());
        super.onAttach(context);
        if (!(context instanceof DialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement AddDialog interface");
        }
        dialogEvents = (DialogEvents) context;
    }

    /**
     * Resets dialog events.
     */
    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: Entering...");
        super.onDetach();
        dialogEvents = null;
    }

    /**
     * This is where the creation of Dialog box is created.
     *
     * @param savedInstanceState
     * @return Dialog object.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: Starts");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle arguments = getArguments();
        final int dialogId;
        String messageString;
        int positiveStringId;
        int negativeStringId;

        if (arguments != null) {
            dialogId = arguments.getInt(DIALOG_ID);
            messageString = arguments.getString(DIALOG_MESSAGE);

            if (dialogId == 0 || messageString == null) {
                throw new IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE not present in the bundle.");
            }

            positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID);
            if (positiveStringId == 0) {
                positiveStringId = R.string.ok;
            }

            negativeStringId = arguments.getInt(DIALOG_NEGATIVE_RID);
            if (negativeStringId == 0) {
                negativeStringId = R.string.cancel;
            }
        } else {
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle.");
        }

        builder.setMessage(messageString).setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //On positive result this action will take place.
                if (dialogEvents != null) {
                    dialogEvents.onPositiveDialogResult(dialogId, arguments);
                }
            }
        }).setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //On negative result this action will take place.
                if (dialogEvents != null) {
                    dialogEvents.onNegativeDialogResult(dialogId, arguments);
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d(TAG, "onCancel: Called");
        if (dialogEvents != null) {
            int dialogId = getArguments().getInt(DIALOG_ID);
            dialogEvents.onDialogCancelled(dialogId);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(TAG, "onDismiss: Called.");
        super.onDismiss(dialog);
    }
}
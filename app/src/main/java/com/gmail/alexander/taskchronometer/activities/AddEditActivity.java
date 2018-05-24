package com.gmail.alexander.taskchronometer.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.dialogs.AppDialog;
import com.gmail.alexander.taskchronometer.dialogs.DialogEvents;
import com.gmail.alexander.taskchronometer.listeners.OnSaveListener;

/**
 * This class is used for  add and edit of the task actions.
 */
public class AddEditActivity extends AppCompatActivity implements OnSaveListener, DialogEvents {
    public static final String TAG = "AddEditActivity";
    private static final int DIALOG_ID_CANCEL_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.fragment) == null) {
            AddEditActivityFragment fragment = new AddEditActivityFragment();

            Bundle arguments = getIntent().getExtras();
            fragment.setArguments(arguments);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: Home button pressed");
                AddEditActivityFragment fragment = (AddEditActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                if (fragment.canClose()) {
                    return super.onOptionsItemSelected(item);

                } else {
                    showConfirmationDialog();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Finishes the intent that is used to call this class.
     */
    @Override
    public void onSaveClicked() {
        finish();
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: Starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.fragment);
        if ((fragment == null) || fragment.canClose()) {
            super.onBackPressed();
        } else {
            showConfirmationDialog();
        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: Starts");
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: Starts");
        finish();
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: Starts");
    }

    /**
     * Shows confirmation dialog to the user.
     */
    private void showConfirmationDialog() {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.canclerEditDiag_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancleEditDiag_positive_caption);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancleEditDiag_negative_caption);

        dialog.setArguments(args);
        dialog.show(getFragmentManager(), null);
    }
}

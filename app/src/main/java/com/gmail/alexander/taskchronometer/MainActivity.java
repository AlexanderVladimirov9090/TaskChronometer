package com.gmail.alexander.taskchronometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.alexander.taskchronometer.activities.AddEditActivity;
import com.gmail.alexander.taskchronometer.activities.AddEditActivityFragment;
import com.gmail.alexander.taskchronometer.datatools.TasksContract;
import com.gmail.alexander.taskchronometer.dialogs.AppDialog;
import com.gmail.alexander.taskchronometer.dialogs.DialogEvents;
import com.gmail.alexander.taskchronometer.domain_layer.Task;
import com.gmail.alexander.taskchronometer.listeners.OnSaveListener;
import com.gmail.alexander.taskchronometer.listeners.OnTaskClickListener;

/**
 * This is the starting point of the application.
 */
public class MainActivity extends AppCompatActivity implements OnTaskClickListener, OnSaveListener, DialogEvents {
    private static final String TAG = "MainActivity";
    // Whether or not th    e activity is in 2-pane mode
    // i.e. running in landscape on a tablet
    //If landscape is on for tablets
    private boolean twoPane = false;

    public static final int DIALOG_ID_DELETE = 1;
    public static final int DIALOG_ID_CANCEL_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.task_details_container) != null) {
            //The detail container view will be present only of the screen is large enough.
            // If this view is present, then activity should be in two-pane mode.
            twoPane = true;

        }

    }

    /**
     * This is where the menu is created.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This is where the select item from menu is executed.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDurations:
                break;
            case R.id.menumain_settings:
                break;
            case R.id.menumain_showAbout:
                break;
            case R.id.menumain_generate:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fires taskEditRequest for editing a task.
     *
     * @param task
     */
    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    /**
     * Prompts user in form of dialog box for deletion of a task.
     *
     * @param task
     */
    @Override
    public void onDeleteClick(Task task) {
        AppDialog appDialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.delete_notification_message, task.getId(), task.getName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.del_dialog_positive_caption);
        args.putLong("TaskID", task.getId());
        appDialog.setArguments(args);
        appDialog.show(getFragmentManager(), null);
    }

    /**
     * Saves task in to database.
     */
    @Override
    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: Starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    /**
     * Deletion of task is handled here when dialog box is tapped delete button.
     *
     * @param dialogId
     * @param args
     */
    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Long taskId = args.getLong("TaskID");
        if (BuildConfig.DEBUG && taskId == 0) {
            throw new AssertionError("Task ID is zero");
        }
        getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
    }

    /**
     * This method is fired when tapped cancel on the dialog box.
     *
     * @param dialogId
     * @param args
     */
    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");

    }

    /**
     * This method is fired when dialog is cancelled.
     *
     * @param dialogId
     */
    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: Starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if ((fragment == null) || fragment.canClose()) {
            super.onBackPressed();
        } else {
            //show dialog to get confirmation to quit editing.
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

    /**
     * This is fired when user want to edit a task.
     * Using Two-pane mode if the screen size allows it.
     *
     * @param task
     */
    private void taskEditRequest(Task task) {

        if (twoPane) {
            Log.d(TAG, "taskEditRequest: Two-pane mode");
            AddEditActivityFragment fragment = new AddEditActivityFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.task_details_container, fragment)
                    .commit();
        } else {

            Intent detailIntent = new Intent(this, AddEditActivity.class);

            if (task != null) {
                detailIntent.putExtra(Task.class.getSimpleName(), task);
                startActivity(detailIntent);
            } else {
                startActivity(detailIntent);
            }
        }
    }
}

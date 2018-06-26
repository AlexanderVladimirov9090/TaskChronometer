package com.gmail.alexander.taskchronometer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
    public static final int DIALOG_ID_DELETE = 1;
    //If landscape is on for tablets
    public static final int DIALOG_ID_CANCEL_EDIT = 2;
    private boolean twoPane = false;
    private AlertDialog dialog = null;
    private static final int DIALOG_ID_CANCEL_EDIT_UP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        twoPane = (getResources()).getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        Log.d(TAG, "onCreate: Twopane is: " + twoPane);
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Id AddEditActivity fragment is present. It is in editing mode.
        boolean editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;
        Log.d(TAG, "onCreate: Editing is " + editing);
        View addEditLayout = findViewById(R.id.task_details_container);
        View mainFragment = findViewById(R.id.fragment);
        if (twoPane) {
            Log.d(TAG, "onCreate: twoPane mode ");
            mainFragment.setVisibility(View.VISIBLE);
            addEditLayout.setVisibility(View.VISIBLE);
        } else {
            if (editing) {
                mainFragment.setVisibility(View.GONE);
            } else {
                mainFragment.setVisibility(View.VISIBLE);
                addEditLayout.setVisibility(View.GONE);
            }
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
                showAboutDialog();
                break;
            case R.id.menumain_generate:
                break;
            case android.R.id.home:
                AddEditActivityFragment fragment = (AddEditActivityFragment) getSupportFragmentManager().findFragmentById(R.id.task_details_container);
                if (fragment.canClose()) {
                    return super.onOptionsItemSelected(item);

                } else {
                    showConfirmationDialog(DIALOG_ID_CANCEL_EDIT_UP);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fires taskEditRequest for editing a task.
     *
     * @param task
     */
    @Override
    public void onEditClick(@NonNull Task task) {
        taskEditRequest(task);
    }

    /**
     * Prompts user in form of dialog box for deletion of a task.
     *
     * @param task
     */
    @Override
    public void onDeleteClick(@NonNull Task task) {
        AppDialog appDialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.delete_notification_message, task.getId(), task.getName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.del_dialog_positive_caption);
        args.putLong("TaskID", task.getId());
        appDialog.setArguments(args);
        appDialog.show(getFragmentManager(), null);
    }

    @Override
    public void onTaskLongClick(@NonNull Task task) {
     // Need to satisfy the interface.
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
        View addEditLayout = findViewById(R.id.task_details_container);
        View mainFragment = findViewById(R.id.fragment);

        if (!twoPane) {
            addEditLayout.setVisibility(View.GONE);
            mainFragment.setVisibility(View.VISIBLE);
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
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                if (BuildConfig.DEBUG && taskId == 0) {
                    throw new AssertionError("Task ID is zero");
                }
                getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
                break;
            case DIALOG_ID_CANCEL_EDIT:
            case DIALOG_ID_CANCEL_EDIT_UP:
                break;
        }
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
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                break;
            case DIALOG_ID_CANCEL_EDIT:
            case DIALOG_ID_CANCEL_EDIT_UP:
                //If we are editing, remove the fragment or close app.
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
                if (fragment != null) {
                    getSupportFragmentManager().
                            beginTransaction().
                            remove(fragment).
                            commit();
                    if (twoPane) {
                        //in Landscape, quit if only the back button was pressed.
                        if (dialogId == DIALOG_ID_CANCEL_EDIT) {
                            finish();
                        }
                    } else {
                        View addEditLayout = findViewById(R.id.task_details_container);
                        View mainFragment = findViewById(R.id.fragment);
                        addEditLayout.setVisibility(View.GONE);
                        mainFragment.setVisibility(View.VISIBLE);
                    }
                } else {
                    finish();
                }
                break;
        }
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

    /**
     * Handles action when back button is pressed.
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: Starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if ((fragment == null) || fragment.canClose()) {
            super.onBackPressed();
        } else {
            //show dialog to get confirmation to quit editing.
            showConfirmationDialog(DIALOG_ID_CANCEL_EDIT);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * This is fired when user want to edit a task.
     * Using Two-pane mode if the screen size allows it.
     *
     * @param task
     */
    private void taskEditRequest(Task task) {

        AddEditActivityFragment fragment = new AddEditActivityFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(Task.class.getSimpleName(), task);
        fragment.setArguments(arguments);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.task_details_container, fragment)
                .commit();
        if (!twoPane) {

            View mainFragment = findViewById(R.id.fragment);
            View addEditLayout = findViewById(R.id.task_details_container);
            mainFragment.setVisibility(View.GONE);
            addEditLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Shows information about application build and author.
     */
    private void showAboutDialog() {
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_foreground);
        builder.setView(messageView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);


        TextView tv = (TextView) messageView.findViewById(R.id.about_version);
        String versionInfo = "v" + BuildConfig.VERSION_NAME;
        tv.setText(versionInfo);
        dialog.show();
    }

    /**
     * Shows confirmation dialog to the user.
     */
    private void showConfirmationDialog(int idDialog) {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, idDialog);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.canclerEditDiag_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancleEditDiag_positive_caption);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancleEditDiag_negative_caption);

        dialog.setArguments(args);
        dialog.show(getFragmentManager(), null);
    }
}

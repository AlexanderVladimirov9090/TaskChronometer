package com.gmail.alexander.taskchronometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.alexander.taskchronometer.activities.AddEditActivity;
import com.gmail.alexander.taskchronometer.activities.AddEditActivityFragment;
import com.gmail.alexander.taskchronometer.datatools.TasksContract;
import com.gmail.alexander.taskchronometer.domain_layer.Task;
import com.gmail.alexander.taskchronometer.listeners.OnTaskClickListener;

/**
 * This is the starting point of the application.
 */
public class MainActivity extends AppCompatActivity implements OnTaskClickListener {
    private static final String TAG = "MainActivity";
    // Whether or not th    e activity is in 2-pane mode
    // i.e. running in landscape on a tablet
    //If landscape is on for tablets
    private boolean twoPane = false;

    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

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

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        getContentResolver().delete(TasksContract.buildTaskUri(task.getId()), null, null);
    }

    /**
     * This method is checking if Two-pane mode active or not.
     *
     * @param task
     */
    private void taskEditRequest(Task task) {

        if (twoPane) {
            Log.d(TAG, "taskEditRequest: Two-pane mode");
            AddEditActivityFragment fragment = new AddEditActivityFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(),task);
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.task_details_container, fragment);
            fragmentTransaction.commit();
        } else {

            Intent detailIntent = new Intent(this, AddEditActivity.class);

            if (task != null) {
                detailIntent.putExtra(com.gmail.alexander.taskchronometer.domain_layer.Task.class.getName(), task);
                startActivity(detailIntent);
            } else {
                startActivity(detailIntent);
            }
        }
    }
}

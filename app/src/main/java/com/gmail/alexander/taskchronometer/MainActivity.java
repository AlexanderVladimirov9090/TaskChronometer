package com.gmail.alexander.taskchronometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.alexander.taskchronometer.activities.AddEditActivity;
import com.gmail.alexander.taskchronometer.domain_layer.Task;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //If landscape is on for tablets
    private boolean twoPane=false;

    private static final String ADD_EDIT_FRAGMENT= "AddEditFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDuration:
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

    private void taskEditRequest(Task task){
        Log.d(TAG, "taskEditRequest: ");
        if(twoPane){
            Log.d(TAG, "taskEditRequest: Two-pane mode");
        }else {
            Log.d(TAG, "taskEditRequest: single-pane mode");
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if (task != null){
                detailIntent.putExtra(Task.class.getName(),task);
                startActivity(detailIntent);
            }else {
                startActivity(detailIntent);
            }
        }
    }
}

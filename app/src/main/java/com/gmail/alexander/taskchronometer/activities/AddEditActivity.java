package com.gmail.alexander.taskchronometer.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.listeners.OnSaveListener;

/**
 * This class is used for  add and edit of the task actions.
 */
public class AddEditActivity extends AppCompatActivity implements OnSaveListener {
    public static final String TAG = "AddEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AddEditActivityFragment fragment = new AddEditActivityFragment();

        Bundle arguments = getIntent().getExtras();  // The line we'll change later
//        arguments.putSerializable(Task.class.getSimpleName(), task);
        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();

    }

    /**
     * Finishes the intent that is used to call this class.
     */
    @Override
    public void onSaveClicked() {
        finish();
    }
}

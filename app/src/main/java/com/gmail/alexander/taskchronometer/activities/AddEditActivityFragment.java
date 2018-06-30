package com.gmail.alexander.taskchronometer.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.contractors.TasksContract;
import com.gmail.alexander.taskchronometer.domain_layer.Task;
import com.gmail.alexander.taskchronometer.listeners.OnSaveListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";


    public enum FragmentEditMode {EDIT, ADD}

    private FragmentEditMode mode;

    private EditText nameText;

    private EditText descriptionText;
    private EditText sortOrderText;
    private OnSaveListener onSaveListener;

    public AddEditActivityFragment() {
    }

    /**
     * Initialize onSaveListener, if not provided correctly throws ClassCastException.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: Starts");
        super.onAttach(context);
        //Activities containing this fragment must implement it`s callback.
        Activity activity = getActivity();
        if (!(activity instanceof OnSaveListener)) {
            throw new ClassCastException(activity.getClass().getSimpleName() + " must be implemented OnClickListener interface.");
        }
        onSaveListener = (OnSaveListener) activity;

    }

    /**
     * Clear the reference onSavedListener.
     */
    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: Starts");
        super.onDetach();
        onSaveListener = null;
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    public boolean canClose() {
        return false;
    }

    /**
     * This is where the add edit task is done.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        nameText = view.findViewById(R.id.addedit_name);
        descriptionText = view.findViewById(R.id.addedit_description);
        sortOrderText = view.findViewById(R.id.addedit_sortorder);

        Button saveButton = view.findViewById(R.id.addedit_save);
        Bundle argument = getArguments();

        final Task task;

        if (argument != null) {

            task = (Task) argument.getSerializable(Task.class.getSimpleName());
            if (task != null) {

                nameText.setText(task.getName());
                descriptionText.setText(task.getDescription());
                sortOrderText.setText(String.valueOf(task.getSortOrder()));
                mode = FragmentEditMode.EDIT;
            } else {
                mode = FragmentEditMode.ADD;

            }
        } else {
            task = null;

            mode = FragmentEditMode.ADD;
        }

        //This is where the save button logic is.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int so;
                if (sortOrderText.length() > 0) {
                    so = Integer.parseInt(sortOrderText.getText().toString());
                } else {
                    so = 0;
                }

                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();

                switch (mode) {
                    case EDIT:
                        if (task == null) {
                            break;
                        }

                        if (!nameText.getText().toString().equals(task.getName())) {
                            values.put(TasksContract.Columns.TASKS_NAME, nameText.getText().toString());
                        }
                        if (!descriptionText.getText().toString().equals(task.getDescription())) {
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION, descriptionText.getText().toString());
                        }
                        if (so != task.getSortOrder()) {
                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
                        }
                        if (values.size() != 0) {
                            contentResolver.update(TasksContract.buildTaskUri(task.getId()), values, null, null);
                        }
                        break;
                    case ADD:
                        if (nameText.length() > 0) {
                            values.put(TasksContract.Columns.TASKS_NAME, nameText.getText().toString());
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION, descriptionText.getText().toString());
                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
                            contentResolver.insert(TasksContract.CONTENT_URI, values);

                        }
                        break;
                }
                if (onSaveListener != null) {
                    onSaveListener.onSaveClicked();
                }
            }

        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}

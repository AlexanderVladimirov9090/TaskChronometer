package com.gmail.alexander.taskchronometer.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.datatools.TasksContract;
import com.gmail.alexander.taskchronometer.domain_layer.Task;

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
    private Button saveButton;

    public AddEditActivityFragment() {
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

        nameText = (EditText) view.findViewById(R.id.addedit_name);
        descriptionText = (EditText) view.findViewById(R.id.addedit_description);
        sortOrderText = (EditText) view.findViewById(R.id.addedit_sortorder);
        saveButton = (Button) view.findViewById(R.id.addedit_save);

        Bundle argument = getActivity().getIntent().getExtras();
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
                    case ADD:
                        if (nameText.length() > 0) {
                            values.put(TasksContract.Columns.TASKS_NAME, nameText.getText().toString());
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION, descriptionText.getText().toString());
                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
                            contentResolver.insert(TasksContract.CONTENT_URI, values);

                        }
                        break;
                }
            }

        });
        return view;
    }
}

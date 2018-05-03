package com.gmail.alexander.taskchronometer.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gmail.alexander.taskchronometer.R;
import com.gmail.alexander.taskchronometer.domain_layer.Task;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";

    public enum FragmentEditMode {EDIT, ADD}

    private FragmentEditMode mode;

    private EditText nameText;
    private EditText descritpionText;
    private EditText sortOrderText;
    private Button saveButton;

    public AddEditActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Stats");
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);
        nameText = (EditText) view.findViewById(R.id.addedit_name);
        descritpionText = (EditText) view.findViewById(R.id.addedit_description);
        sortOrderText = (EditText) view.findViewById(R.id.addedit_sortorder);
        saveButton = (Button) view.findViewById(R.id.addedit_save);
        Bundle argument = getActivity().getIntent().getExtras();
        final Task task;
        if (argument != null) {
            Log.d(TAG, "onCreateView: Retrieving task details");
            task = (Task) argument.getSerializable(Task.class.getSimpleName());
            if (task != null) {
                Log.d(TAG, "onCreateView: Task details found editing...");
                nameText.setText(task.getName());
                descritpionText.setText(task.getDescription());
                sortOrderText.setText(Integer.toString(task.getSortOrder()));
                mode = FragmentEditMode.EDIT;
            }else {
                mode = FragmentEditMode.ADD;

            }
        }else {
            task = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record.");
            mode = FragmentEditMode.ADD;
        }
        return view;
    }
}

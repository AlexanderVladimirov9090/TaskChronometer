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

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";

    public enum FragmentEditMode {EDIT, ADD}
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
        View view = inflater.inflate(R.layout.fragment_add_edit,container,false);
        nameText = (EditText) view.findViewById(R.id.addedit_name);
        descritpionText = (EditText) view.findViewById(R.id.addedit_description);
        sortOrderText =  (EditText) view.findViewById(R.id.addedit_sortorder);
        saveButton = (Button) view.findViewById(R.id.addedit_save);

        return inflater.inflate(R.layout.fragment_add_edit, container, false);
    }
}

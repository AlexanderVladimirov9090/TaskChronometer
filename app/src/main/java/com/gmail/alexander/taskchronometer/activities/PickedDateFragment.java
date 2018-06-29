package com.gmail.alexander.taskchronometer.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class PickedDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DatePicker";

    public static final String DATE_PICKER_ID = "ID";
    public static final String DATE_PICKER_TITLE = "TITLE";
    public static final String DATE_PICKER_DATE = "DATE";
    int dialogId = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            final GregorianCalendar calendar = new GregorianCalendar();
            String title= null;
        Bundle arguments = getArguments();
        if(arguments!=null){
            dialogId = arguments.getInt(DATE_PICKER_ID);
            title = arguments.getString(DATE_PICKER_TITLE);

            Date givenDate = (Date) arguments.getSerializable(DATE_PICKER_DATE);
            if(givenDate !=null){
                calendar.setTime(givenDate);

            }
        }
        int year = calendar.get(GregorianCalendar.YEAR);
        int month = calendar.get(GregorianCalendar.MONTH);
        int day= calendar.get(GregorianCalendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),this,year,month,day);
        if(title!=null){
            datePickerDialog.setTitle(title);
        }
        return datePickerDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Activities using this dialog must implement its callbacks.
        if(!(context instanceof DatePickerDialog.OnDateSetListener)){
            throw new ClassCastException(context.toString()+ " must implement DatePickerDialog.OnDataSetListener interface.");
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
        if(listener!=null){
            //Notify calendar of user-selected values.
            view.setTag(dialogId);// pass the id back in the tag, to save caller storing their own copy.
            listener.onDateSet(view,year,month,dayOfMonth);

        }
    }
}

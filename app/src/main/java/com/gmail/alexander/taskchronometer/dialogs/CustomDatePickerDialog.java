package com.gmail.alexander.taskchronometer.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 */
public class CustomDatePickerDialog extends DatePickerDialog{

    public CustomDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context,callBack,year,monthOfYear,dayOfMonth);
    }

    @Override
    protected void onStop() {
        //Bug from old version of android.
    }
}

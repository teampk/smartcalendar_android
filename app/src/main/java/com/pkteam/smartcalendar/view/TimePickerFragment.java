package com.pkteam.smartcalendar.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private OnDialogDismissed mDialogDismissedCallback;

    public interface OnDialogDismissed {
        void onDialogDismissed(String whichSalutation);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDialogDismissedCallback = (OnDialogDismissed)activity;
        } catch (ClassCastException cce) {
            Log.e("Error", getClass().getSimpleName() + ", calling Activity must implement OnDialogDismissed");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Toast.makeText(getContext(), String.valueOf(hourOfDay)+":"+String.valueOf(minute), Toast.LENGTH_SHORT).show();
        if(getTag().equals("start")){
            mDialogDismissedCallback.onDialogDismissed(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+"/start");
        }else if(getTag().equals("end")){
            mDialogDismissedCallback.onDialogDismissed(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+"/end");
        }else{
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar mCalendar = Calendar.getInstance();
        /*
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);
        */
        int hour = 0;
        int min = 0;
        TimePickerDialog mTimePickerDialog = new TimePickerDialog(
                getContext(), this, hour, min, DateFormat.is24HourFormat(getContext())
        );
        return mTimePickerDialog;
    }
}

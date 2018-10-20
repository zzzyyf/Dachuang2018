package com.example.zyf.timetable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    interface SendDate{
        public void sendDate(int year, int month, int day, boolean isStart);
    }
    private SendDate listener;
    boolean isStart;
    Calendar c;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        listener.sendDate(year, month, day, isStart);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SendDate){
            listener = (SendDate)context;
        }else{
            throw new IllegalArgumentException("Activity must implement SendDate");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

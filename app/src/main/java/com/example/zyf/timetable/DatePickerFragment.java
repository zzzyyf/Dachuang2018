package com.example.zyf.timetable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    interface SendDate{
        public void sendDate(int year, int month, int day, boolean isStart);
    }
    private SendDate listener;
    boolean isStart;
    LocalDate c;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = c.getYear();
        int month = c.getMonthOfYear();
        int day = c.getDayOfMonth();
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month-1, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        listener.sendDate(year, month+1, day, isStart);
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

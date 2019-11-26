package com.klakier.proRobIntranet.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.TimesheetRow;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class TimesheetRowPickerDialogFragment extends DialogFragment {

    private DialogResultListener dialogResultListener;

    private TimesheetRow defVal;
    private TextView textViewDate;
    private TextView textViewFromValue;
    private TextView textViewToValue;
    private TextView textViewCustomerBreakValue;
    private TextView textViewStatutoryBreakValue;
    private Spinner spinnerProjectName;
    private EditText editTextComments;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialogFrom;
    private TimePickerDialog timePickerDialogTo;
    private TimePickerDialog timePickerDialogCustomerBreak;
    private TimePickerDialog timePickerDialogStatutoryBreak;
    private Calendar calendar;


    public TimesheetRowPickerDialogFragment() {
        calendar = Calendar.getInstance();
        Date today = Date.valueOf(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        Time timeFromDef = Time.valueOf("07:00:00");
        Time timeToDef = Time.valueOf("15:00:00");
        Time timeCustomerBreakDef = Time.valueOf("00:15:00");
        Time timeStatutoryBreakDef = Time.valueOf("00:15:00");
        defVal = new TimesheetRow(0, 0, today, timeFromDef, timeToDef, timeCustomerBreakDef, timeStatutoryBreakDef, null, 0, 0, false, null, null, "");
    }

    public static TimesheetRowPickerDialogFragment newInstance(TimesheetRow defVal, DialogResultListener listener) {
        TimesheetRowPickerDialogFragment fragment = new TimesheetRowPickerDialogFragment();
        fragment.setValues(defVal);
        fragment.setDiaglogResultListener(listener);
        return fragment;
    }

    public void setValues(TimesheetRow defVal) {
        this.defVal = defVal;
    }

    public void setDiaglogResultListener(DialogResultListener diaglogResultListener) {
        this.dialogResultListener = diaglogResultListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.timesheet_dialog, null);


        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogResultListener != null) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    if (defVal.getCreatedAt() == null)    //set timestamp only if is null, that mean object is new
                        defVal.setCreatedAt(timestamp);
                    defVal.setUpdatedAt(timestamp);
                    defVal.setUserId(new Token(getContext()).getId());
                    defVal.setComments(editTextComments.getText().toString());
                    dialogResultListener.onDialogResult(defVal);
                }
            }
        });
        builder.setNegativeButton("Anuluj", null);

        textViewDate = view.findViewById(R.id.textViewDialogDate);
        textViewFromValue = view.findViewById(R.id.textViewDialogFromValue);
        textViewToValue = view.findViewById(R.id.textViewDialogToValue);
        textViewCustomerBreakValue = view.findViewById(R.id.textViewDialogCustomerBreakValue);
        textViewStatutoryBreakValue = view.findViewById(R.id.textViewDialogStatutoryBreakValue);
        spinnerProjectName = view.findViewById(R.id.spinnerProjectName);
        editTextComments = view.findViewById(R.id.editTextComments);

        textViewDate.setText(defVal.getDate().toString());
        textViewFromValue.setText(defVal.getFrom().toString());
        textViewToValue.setText(defVal.getTo().toString());
        textViewCustomerBreakValue.setText(defVal.getCustomerBreak().toString());
        textViewStatutoryBreakValue.setText(defVal.getStatutoryBreak().toString());
        editTextComments.setText(defVal.getComments());

        //DATE
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date = Date.valueOf(year + "-" + (month + 1) + "-" + dayOfMonth);
                defVal.setDate(date);
                textViewDate.setText(date.toString());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //TIME FROM
        calendar.setTime(defVal.getFrom());
        timePickerDialogFrom = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                defVal.setFrom(time);
                textViewFromValue.setText(time.toString());
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        textViewFromValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogFrom.show();
            }
        });

        //TIME TO
        calendar.setTime(defVal.getTo());
        timePickerDialogTo = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                defVal.setTo(time);
                textViewToValue.setText(time.toString());
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        textViewToValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogTo.show();
            }
        });

        //TIME CUSTOMER BREAK
        calendar.setTime(defVal.getCustomerBreak());
        timePickerDialogCustomerBreak = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                defVal.setCustomerBreak(time);
                textViewCustomerBreakValue.setText(time.toString());
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        textViewCustomerBreakValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogCustomerBreak.show();
            }
        });

        //TIME STATUTORY BREAK
        calendar.setTime(defVal.getStatutoryBreak());
        timePickerDialogStatutoryBreak = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                defVal.setStatutoryBreak(time);
                textViewStatutoryBreakValue.setText(time.toString());
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        textViewStatutoryBreakValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogStatutoryBreak.show();
            }
        });

        return builder.create();
    }

    public interface DialogResultListener {
        void onDialogResult(TimesheetRow timesheetRow);
    }
}

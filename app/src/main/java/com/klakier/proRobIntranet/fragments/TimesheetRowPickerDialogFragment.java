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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.TimesheetRow;
import com.klakier.proRobIntranet.database.DBProRob;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimesheetRowPickerDialogFragment extends DialogFragment {

    /**
     * if true, doesn't return result if there is no changes
     */
    private Boolean updating = false;

    private DialogResultListener dialogResultListener;
    private TimesheetRow newTimesheetRow;
    private TimesheetRow oldTimesheetRow;
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
    private List<String> projects;

    public TimesheetRowPickerDialogFragment() {
        calendar = Calendar.getInstance();

        Date today = Date.valueOf(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        Time timeFromDef = Time.valueOf("07:00:00");
        Time timeToDef = Time.valueOf("15:00:00");
        Time timeCustomerBreakDef = Time.valueOf("00:15:00");
        Time timeStatutoryBreakDef = Time.valueOf("00:15:00");
        newTimesheetRow = new TimesheetRow(0, 0, today, timeFromDef, timeToDef, timeCustomerBreakDef, timeStatutoryBreakDef, null, 0, 0, false, null, null, null);

    }

    public static TimesheetRowPickerDialogFragment newInstance(TimesheetRow defVal, DialogResultListener listener) {
        TimesheetRowPickerDialogFragment fragment = new TimesheetRowPickerDialogFragment();
        fragment.setValues(defVal);
        fragment.setDialogResultListener(listener);
        return fragment;
    }

    public Boolean getUpdating() {
        return updating;
    }

    /**
     * If is true, doesn't return result if detect no changes
     *
     * @param updating boolean
     */
    public void setUpdating(Boolean updating) {
        this.updating = updating;
    }

    public void setValues(TimesheetRow defVal) {
        this.newTimesheetRow = defVal;
        try {
            this.oldTimesheetRow = (TimesheetRow) defVal.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void setDialogResultListener(DialogResultListener dialogResultListener) {
        this.dialogResultListener = dialogResultListener;
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
                    if (!newTimesheetRow.equals(oldTimesheetRow) || !updating) { //if something is changed

                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        if (newTimesheetRow.getCreatedAt() == null)    //set timestamp only if is null, that mean object is new
                            newTimesheetRow.setCreatedAt(timestamp);
                        newTimesheetRow.setUpdatedAt(timestamp);
                        if (newTimesheetRow.getUserId() == 0)
                            newTimesheetRow.setUserId(new Token(getContext()).getId()); //set userId only if is 0, that mean object is new

                        dialogResultListener.onDialogResult(newTimesheetRow);
                    }
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

        textViewDate.setText(newTimesheetRow.getDate().toString());
        textViewFromValue.setText(newTimesheetRow.getFrom().toString());
        textViewToValue.setText(newTimesheetRow.getTo().toString());
        textViewCustomerBreakValue.setText(newTimesheetRow.getCustomerBreak().toString());
        textViewStatutoryBreakValue.setText(newTimesheetRow.getStatutoryBreak().toString());
        editTextComments.setText(newTimesheetRow.getComments());

        projects = new ArrayList<String>();
        projects.add("");
        projects.addAll(new DBProRob(getContext(), null).readObjectives());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, projects);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //PROJECT SPINNER
        spinnerProjectName.setAdapter(spinnerAdapter);
        spinnerProjectName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (projects.get(position).equals(""))
                    newTimesheetRow.setProject(null);
                else
                    newTimesheetRow.setProject(projects.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int indexSpinner = projects.indexOf(newTimesheetRow.getProject());
        if (indexSpinner == -1)
            spinnerProjectName.setSelection(0);
        else
            spinnerProjectName.setSelection(indexSpinner);


        //COMMENTS
        editTextComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(""))
                    newTimesheetRow.setComments(null);
                else
                    newTimesheetRow.setComments(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //DATE
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date = Date.valueOf(year + "-" + (month + 1) + "-" + dayOfMonth);
                newTimesheetRow.setDate(date);
                textViewDate.setText(date.toString());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                // Hide day spinner
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                if (day != 0) {
                    View dayPicker = findViewById(day);
                    if (dayPicker != null) {
                        //dayPicker.setVisibility(View.GONE);
                    }
                }
            }
        };

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //TIME FROM
        calendar.setTime(newTimesheetRow.getFrom());
        timePickerDialogFrom = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                newTimesheetRow.setFrom(time);
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
        calendar.setTime(newTimesheetRow.getTo());
        timePickerDialogTo = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                newTimesheetRow.setTo(time);
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
        calendar.setTime(newTimesheetRow.getCustomerBreak());
        timePickerDialogCustomerBreak = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                newTimesheetRow.setCustomerBreak(time);
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
        calendar.setTime(newTimesheetRow.getStatutoryBreak());
        timePickerDialogStatutoryBreak = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                newTimesheetRow.setStatutoryBreak(time);
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

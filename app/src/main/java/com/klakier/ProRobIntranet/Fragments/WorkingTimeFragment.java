package com.klakier.ProRobIntranet.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.klakier.ProRobIntranet.Database.DBProRob;
import com.klakier.ProRobIntranet.R;
import com.klakier.ProRobIntranet.Responses.TimesheetRow;
import com.klakier.ProRobIntranet.TimeSheetViewAdapter;

import java.util.List;

public class WorkingTimeFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private TimeSheetViewAdapter timeSheetViewAdapter;
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton fab;

    public WorkingTimeFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_working_time, container, false);
        View view = inflater.inflate(R.layout.fragment_working_time, container, false);

        final List<TimesheetRow> ltsr = new DBProRob(context, null).readTimesheet();
        timeSheetViewAdapter = new TimeSheetViewAdapter(ltsr);
        recyclerView = view.findViewById(R.id.recyclerViewTimesheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(timeSheetViewAdapter);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar snackbar = Snackbar.make(view, "Jeszcze nie wymyśliłem do czego to sie przyda", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);

                // Change the text message color
                View mySbView = snackbar.getView();
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);

                // We can apply the property of TextView
                textView.setTextColor(getResources().getColor(R.color.secondaryTextColor));

                snackbar.show();*/

                TimesheetRowPickerDialogFragment dialog = new TimesheetRowPickerDialogFragment();
                dialog.setDiaglogResultListener(new TimesheetRowPickerDialogFragment.DialogResultListener() {
                    @Override
                    public void onDialogResult(TimesheetRow timesheetRow) {
                        Log.d("TEST", timesheetRow.getCreatedAt().toString());
                        new DBProRob(context, null).writeTimesheet(timesheetRow);
                        ltsr.add(timesheetRow);
                        timeSheetViewAdapter.notifyItemInserted(ltsr.size());
                    }
                });
                dialog.show(getFragmentManager(), "test");
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onAction(String action) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        this.context = null;
    }
}

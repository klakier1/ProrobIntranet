package com.klakier.proRobIntranet.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.klakier.proRobIntranet.Database.DBProRob;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Responses.TimesheetRow;
import com.klakier.proRobIntranet.TimeSheetViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkingTimeFragment extends Fragment implements TimeSheetViewAdapter.OnTimeSheetItemListener {

    private final List<TimesheetRow> mListTsr = new ArrayList<TimesheetRow>();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private TimeSheetViewAdapter mTimeSheetViewAdapter;
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton mFab;

    public WorkingTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_working_time, container, false);
        View view = inflater.inflate(R.layout.fragment_working_time, container, false);

        mListTsr.addAll(new DBProRob(mContext, null).readTimesheetWithoutMarkedForDelete());
        mTimeSheetViewAdapter = new TimeSheetViewAdapter(mListTsr, this);
        mRecyclerView = view.findViewById(R.id.recyclerViewTimesheet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mTimeSheetViewAdapter);

        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimesheetRowPickerDialogFragment dialog = new TimesheetRowPickerDialogFragment();
                dialog.setDiaglogResultListener(new TimesheetRowPickerDialogFragment.DialogResultListener() {
                    @Override
                    public void onDialogResult(TimesheetRow timesheetRow) {
                        long id = new DBProRob(mContext, null).writeTimesheet(timesheetRow);
                        if (id != -1) {
                            //if added to local DB without error, add also to adapter and notify
                            //set IDlocal if added to localDB
                            timesheetRow.setIdLocal((int) id);
                            mListTsr.add(timesheetRow);
                            mTimeSheetViewAdapter.notifyItemInserted(mListTsr.size());
                        }
                    }
                });
                dialog.show(getFragmentManager(), "ADD");
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
        this.mContext = context;
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
        this.mContext = null;
    }

    @Override
    public void onTimeSheetItemClick(final int position, View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.menu_timesheet_item);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.timesheet_item_action_remove: {
                        int idToRemove = mListTsr.get(position).getIdLocal();
                        DBProRob dbProRob = new DBProRob(mContext, null);

                        TimesheetRow timesheetRow = Stream.of(dbProRob.readTimesheet(String.valueOf(idToRemove), false)).findFirst().get();
                        int extId = timesheetRow.getIdExternal();

                        if (extId == 0) {
                            int deleteSize = dbProRob.deleteTimesheetRows(new String[]{String.valueOf(idToRemove)});
                            if (deleteSize == 1) //if number of deleted row equals 1, delete from adapter and notify
                            {
                                mListTsr.remove(position);
                                mTimeSheetViewAdapter.notifyItemRemoved(position);
                            }
                        } else if (extId > 0) {
                            timesheetRow.setIdExternal(extId * (-1));
                            int updateSize = dbProRob.updateTimesheetRow(timesheetRow, timesheetRow.getIdLocal().toString());
                            if (updateSize == 1) //if number of updated row equals 1, delete from adapter and notify
                            {
                                mListTsr.remove(position);
                                mTimeSheetViewAdapter.notifyItemRemoved(position);
                            }
                        }
                        break;
                    }
                    case R.id.timesheet_item_action_edit: {
                        TimesheetRowPickerDialogFragment dialog = new TimesheetRowPickerDialogFragment();
                        try {
                            dialog.setValues((TimesheetRow) mListTsr.get(position).clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        dialog.setDiaglogResultListener(new TimesheetRowPickerDialogFragment.DialogResultListener() {
                            @Override
                            public void onDialogResult(TimesheetRow timesheetRow) {
                                int updated = new DBProRob(mContext, null).updateTimesheetRow(timesheetRow, timesheetRow.getIdLocal().toString());
                                if (updated == 1) {
                                    mListTsr.set(position, timesheetRow);
                                    mTimeSheetViewAdapter.notifyItemChanged(position);
                                }
                            }
                        });
                        dialog.show(getFragmentManager(), "UPDATE");
                        break;
                    }
                    default: {
                        return false;
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }
}

package com.klakier.proRobIntranet.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.TimeSheetViewAdapter;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.call.GetObjectivesCall;
import com.klakier.proRobIntranet.api.call.OnResponseListener;
import com.klakier.proRobIntranet.api.response.ObjectivesResponse;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TimesheetRow;
import com.klakier.proRobIntranet.database.DBProRob;
import com.klakier.proRobIntranet.database.SyncTask;

import java.util.ArrayList;
import java.util.List;

public class WorkingTimeFragment extends Fragment implements TimeSheetViewAdapter.OnTimeSheetItemListener {

    public static final String DEF_VAL_TSR = "defValTsr";

    private final List<TimesheetRow> mListTsr = new ArrayList<TimesheetRow>();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private TimeSheetViewAdapter mTimeSheetViewAdapter;
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton mFab;

    public WorkingTimeFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_working_time_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync: {
                SyncTask syncTask = new SyncTask(mContext, new SyncTask.SyncTaskListener() {
                    @Override
                    public void onResult() {
                        refreshList();
                    }

                    @Override
                    public void onUpdate(SyncTask.UpdateState updateState) {
                        switch (updateState.getTag()) {
                            case SyncTask.PROGRESS_TAG_TOAST: {
                                Toast.makeText(mContext, updateState.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                });
                syncTask.execute();
                return true;
            }
            case R.id.action_get_objectives: {
                new GetObjectivesCall(mContext, new Token(mContext)).enqueue(new OnResponseListener() {
                    @Override
                    public void onSuccess(StandardResponse response) {
                        DBProRob dbProRob = new DBProRob(mContext, null);
                        long i = dbProRob.clearObjectives();
                        dbProRob.writeObjectives(((ObjectivesResponse) response).getData());
                        Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(StandardResponse response) {
                        Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void refreshList() {
        if (mTimeSheetViewAdapter != null) {
            mListTsr.clear();
            mListTsr.addAll(new DBProRob(mContext, null).readTimesheetWithoutMarkedForDelete());
            mTimeSheetViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_working_time, container, false);
        View view = inflater.inflate(R.layout.fragment_working_time, container, false);

        mListTsr.addAll(new DBProRob(mContext, null).readTimesheetWithoutMarkedForDelete(
//                Date.valueOf("2020-02-01"),
//                Date.valueOf("2020-02-10"),
//                "Z46 Webasto Schierling"
                )
        );
        mTimeSheetViewAdapter = new TimeSheetViewAdapter(getContext(), mListTsr, this);
        mRecyclerView = view.findViewById(R.id.recyclerViewTimesheet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mTimeSheetViewAdapter);

        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimesheetRowPickerDialogFragment dialog = new TimesheetRowPickerDialogFragment();
                dialog.setUpdating(false);
                dialog.setDialogResultListener(new TimesheetRowPickerDialogFragment.DialogResultListener() {
                    @Override
                    public void onDialogResult(final TimesheetRow timesheetRow) {
                        //check if tsr with that date exist in db
                        int count = new DBProRob(mContext, null).readTimesheet(timesheetRow.getDate()).size();
                        if (count != 0) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Yes button clicked
                                            addTsrToLocalDB(timesheetRow);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage(mContext.getString(R.string.double_tsr_msg))
                                    .setPositiveButton(mContext.getString(R.string.yes), dialogClickListener)
                                    .setNegativeButton(mContext.getString(R.string.no), dialogClickListener)
                                    .show();
                        } else {
                            addTsrToLocalDB(timesheetRow);
                        }

                    }
                });
                //set default values
                SharedPreferences sharedPref = mContext.getSharedPreferences("PROROB", Context.MODE_PRIVATE);
                long id = sharedPref.getLong(DEF_VAL_TSR, 0);
                if (id != 0) {
                    List<TimesheetRow> listDefVal = new DBProRob(mContext, null).readTimesheet(id, false);
                    if (listDefVal.size() == 1)
                        dialog.setValues(listDefVal.get(0));
                }
                //show dialog
                dialog.show(getFragmentManager(), "ADD");
            }
        });
        return view;
    }

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
    public void onTimeSheetItemClick(final int adapterPosition, final int contentPosition, View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.menu_timesheet_item);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.timesheet_item_action_remove: {
                        int idToRemove = mListTsr.get(contentPosition).getIdLocal();
                        DBProRob dbProRob = new DBProRob(mContext, null);

                        TimesheetRow timesheetRow = Stream.of(dbProRob.readTimesheet(idToRemove, false)).findFirst().get();
                        int extId = timesheetRow.getIdExternal();

                        if (extId == 0) {
                            int deleteSize = dbProRob.deleteTimesheetRow(idToRemove);
                            if (deleteSize == 1) //if number of deleted row equals 1, delete from adapter and notify
                            {
                                mListTsr.remove(contentPosition);
                                mTimeSheetViewAdapter.notifyItemRemoved(adapterPosition);
                            }
                        } else if (extId > 0) {
                            timesheetRow.setIdExternal(extId * (-1));
                            int updateSize = dbProRob.updateTimesheetRow(timesheetRow);
                            if (updateSize == 1) //if number of updated row equals 1, delete from adapter and notify
                            {
                                mListTsr.remove(contentPosition);
                                mTimeSheetViewAdapter.notifyItemRemoved(adapterPosition);
                            }
                        }
                        break;
                    }
                    case R.id.timesheet_item_action_edit: {
                        TimesheetRowPickerDialogFragment dialog = new TimesheetRowPickerDialogFragment();
                        dialog.setUpdating(true);
                        dialog.setDialogResultListener(new TimesheetRowPickerDialogFragment.DialogResultListener() {
                            @Override
                            public void onDialogResult(TimesheetRow timesheetRow) {
                                int updated = new DBProRob(mContext, null).updateTimesheetRow(timesheetRow);
                                if (updated == 1) {
                                    mListTsr.set(contentPosition, timesheetRow);
                                    mTimeSheetViewAdapter.notifyItemChanged(adapterPosition);
                                }
                            }
                        });
                        try {
                            dialog.setValues((TimesheetRow) mListTsr.get(contentPosition).clone());
                            int idUpdate = mListTsr.get(contentPosition).getIdLocal();
                            dialog.setValues(new DBProRob(mContext, null).readTimesheet(idUpdate, false).get(0));
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
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

    public boolean addTsrToLocalDB(TimesheetRow timesheetRow) {
        long id = new DBProRob(mContext, null).writeTimesheet(timesheetRow);
        if (id != -1) {
            //if added to local DB without error, add also to adapter and notify
            //set ID local if added to localDB
            timesheetRow.setIdLocal((int) id);
            mListTsr.add(timesheetRow);
            mTimeSheetViewAdapter.notifyItemInserted(mListTsr.size());

            //add if to shared preferences, to make it default next time
            SharedPreferences sharedPref = mContext.getSharedPreferences("PROROB", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(DEF_VAL_TSR, id);
            editor.apply();
            return true;
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.toast_error_add_to_loc_db), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

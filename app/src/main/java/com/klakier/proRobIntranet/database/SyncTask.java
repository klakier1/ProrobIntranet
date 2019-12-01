package com.klakier.proRobIntranet.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Predicate;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.call.DeleteTimesheetRowCall;
import com.klakier.proRobIntranet.api.call.GetTimesheetCall;
import com.klakier.proRobIntranet.api.call.InsertTimesheetRowCall;
import com.klakier.proRobIntranet.api.call.UpdateTimesheetCall;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TimesheetResponse;
import com.klakier.proRobIntranet.api.response.TimesheetRow;
import com.klakier.proRobIntranet.api.response.TimesheetRowInsertedResponse;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SyncTask extends AsyncTask<Void, Void, Void> {

    private static final String DB_SYNC = "dbOpsSync";
    private static final String DB_SYNC_ERROR = "dbOpsSyncError";

    private Context mContext;
    private ProgressDialog dialog;

    public SyncTask(Context context) {
        super();
        mContext = context;
        dialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Synchronizacja baz danych");
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Token token = new Token(mContext);
        final DBProRob dbProRob = new DBProRob(mContext, null);

        GetTimesheetCall getTimesheetCall = new GetTimesheetCall(mContext, new Token(mContext));
        StandardResponse standardResponse = getTimesheetCall.execute();
        TimesheetResponse timesheetResponse;
        if (standardResponse instanceof TimesheetResponse)
            timesheetResponse = (TimesheetResponse) standardResponse;
        else {
            Log.d(DB_SYNC, standardResponse.toString());
            //Toast.makeText(mContext, standardResponse.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }

        final List<TimesheetRow> tsrExt = timesheetResponse.getData();
        final List<TimesheetRow> tsrLoc = dbProRob.readTimesheet();

        final List<TimesheetRow> addToLoc = new ArrayList<TimesheetRow>();
        final List<TimesheetRow> addToExt = new ArrayList<TimesheetRow>();
        final List<TimesheetRow> deleteFromLoc = new ArrayList<TimesheetRow>();
        final List<TimesheetRow> deleteFromExt = new ArrayList<TimesheetRow>();
        final List<TimesheetRow> newerInLoc = new ArrayList<TimesheetRow>();
        final List<TimesheetRow> newerInExt = new ArrayList<TimesheetRow>();
        final List<TimesheetRow> equals = new ArrayList<TimesheetRow>();
        final List<TimesheetRow> notEquals = new ArrayList<TimesheetRow>();

        Stream.of(tsrExt).forEach(new Consumer<TimesheetRow>() {
            @Override
            public void accept(final TimesheetRow e) {
                TimesheetRow loc = Stream.of(tsrLoc).filter(new Predicate<TimesheetRow>() {
                    @Override
                    public boolean test(TimesheetRow l) {
                        return e.getIdExternal().equals(abs(l.getIdExternal()));
                    }
                }).findFirst().orElse(null);

                if (loc != null) {
                    if (e.getIdExternal().equals(loc.getIdExternal())) {
                        int compare = loc.getUpdatedAt().compareTo(e.getUpdatedAt());
                        if (compare == 0) {
                            if (loc.hashCode() == e.hashCode())
                                equals.add(loc);
                            else
                                notEquals.add(loc);
                        } else if (compare < 0) {
                            e.setIdLocal(loc.getIdLocal());
                            newerInExt.add(e);
                        } else {
                            newerInLoc.add(loc);
                        }
                    } else {
                        deleteFromExt.add(loc);
                        //deleteFromLoc.add(loc);
                    }
                } else {
                    addToLoc.add(e);
                }
            }
        });

        Stream.of(tsrLoc).forEach(new Consumer<TimesheetRow>() {
            @Override
            public void accept(final TimesheetRow l) {
                if (l.getIdExternal() == 0) {
                    addToExt.add(l);
                } else if (Stream.of(tsrExt).noneMatch(new Predicate<TimesheetRow>() {
                    @Override
                    public boolean test(TimesheetRow e) {
                        return e.getIdExternal().equals(abs(l.getIdExternal()));
                    }
                })) {
                    deleteFromLoc.add(l);
                }
            }
        });

        //print summary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(DB_SYNC, "AddToLoc:" + addToLoc.size());
            addToLoc.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
            Log.d(DB_SYNC, "AddToExt:" + addToExt.size());
            addToExt.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
            Log.d(DB_SYNC, "DeleteFromLoc:" + deleteFromLoc.size());
            deleteFromLoc.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
            Log.d(DB_SYNC, "DeleteFromExt:" + deleteFromExt.size());
            deleteFromExt.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
            Log.d(DB_SYNC, "NewerInLoc:" + newerInLoc.size());
            newerInLoc.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
            Log.d(DB_SYNC, "NewerInExt:" + newerInExt.size());
            newerInExt.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
            Log.d(DB_SYNC, "Equals:" + equals.size());
            equals.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
            Log.d(DB_SYNC, "NotEquals:" + notEquals.size());
            notEquals.forEach(new java.util.function.Consumer<TimesheetRow>() {
                @Override
                public void accept(TimesheetRow l) {
                    Log.d(DB_SYNC, l.toString());
                }
            });
        }

        //ADD LOC
        dbProRob.writeTimesheets(addToLoc);

        //ADD EXT
        for (TimesheetRow ae : addToExt) {
            //call
            InsertTimesheetRowCall insertTimesheetRowCall = new InsertTimesheetRowCall(mContext, token, ae);
            StandardResponse standardResponseITRC = insertTimesheetRowCall.execute();
            TimesheetRowInsertedResponse timesheetRowInsertedResponse;
            //if call is correct instance means response is successful
            if (standardResponseITRC instanceof TimesheetRowInsertedResponse) {
                timesheetRowInsertedResponse = (TimesheetRowInsertedResponse) standardResponseITRC;
                //set id external for response
                ae.setIdExternal(timesheetRowInsertedResponse.getId());
                //update tsr with id external in locDB
                dbProRob.updateTimesheetRow(ae);
            } else {
                Log.d(DB_SYNC_ERROR, ae.toString() + " " + standardResponseITRC.toString());
                continue;
            }
        }

        //DEL LOC
        dbProRob.deleteTimesheetRows(deleteFromLoc);

        //DEL EXT
        for (TimesheetRow de : deleteFromExt) {
            DeleteTimesheetRowCall deleteTimesheetRowCall = new DeleteTimesheetRowCall(mContext, token, abs(de.getIdExternal()));
            StandardResponse standardResponseDTRC = deleteTimesheetRowCall.execute();
            if (!standardResponseDTRC.getError()) {
                //delete from locDB
                dbProRob.deleteTimesheetRow(de.getIdLocal());
            } else {
                Log.d(DB_SYNC_ERROR, de.toString() + " " + standardResponseDTRC.toString());
                continue;
            }
        }

        //UPDATE LOC
        dbProRob.updateTimesheetRows(newerInExt);

        //UPDATE EXT
        for (TimesheetRow nl : newerInLoc) {
            UpdateTimesheetCall updateTimesheetCall = new UpdateTimesheetCall(mContext, token, nl);
            StandardResponse standardResponseUTC = updateTimesheetCall.execute();
            if (!standardResponseUTC.getError()) {
            } else {
                Log.d(DB_SYNC_ERROR, nl.toString() + " " + standardResponseUTC.toString());
                continue;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

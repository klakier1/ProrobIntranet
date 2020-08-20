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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SyncTask extends AsyncTask<Void, SyncTask.UpdateState, Void> {

  public static final String PROGRESS_TAG_TOAST = "prograssTagToast";
  private static final String DB_SYNC = "dbOpsSync";
  private static final String DB_SYNC_ERROR = "dbOpsSyncError";
  private final WeakReference<Context> mWeakContext;
  private final WeakReference<Context> mWeakApplicationContext;
  private ProgressDialog mDialog;
  private SyncTaskListener mSyncTaskListener;


  public SyncTask(Context context, SyncTaskListener syncTaskListener) {
    super();
    mWeakContext = new WeakReference<>(context);
    mWeakApplicationContext = new WeakReference<>(context.getApplicationContext());
    mDialog = new ProgressDialog(context);
    mSyncTaskListener = syncTaskListener;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    mDialog.setMessage("Synchronizacja baz danych");
    mDialog.setIndeterminate(false);
    mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    mDialog.setCancelable(false);
    mDialog.show();
  }

  @Override
  protected Void doInBackground(Void... voids) {

    final Token token;
    final DBProRob dbProRob;
    Context applicationContext = mWeakApplicationContext.get();

    if (applicationContext != null) {
      token = new Token(applicationContext);
      dbProRob = new DBProRob(applicationContext, null);
    } else return null;

    GetTimesheetCall getTimesheetCall;
    if (applicationContext != null) {
      getTimesheetCall = new GetTimesheetCall(applicationContext, token);
    } else return null;
    StandardResponse standardResponse = getTimesheetCall.execute();
    TimesheetResponse timesheetResponse;
    if (standardResponse instanceof TimesheetResponse && !standardResponse.getError())
      timesheetResponse = (TimesheetResponse) standardResponse;
    else {
      Log.d(DB_SYNC, standardResponse.toString());
      publishProgress(new UpdateState(PROGRESS_TAG_TOAST, standardResponse.getMessage()));
      return null;
    }

    final List<TimesheetRow> tsrExt = new ArrayList<>();
    if (timesheetResponse.getDataLength() > 0)
      tsrExt.addAll(timesheetResponse.getData());
    final List<TimesheetRow> tsrLoc = dbProRob.readTimesheet();


    final List<TimesheetRow> addToLoc = new ArrayList<>();
    final List<TimesheetRow> addToExt = new ArrayList<>();
    final List<TimesheetRow> deleteFromLoc = new ArrayList<>();
    final List<TimesheetRow> deleteFromExt = new ArrayList<>();
    final List<TimesheetRow> newerInLoc = new ArrayList<>();
    final List<TimesheetRow> newerInExt = new ArrayList<>();
    final List<TimesheetRow> equals = new ArrayList<>();
    final List<TimesheetRow> notEquals = new ArrayList<>();

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
      Log.d(DB_SYNC, " ");
      Log.d(DB_SYNC, " ******************  COMPARE SUMMARY ******************************");
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
    long retAddToLoc = dbProRob.writeTimesheets(addToLoc);

    //ADD EXT
    long retAddToExt = 0;
    for (TimesheetRow ae : addToExt) {
      //call
      InsertTimesheetRowCall insertTimesheetRowCall;
      if (applicationContext != null) {
        insertTimesheetRowCall = new InsertTimesheetRowCall(applicationContext, token, ae);
      } else return null;
      StandardResponse standardResponseITRC = insertTimesheetRowCall.execute();
      TimesheetRowInsertedResponse timesheetRowInsertedResponse;
      //if call is correct instance means response is successful
      if (standardResponseITRC instanceof TimesheetRowInsertedResponse) {
        timesheetRowInsertedResponse = (TimesheetRowInsertedResponse) standardResponseITRC;
        //set id external for response
        ae.setIdExternal(timesheetRowInsertedResponse.getId());
        //update tsr with id external in locDB
        retAddToExt += dbProRob.updateTimesheetRow(ae);
      } else {
        Log.d(DB_SYNC_ERROR, ae.toString() + " " + standardResponseITRC.toString());
      }
    }

    //DEL LOC
    long retDelFromLoc = dbProRob.deleteTimesheetRows(deleteFromLoc);

    //DEL EXT
    long retDelFromExt = 0;
    for (TimesheetRow de : deleteFromExt) {
      DeleteTimesheetRowCall deleteTimesheetRowCall;
      if (applicationContext != null) {
        deleteTimesheetRowCall = new DeleteTimesheetRowCall(applicationContext, token, abs(de.getIdExternal()));
      } else return null;
      StandardResponse standardResponseDTRC = deleteTimesheetRowCall.execute();
      if (!standardResponseDTRC.getError()) {
        //delete from locDB
        retDelFromExt += dbProRob.deleteTimesheetRow(de.getIdLocal());
      } else {
        Log.d(DB_SYNC_ERROR, de.toString() + " " + standardResponseDTRC.toString());
      }
    }

    //UPDATE LOC
    long retNewerInExt = dbProRob.updateTimesheetRows(newerInExt);

    //UPDATE EXT
    long retNewerInLoc = 0;
    for (TimesheetRow nl : newerInLoc) {
      UpdateTimesheetCall updateTimesheetCall;
      if (applicationContext != null) {
        updateTimesheetCall = new UpdateTimesheetCall(applicationContext, token, nl);
      } else return null;
      StandardResponse standardResponseUTC = updateTimesheetCall.execute();
      if (!standardResponseUTC.getError()) {
        retNewerInLoc++;
      } else {
        Log.d(DB_SYNC_ERROR, nl.toString() + " " + standardResponseUTC.toString());
      }
    }

    //Sync summary
    Log.d(DB_SYNC, " ******************  SYNC SUMMARY *********************************");
    Log.d(DB_SYNC, "List addToLoc size:      " + addToLoc.size() + " -> " + retAddToLoc);
    Log.d(DB_SYNC, "List addToExt size:      " + addToExt.size() + " -> " + retAddToExt);
    Log.d(DB_SYNC, "List deleteFromLoc size: " + deleteFromLoc.size() + " -> " + retDelFromLoc);
    Log.d(DB_SYNC, "List deleteFromExt size: " + deleteFromExt.size() + " -> " + retDelFromExt);
    Log.d(DB_SYNC, "List newerInExt size:    " + newerInExt.size() + " -> " + retNewerInExt);
    Log.d(DB_SYNC, "List newerInLoc size:    " + newerInLoc.size() + " -> " + retNewerInLoc);

    String syncSummary = "Rezultat synchronizacji baz danych\n" +
            "\n" +
            "Dodać do lokalnej DB: " + addToLoc.size() + " -> " + retAddToLoc + "\n" +
            "Dodać do zewnętrznej DB: " + addToExt.size() + " -> " + retAddToExt + "\n" +
            "Usunąć z lokalnej DB: " + deleteFromLoc.size() + " -> " + retDelFromLoc + "\n" +
            "Usunąć z zewnętrznej DB: " + deleteFromExt.size() + " -> " + retDelFromExt + "\n" +
            "Zaktualizować w lokalnej DB: " + newerInExt.size() + " -> " + retNewerInExt + "\n" +
            "Zaktualizować w zewnętrznej DB: " + newerInLoc.size() + " -> " + retNewerInLoc + "\n" +
            "Wpisy identyczne: " + equals.size() + "\n" +
            "Konflikty: " + notEquals.size() + "\n";
    publishProgress(new UpdateState(PROGRESS_TAG_TOAST, syncSummary));

    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
    mSyncTaskListener.onResult();
    mDialog.dismiss();
  }

  @Override
  protected void onProgressUpdate(SyncTask.UpdateState... values) {
    super.onProgressUpdate(values);
    mSyncTaskListener.onUpdate(values[0]);
  }

  public interface SyncTaskListener {
    void onResult();

    void onUpdate(SyncTask.UpdateState updateState);
  }

  public class UpdateState {

    private String tag;
    private String message;

    private UpdateState(String tag, String message) {
      this.tag = tag;
      this.message = message;
    }

    public String getTag() {
      return tag;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}

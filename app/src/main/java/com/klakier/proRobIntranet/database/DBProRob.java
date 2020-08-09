package com.klakier.proRobIntranet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.TimesheetRow;
import com.klakier.proRobIntranet.api.response.UserDataShort;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DBProRob extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "prorob.db";
    //tables names
    private static final String TABLE_CURRENT_USER = "currentUser";
    private static final String TABLE_TIMESHEET = "timesheet";
    private static final String TABLE_OBJECTIVES = "objectives";
    private static final String TABLE_TEAM = "team";
    //column names currentUser and team
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_AVATAR_FILE_NAME = "avatarFileName";
    private static final String COL_AVATAR_CONTENT_TYPE = "avatarContentType";
    private static final String COL_AVATAR_FILE_SIZE = "avatarFileSize";
    private static final String COL_ROLE = "role";
    private static final String COL_ACTIVE = "active";
    private static final String COL_FIRST_NAME = "firstName";
    private static final String COL_LAST_NAME = "lastName";
    private static final String COL_TITLE = "title";
    private static final String COL_PHONE = "phone";
    //column names timesheet
    private static final String COL_ID_LOCAL = "id_local";
    private static final String COL_ID_EXTERNAL = "id_external";
    private static final String COL_ID_USER = "id_user";
    private static final String COL_DATE = "date";
    private static final String COL_FROM = "from_hour";
    private static final String COL_TO = "to_hour";
    private static final String COL_CUSTOMER_BREAK = "customer_break";
    private static final String COL_STATUTORY_BREAK = "statutory_break";
    private static final String COL_COMMENTS = "comments";
    private static final String COL_ID_PROJECT = "id_project";
    private static final String COL_ID_COMPANY = "id_company";
    private static final String COL_STATUS = "status";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";
    private static final String COL_PROJECT = "project";
    //column name objectives
    //private static final String COL_ID = "id";
    private static final String COL_OBJECTIVE = "objective";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_CURRENT_USER + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            COL_EMAIL + " TEXT," +
            COL_AVATAR_FILE_NAME + " TEXT," +
            COL_AVATAR_CONTENT_TYPE + " TEXT," +
            COL_AVATAR_FILE_SIZE + " INTEGER," +
            COL_ROLE + " TEXT," +
            COL_ACTIVE + " BOOLEAN," +
            COL_FIRST_NAME + " TEXT," +
            COL_LAST_NAME + " TEXT," +
            COL_TITLE + " TEXT," +
            COL_PHONE + " TEXT" +
            ");";
    private static final String CREATE_TIMESHEET_TABLE = "CREATE TABLE " + TABLE_TIMESHEET + "(" +
            COL_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            COL_ID_EXTERNAL + " INTEGER," +
            COL_ID_USER + " INTEGER," +
            COL_DATE + " TEXT," +
            COL_FROM + " TEXT," +
            COL_TO + " TEXT," +
            COL_CUSTOMER_BREAK + " TEXT," +
            COL_STATUTORY_BREAK + " TEXT," +
            COL_COMMENTS + " TEXT," +
            COL_ID_PROJECT + " INTEGER," +
            COL_ID_COMPANY + " INTEGER," +
            COL_STATUS + " BOOLEAN," +
            COL_CREATED_AT + " TEXT," +
            COL_UPDATED_AT + " TEXT," +
            COL_PROJECT + " TEXT" +
            ");";
    private static final String CREATE_OBJECTIVES_TABLE = "CREATE TABLE " + TABLE_OBJECTIVES + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            COL_OBJECTIVE + " TEXT" +
            ");";
    private static final String CREATE_TEAM_TABLE = "CREATE TABLE " + TABLE_TEAM + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            COL_EMAIL + " TEXT," +
            COL_AVATAR_FILE_NAME + " TEXT," +
            COL_AVATAR_CONTENT_TYPE + " TEXT," +
            COL_AVATAR_FILE_SIZE + " INTEGER," +
            COL_ROLE + " TEXT," +
            COL_ACTIVE + " BOOLEAN," +
            COL_FIRST_NAME + " TEXT," +
            COL_LAST_NAME + " TEXT," +
            COL_TITLE + " TEXT," +
            COL_PHONE + " TEXT" +
            ");";
    private Context mContext;

    public DBProRob(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TIMESHEET_TABLE);
        db.execSQL(CREATE_OBJECTIVES_TABLE);
        db.execSQL(CREATE_TEAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_USER + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMESHEET + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJECTIVES + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM + ";");
        onCreate(db);
    }

    //region USER TABLE FUNCTIONS ******************************************
    public void resetUser() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CURRENT_USER, null, null);
        db.close();
    }

    public UserDataShort getUser() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CURRENT_USER + " LIMIT 1";

        Cursor c = db.rawQuery(query, null);
        UserDataShort user = null;

        if (c.getCount() == 1) {
            c.moveToFirst();
            user = new UserDataShort(
                    c.getString(c.getColumnIndex(COL_EMAIL)),
                    c.getString(c.getColumnIndex(COL_AVATAR_FILE_NAME)),
                    c.getString(c.getColumnIndex(COL_AVATAR_CONTENT_TYPE)),
                    c.getInt(c.getColumnIndex(COL_AVATAR_FILE_SIZE)),
                    c.getString(c.getColumnIndex(COL_ROLE)),
                    c.getInt(c.getColumnIndex(COL_ACTIVE)) == 1,
                    c.getString(c.getColumnIndex(COL_FIRST_NAME)),
                    c.getString(c.getColumnIndex(COL_LAST_NAME)),
                    c.getString(c.getColumnIndex(COL_TITLE)),
                    c.getString(c.getColumnIndex(COL_PHONE))
            );
        }
        db.close();
        c.close();
        return user;
    }

    public void setUser(UserDataShort user) {
        resetUser();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMAIL, user.getEmail());
        contentValues.put(COL_AVATAR_FILE_NAME, user.getAvatarFileSize());
        contentValues.put(COL_AVATAR_CONTENT_TYPE, user.getAvatarContentType());
        contentValues.put(COL_AVATAR_FILE_SIZE, user.getAvatarFileSize());
        contentValues.put(COL_ROLE, user.getRole());
        contentValues.put(COL_ACTIVE, user.getActive());
        contentValues.put(COL_FIRST_NAME, user.getFirstName());
        contentValues.put(COL_LAST_NAME, user.getLastName());
        contentValues.put(COL_TITLE, user.getTitle());
        contentValues.put(COL_PHONE, user.getPhone());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CURRENT_USER, null, contentValues);
        db.close();
    }

    public boolean hasUser() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CURRENT_USER + " LIMIT 1";

        Cursor c = db.rawQuery(query, null);
        boolean ret = (c.getCount() == 1);

        db.close();
        c.close();
        return ret;
    }
    // endregion

    // region TIMESHEET TABLE FUNCTIONS *************************************
    public long writeTimesheet(TimesheetRow tsr) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_EXTERNAL, tsr.getIdExternal());
        contentValues.put(COL_ID_USER, tsr.getUserId());
        contentValues.put(COL_DATE, tsr.getDate().toString());
        contentValues.put(COL_FROM, tsr.getFrom().toString());
        contentValues.put(COL_TO, tsr.getTo().toString());
        contentValues.put(COL_CUSTOMER_BREAK, tsr.getCustomerBreak().toString());
        contentValues.put(COL_STATUTORY_BREAK, tsr.getStatutoryBreak().toString());
        contentValues.put(COL_COMMENTS, tsr.getComments());
        contentValues.put(COL_ID_PROJECT, tsr.getProjectId());
        contentValues.put(COL_ID_COMPANY, tsr.getCompanyId());
        contentValues.put(COL_STATUS, tsr.getStatus());
        contentValues.put(COL_CREATED_AT, tsr.getCreatedAt().toString());
        contentValues.put(COL_UPDATED_AT, tsr.getUpdatedAt().toString());
        contentValues.put(COL_PROJECT, tsr.getProject());

        SQLiteDatabase db = getWritableDatabase();
        long ret = db.insert(TABLE_TIMESHEET, null, contentValues);
        db.close();
        return ret;
    }

    public long writeTimesheets(List<TimesheetRow> ltsr) {
        long ret = 0;
        for (TimesheetRow tsr : ltsr) {
            //check in row with do
            if (writeTimesheet(tsr) != -1)
                ret++;
            else
                return -1;
        }
        return ret;
    }

    private TimesheetRow getTimesheetRowFromCursor(Cursor c) {
        TimesheetRow tsr = new TimesheetRow(
                c.getInt(c.getColumnIndex(COL_ID_EXTERNAL)),
                c.getInt(c.getColumnIndex(COL_ID_USER)),
                Date.valueOf(c.getString(c.getColumnIndex(COL_DATE))),
                Time.valueOf(c.getString(c.getColumnIndex(COL_FROM))),
                Time.valueOf(c.getString(c.getColumnIndex(COL_TO))),
                Time.valueOf(c.getString(c.getColumnIndex(COL_CUSTOMER_BREAK))),
                Time.valueOf(c.getString(c.getColumnIndex(COL_STATUTORY_BREAK))),
                c.getString(c.getColumnIndex(COL_COMMENTS)),
                c.getInt(c.getColumnIndex(COL_ID_PROJECT)),
                c.getInt(c.getColumnIndex(COL_ID_COMPANY)),
                c.getInt(c.getColumnIndex(COL_STATUS)) == 1,
                Timestamp.valueOf(c.getString(c.getColumnIndex(COL_CREATED_AT))),
                Timestamp.valueOf(c.getString(c.getColumnIndex(COL_UPDATED_AT))),
                c.getString(c.getColumnIndex(COL_PROJECT))
        );
        tsr.setIdLocal(c.getInt(c.getColumnIndex(COL_ID_LOCAL)));
        return tsr;
    }

    public List<TimesheetRow> readTimesheet() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMESHEET
                + " WHERE " + COL_ID_USER + "=" + new Token(mContext).getId()
                + " ORDER BY " + COL_DATE;

        Cursor c = db.rawQuery(query, null);
        List<TimesheetRow> ltsr = new ArrayList<TimesheetRow>();

        while (c.moveToNext()) {
            ltsr.add(getTimesheetRowFromCursor(c));
        }

        db.close();
        c.close();

        return ltsr;
    }

    public List<TimesheetRow> readTimesheetWithoutMarkedForDelete() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMESHEET
                + " WHERE " + COL_ID_USER + "=" + new Token(mContext).getId()
                + " AND " + COL_ID_EXTERNAL + ">=" + 0
                + " ORDER BY " + COL_DATE;

        Cursor c = db.rawQuery(query, null);
        List<TimesheetRow> ltsr = new ArrayList<TimesheetRow>();

        while (c.moveToNext()) {
            ltsr.add(getTimesheetRowFromCursor(c));
        }

        db.close();
        c.close();

        return ltsr;
    }

    public List<TimesheetRow> readTimesheetWithoutMarkedForDelete(Date from, Date to, String projectName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMESHEET
                + " WHERE " + COL_ID_USER + "=" + new Token(mContext).getId()
                + " AND " + COL_ID_EXTERNAL + ">=" + 0;

        if (from != null && to != null)
            query += " AND " + COL_DATE + ">=" + "'" + from.toString() + "'"
                    + " AND " + COL_DATE + "<=" + "'" + to.toString() + "'";

        if (projectName != null)
            query += " AND " + COL_PROJECT + "=" + "'" + projectName + "'";

        query += " ORDER BY " + COL_DATE;

        Cursor c = db.rawQuery(query, null);
        List<TimesheetRow> ltsr = new ArrayList<TimesheetRow>();

        while (c.moveToNext()) {
            ltsr.add(getTimesheetRowFromCursor(c));
        }

        db.close();
        c.close();

        return ltsr;
    }

    public List<TimesheetRow> readTimesheet(long id, boolean idExternal) {
        SQLiteDatabase db = getReadableDatabase();
        String columnId = idExternal ? COL_ID_EXTERNAL : COL_ID_LOCAL;
        String query = "SELECT * FROM " + TABLE_TIMESHEET + " WHERE "
                + COL_ID_USER + "=" + new Token(mContext).getId() + " AND "
                + columnId + "=" + id;

        Cursor c = db.rawQuery(query, null);
        List<TimesheetRow> ltsr = new ArrayList<TimesheetRow>();

        while (c.moveToNext()) {
            ltsr.add(getTimesheetRowFromCursor(c));
        }

        db.close();
        c.close();

        return ltsr;
    }

    public List<TimesheetRow> readTimesheet(Date date) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMESHEET + " WHERE "
                + COL_ID_USER + "=" + new Token(mContext).getId() + " AND "
                + COL_DATE + "=" + "\"" + date.toString() + "\"";

        Cursor c = db.rawQuery(query, null);
        List<TimesheetRow> ltsr = new ArrayList<TimesheetRow>();

        while (c.moveToNext()) {
            ltsr.add(getTimesheetRowFromCursor(c));
        }

        db.close();
        c.close();

        return ltsr;
    }

    public List<TimesheetRow> readTimesheetMarkedDelete() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMESHEET + " WHERE "
                + COL_ID_USER + "=" + new Token(mContext).getId() + " AND "
                + COL_ID_EXTERNAL + "<0";

        Cursor c = db.rawQuery(query, null);
        List<TimesheetRow> ltsr = new ArrayList<TimesheetRow>();

        while (c.moveToNext()) {
            ltsr.add(getTimesheetRowFromCursor(c));
        }

        db.close();
        c.close();

        return ltsr;
    }

    public boolean hasTimeSheetRowFromExternalDb(String idExternal) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMESHEET + " WHERE "
                + COL_ID_USER + "=" + new Token(mContext).getId() + " AND " +
                COL_ID_EXTERNAL + "=" + idExternal;

        Cursor c = db.rawQuery(query, null);
        /* if count != 0 return false, else true */
        return (c.getCount() != 0);

    }

    public int deleteTimesheetRow(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int ret = db.delete(TABLE_TIMESHEET, COL_ID_LOCAL + "=?", new String[]{Integer.toString(id)});
        db.close();
        return ret;
    }

    public int deleteTimesheetRows(List<TimesheetRow> toDeleteFromLocDb) {

        String singleWhereClause = COL_ID_LOCAL + "=?";
        StringBuilder whereClauseBuilder = new StringBuilder(singleWhereClause);
        for (int i = 1; i < toDeleteFromLocDb.size(); i++)
            whereClauseBuilder.append(" OR " + singleWhereClause);

        String[] whereArg = Stream.of(toDeleteFromLocDb).map(new Function<TimesheetRow, String>() {
            @Override
            public String apply(TimesheetRow q) {
                return q.getIdLocal().toString();
            }
        }).collect(Collectors.toList()).toArray(new String[0]);

        SQLiteDatabase db = getWritableDatabase();
        int ret = db.delete(TABLE_TIMESHEET, whereClauseBuilder.toString(), whereArg);
        db.close();
        return ret;
    }

    public int updateTimesheetRow(TimesheetRow tsr) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_EXTERNAL, tsr.getIdExternal());
        contentValues.put(COL_ID_USER, tsr.getUserId());
        contentValues.put(COL_DATE, tsr.getDate().toString());
        contentValues.put(COL_FROM, tsr.getFrom().toString());
        contentValues.put(COL_TO, tsr.getTo().toString());
        contentValues.put(COL_CUSTOMER_BREAK, tsr.getCustomerBreak().toString());
        contentValues.put(COL_STATUTORY_BREAK, tsr.getStatutoryBreak().toString());
        contentValues.put(COL_COMMENTS, tsr.getComments());
        contentValues.put(COL_ID_PROJECT, tsr.getProjectId());
        contentValues.put(COL_ID_COMPANY, tsr.getCompanyId());
        contentValues.put(COL_STATUS, tsr.getStatus());
        //contentValues.put(COL_CREATED_AT, tsr.getCreatedAt().toString());
        contentValues.put(COL_UPDATED_AT, tsr.getUpdatedAt().toString());
        contentValues.put(COL_PROJECT, tsr.getProject());

        SQLiteDatabase db = getWritableDatabase();
        int ret = db.update(TABLE_TIMESHEET, contentValues, COL_ID_LOCAL + "=?", new String[]{Integer.toString(tsr.getIdLocal())});
        db.close();
        return ret;
    }

    public long updateTimesheetRows(List<TimesheetRow> ltsr) {
        long ret = 0;
        for (TimesheetRow tsr : ltsr) {
            if (updateTimesheetRow(tsr) == 1)
                ret++;
            else
                return -1;
        }
        return ret;
    }

    public List<TimesheetRow> filterTimesheetRows(List<TimesheetRow> ltsr) {
        List<TimesheetRow> filtered = new ArrayList<TimesheetRow>();
        int idExt;
        for (TimesheetRow tsr : ltsr) {
            idExt = tsr.getIdExternal();
            if (!hasTimeSheetRowFromExternalDb(String.valueOf(idExt)) && !hasTimeSheetRowFromExternalDb(String.valueOf(-1 * idExt))) {
                filtered.add(tsr);
            }
        }
        return filtered;
    }
    // endregion**

    // region OBJECTIVES TABLE FUNCTIONS ************************************
    public List<String> readObjectives() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_OBJECTIVES;

        Cursor c = db.rawQuery(query, null);
        List<String> objectives = new ArrayList<String>();

        while (c.moveToNext()) {
            objectives.add(c.getString(c.getColumnIndex(COL_OBJECTIVE)));
        }

        db.close();
        c.close();

        return objectives;
    }

    public long writeObjective(String objective) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_OBJECTIVE, objective);

        SQLiteDatabase db = getWritableDatabase();
        long ret = db.insert(TABLE_OBJECTIVES, null, contentValues);
        db.close();
        return ret;
    }

    public long writeObjectives(List<String> objectives) {
        long ret = 0;
        for (String objective : objectives) {
            if (writeObjective(objective) != -1)
                ret++;
            else
                return -1;
        }
        return ret;
    }

    public long clearObjectives() {
        SQLiteDatabase db = getWritableDatabase();
        long ret = db.delete(TABLE_OBJECTIVES, null, null);
        db.close();
        return ret;
    }

    // endregion

    // region TEAM TABLE FUNCTIONS ******************************************

    public long clearTeam() {
        SQLiteDatabase db = getWritableDatabase();
        long ret = db.delete(TABLE_TEAM, null, null);
        db.close();
        return ret;
    }

    private UserDataShort getUserFromCursor(Cursor c) {
        UserDataShort user = new UserDataShort(
                c.getString(c.getColumnIndex(COL_EMAIL)),
                c.getString(c.getColumnIndex(COL_AVATAR_FILE_NAME)),
                c.getString(c.getColumnIndex(COL_AVATAR_CONTENT_TYPE)),
                c.getInt(c.getColumnIndex(COL_AVATAR_FILE_SIZE)),
                c.getString(c.getColumnIndex(COL_ROLE)),
                c.getInt(c.getColumnIndex(COL_ACTIVE)) == 1,
                c.getString(c.getColumnIndex(COL_FIRST_NAME)),
                c.getString(c.getColumnIndex(COL_LAST_NAME)),
                c.getString(c.getColumnIndex(COL_TITLE)),
                c.getString(c.getColumnIndex(COL_PHONE))
        );
        return user;
    }

    public List<UserDataShort> getTeam() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TEAM;

        Cursor c = db.rawQuery(query, null);
        List<UserDataShort> team = new ArrayList<>();

        while (c.moveToNext()) {
            team.add(getUserFromCursor(c));
        }

        db.close();
        c.close();

        return team;
    }

    private long setTeamMember(UserDataShort user) {
        resetUser();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMAIL, user.getEmail());
        contentValues.put(COL_AVATAR_FILE_NAME, user.getAvatarFileSize());
        contentValues.put(COL_AVATAR_CONTENT_TYPE, user.getAvatarContentType());
        contentValues.put(COL_AVATAR_FILE_SIZE, user.getAvatarFileSize());
        contentValues.put(COL_ROLE, user.getRole());
        contentValues.put(COL_ACTIVE, user.getActive());
        contentValues.put(COL_FIRST_NAME, user.getFirstName());
        contentValues.put(COL_LAST_NAME, user.getLastName());
        contentValues.put(COL_TITLE, user.getTitle());
        contentValues.put(COL_PHONE, user.getPhone());

        SQLiteDatabase db = getWritableDatabase();
        long ret = db.insert(TABLE_TEAM, null, contentValues);
        db.close();
        return ret;
    }

    public long setTeam(List<UserDataShort> team) {
        long ret = 0;
        for (UserDataShort teamMember : team) {
            if (setTeamMember(teamMember) != -1)
                ret++;
            else
                return -1;
        }
        return ret;
    }

    // endregion
}

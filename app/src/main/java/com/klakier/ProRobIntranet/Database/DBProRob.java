package com.klakier.ProRobIntranet.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.klakier.ProRobIntranet.Responses.UserDataShort;

public class DBProRob extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "prorob.db";

    private static final String TABLE_CURRENT_USER = "currentUser";
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

    public DBProRob(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CURRENT_USER + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
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
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_CURRENT_USER + ";";
        db.execSQL(query);
        onCreate(db);
    }

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
}

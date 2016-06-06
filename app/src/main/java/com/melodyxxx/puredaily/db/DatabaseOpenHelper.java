package com.melodyxxx.puredaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.melodyxxx.puredaily.constant.DatabaseConstants;

/**
 * Created by hanjie on 2016/6/6.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper implements DatabaseConstants {

    private static final String DB_NAME = "daily.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_COLLECTIONS_SQL = "" +
            "CREATE TABLE " + TABLE_COLLECTIONS + "(" +
            COLLECTIONS_ID + " TEXT PRIMARY KEY," +
            COLLECTIONS_TITLE + " TEXT," +
            COLLECTIONS_IMG_URL + " TEXT," +
            COLLECTIONS_TIME + " INTEGER)";

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static DatabaseOpenHelper sOpenHelper = null;

    public static DatabaseOpenHelper getInstance(Context context) {
        if (sOpenHelper == null) {
            synchronized (DatabaseOpenHelper.class) {
                if (sOpenHelper == null) {
                    sOpenHelper = new DatabaseOpenHelper(context.getApplicationContext());
                }
            }
        }
        return sOpenHelper;
    }

    public void open() {
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COLLECTIONS_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

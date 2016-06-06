package com.melodyxxx.puredaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.melodyxxx.puredaily.constant.DatabaseConstants;
import com.melodyxxx.puredaily.entity.Collection;

import java.util.ArrayList;

/**
 * CRUD for database
 * <p>
 * Created by hanjie on 2016/6/6.
 */
public class Dao implements DatabaseConstants {

    private SQLiteDatabase mDB;

    private Dao(Context context) {
        mDB = DatabaseOpenHelper.getInstance(context).getWritableDatabase();
    }

    private static Dao sDao = null;

    public static Dao getInstance(Context context) {
        if (sDao == null) {
            synchronized (Dao.class) {
                if (sDao == null) {
                    sDao = new Dao(context);
                }
            }
        }
        return sDao;
    }

    public static void initialize(Context context) {
        getInstance(context);
    }

    public void insertToCollections(Collection collection) {
        mDB.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLLECTIONS_ID, collection.getId());
            cv.put(COLLECTIONS_TITLE, collection.getTitle());
            cv.put(COLLECTIONS_IMG_URL, collection.getImgUrl());
            cv.put(COLLECTIONS_TIME, collection.getTime());
            if (isExistInCollections(collection.getId())) {
                mDB.update(TABLE_COLLECTIONS, cv, COLLECTIONS_ID + " = ?", new String[]{collection.getId()});
            } else {
                mDB.insert(TABLE_COLLECTIONS, null, cv);
            }
            mDB.setTransactionSuccessful();
        } finally {
            mDB.endTransaction();
        }
    }

    public ArrayList<Collection> getAllCollections() {
        ArrayList<Collection> collections = new ArrayList<>();
        Cursor cursor = mDB.query(TABLE_COLLECTIONS, null, null, null, null, null, COLLECTIONS_TIME + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Collection collection = new Collection();
                collection.setId(cursor.getString(cursor.getColumnIndex(COLLECTIONS_ID)));
                collection.setTitle(cursor.getString(cursor.getColumnIndex(COLLECTIONS_TITLE)));
                collection.setImgUrl(cursor.getString(cursor.getColumnIndex(COLLECTIONS_IMG_URL)));
                collection.setTime(cursor.getLong(cursor.getColumnIndex(COLLECTIONS_TIME)));
                collections.add(collection);
            }
            cursor.close();
        }
        return collections;
    }

    public void removeFromCollections(String id) {
        mDB.execSQL("DELETE FROM " + TABLE_COLLECTIONS + " WHERE " + COLLECTIONS_ID + " = ?", new String[]{id});
    }

    public boolean isExistInCollections(String id) {
        boolean isExist = false;
        Cursor cursor = mDB.query(TABLE_COLLECTIONS, null, COLLECTIONS_ID + " = ?", new String[]{id}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }

    public int getCountOfCollections() {
        int count = 0;
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) AS COUNT FROM " + TABLE_COLLECTIONS, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("COUNT"));
            }
            cursor.close();
        }
        return count;
    }


}

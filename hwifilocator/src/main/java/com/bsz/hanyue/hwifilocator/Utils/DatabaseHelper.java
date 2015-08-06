package com.bsz.hanyue.hwifilocator.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hanyue on 2015/7/21.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    final static String DB_Name = "wifianddistance.db";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context,DB_Name,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS wifi"+
        "(_id INTEGER PRIMARY KEY AUTOINCREMENT, bssid VARCHAR, level INTEGER, frequency INTEGER, distance INTEGER, name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS prewifi"+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, bssid VARCHAR, level INTEGER, frequency INTEGER, name VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

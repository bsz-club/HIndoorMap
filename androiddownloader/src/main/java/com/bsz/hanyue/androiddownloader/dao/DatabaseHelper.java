package com.bsz.hanyue.androiddownloader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bsz.hanyue.androiddownloader.DownloadTask;
import com.bsz.hanyue.androidlog.utils.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by hanyue on 2015/8/11
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "hanyue-hmapview-map.db";

    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the table
    private Dao<DownloadTask, Integer> taskDao = null;

    public DatabaseHelper(Context context) {
        //super(context, DATABASE_NAME, null, DATABASE_VERSION, R.assets.ormlite_config);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i("onCreate");
            TableUtils.createTable(connectionSource, DownloadTask.class);
        } catch (SQLException e) {
            Log.e("Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            Log.i("onUpgrade");
            TableUtils.dropTable(connectionSource, DownloadTask.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e("Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our Image class. It will
     * create it or just give the cached value.
     */
    public Dao<DownloadTask, Integer> getTaskDao() throws SQLException {
        if (taskDao == null) {
            taskDao = getDao(DownloadTask.class);
        }
        return taskDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        taskDao = null;
    }
}

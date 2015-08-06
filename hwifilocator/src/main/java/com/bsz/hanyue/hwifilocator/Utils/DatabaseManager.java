package com.bsz.hanyue.hwifilocator.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bsz.hanyue.hwifilocator.Model.Wifi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyue on 2015/7/21.
 */
public class DatabaseManager {

    public final static String SHEET2 = "wifi";
    public final static String SHEET1 = "prewifi";
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void add(List<Wifi> wifis) {
        for (Wifi wifi : wifis) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("bssid", wifi.getBssID());
            contentValues.put("level", wifi.getLevel());
            contentValues.put("frequency", wifi.getFrequency());
            contentValues.put("name", wifi.getName());
            if (wifi.getDistance() == 0) {
                database.insert(SHEET1, null, contentValues);
            } else {
                contentValues.put("distance", wifi.getDistance());
                database.insert(SHEET2, null, contentValues);
            }
        }
    }

    public void add(Wifi wifi) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("bssid", wifi.getBssID());
        contentValues.put("level", wifi.getLevel());
        contentValues.put("frequency", wifi.getFrequency());
        contentValues.put("name", wifi.getName());
        if (wifi.getDistance() == 0) {
            database.insert(SHEET1, null, contentValues);
        } else {
            contentValues.put("distance", wifi.getDistance());
            database.insert(SHEET2, null, contentValues);
        }
    }

    public void clearPreWifi() {
        database.delete(SHEET1, null, null);
    }

    public List<String> getAllPreBSSID() {
        List<String> bssIDs = new ArrayList<>();
        String queryString = "SELECT bssid FROM prewifi";
        Cursor c = database.rawQuery(queryString, null);
        while (c.moveToNext()) {
            String bssID = c.getString(c.getColumnIndex("bssid"));
            bssIDs.add(bssID);
        }
        c.close();
        return bssIDs;
    }

    public List<Wifi> getPreWifisByBSSID(String bssID) {
        ArrayList<Wifi> wifis = new ArrayList<>();
        String[] attributes = new String[]{bssID};
        String queryString = "SELECT * FROM prewifi WHERE bssid=?";
        Cursor c = database.rawQuery(queryString, attributes);
        while (c.moveToNext()) {
            Wifi wifi = new Wifi();
            wifi.set_id(c.getInt(c.getColumnIndex("_id")));
            wifi.setBssID(c.getString(c.getColumnIndex("bssid")));
            wifi.setLevel(c.getInt(c.getColumnIndex("level")));
            wifi.setFrequency(c.getInt(c.getColumnIndex("frequency")));
            wifi.setName(c.getString(c.getColumnIndex("name")));
            wifis.add(wifi);
        }
        c.close();
        return wifis;
    }

    public List<Wifi> getWifisByBSSID(String bssID) {
        ArrayList<Wifi> wifis = new ArrayList<>();
        String[] attributes = new String[]{bssID};
        String queryString = "SELECT * FROM prewifi WHERE bssid=?";
        Cursor c = database.rawQuery(queryString, attributes);
        while (c.moveToNext()) {
            Wifi wifi = new Wifi();
            wifi.set_id(c.getInt(c.getColumnIndex("_id")));
            wifi.setBssID(c.getString(c.getColumnIndex("bssid")));
            wifi.setLevel(c.getInt(c.getColumnIndex("level")));
            wifi.setFrequency(c.getInt(c.getColumnIndex("frequency")));
            wifi.setName(c.getString(c.getColumnIndex("name")));
            wifis.add(wifi);
        }
        c.close();
        return wifis;
    }

    public List<Wifi> getWifisByBSSIDAndLevel(Wifi oWifi) {
        ArrayList<Wifi> wifis = new ArrayList<>();
        String queryString = null;
        String[] attributes = new String[]{oWifi.getBssID(), String.valueOf(oWifi.getLevel())};
        queryString = "SELECT * FROM wifi WHERE bssid=? AND level>? ORDER BY level DESC";
        Cursor c = database.rawQuery(queryString, attributes);
        while (c.moveToNext()) {
            Wifi wifi = new Wifi();
            wifi.set_id(c.getInt(c.getColumnIndex("_id")));
            wifi.setBssID(c.getString(c.getColumnIndex("bssid")));
            wifi.setLevel(c.getInt(c.getColumnIndex("level")));
            wifi.setFrequency(c.getInt(c.getColumnIndex("frequency")));
            wifi.setDistance(c.getInt(c.getColumnIndex("distance")));
            wifi.setName(c.getString(c.getColumnIndex("name")));
            wifis.add(wifi);
        }
        c.close();
        return wifis;
    }

    public List<Wifi> getWifisByBSSIDAndLevel(List<Wifi> oWifis) {
        ArrayList<Wifi> wifis = new ArrayList<>();
        for (Wifi oWifi:oWifis) {
            String[] attributes = new String[]{oWifi.getBssID(), String.valueOf(oWifi.getLevel())};
            String queryString = "SELECT * FROM wifi WHERE bssid=? AND level>? ORDER BY level DESC";
            Cursor c = database.rawQuery(queryString, attributes);
            while (c.moveToNext()) {
                Wifi wifi = new Wifi();
                wifi.set_id(c.getInt(c.getColumnIndex("_id")));
                wifi.setBssID(c.getString(c.getColumnIndex("bssid")));
                wifi.setLevel(c.getInt(c.getColumnIndex("level")));
                wifi.setFrequency(c.getInt(c.getColumnIndex("frequency")));
                wifi.setDistance(c.getInt(c.getColumnIndex("distance")));
                wifi.setName(c.getString(c.getColumnIndex("name")));
                wifis.add(wifi);
            }
            c.close();
        }
        return wifis;
    }

    public void closeDatabase() {
        database.close();
    }
}

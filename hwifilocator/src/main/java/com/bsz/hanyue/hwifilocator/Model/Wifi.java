package com.bsz.hanyue.hwifilocator.Model;

import android.net.wifi.ScanResult;

/**
 * Created by hanyue on 2015/7/21.
 */
public class Wifi {

    public Wifi() {
    }

    public Wifi(ScanResult scanResult) {
        this.bssID = scanResult.BSSID;
        this.level = scanResult.level;
        this.frequency = scanResult.frequency;
    }

    int _id;
    String name;
    String bssID;
    int level;
    int frequency;
    int distance;
    int floor;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBssID() {
        return bssID;
    }

    public void setBssID(String bssID) {
        this.bssID = bssID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Wifi{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", bssID='" + bssID + '\'' +
                ", level=" + level +
                ", frequency=" + frequency +
                ", distance=" + distance +
                ", floor=" + floor +
                '}';
    }
}

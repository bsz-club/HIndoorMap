package com.bsz.hanyue.hlocatormodel.Model;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by hanyue on 2015/8/10.
 */
public class Map {

    long id;
    int floor;
    String mapimageurl;
    Bitmap mapimage;
    float ruler;//m per px
    List<Icon> iconList;
    Coordinate pointO;

    public static Map parseFromJsonArr(JSONObject jsonObject){
        new Map();
        return new Gson().fromJson(jsonObject.toString(),Map.class);
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<Icon> getIconList() {
        return iconList;
    }

    public void setIconList(List<Icon> iconList) {
        this.iconList = iconList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getMapimage() {
        return mapimage;
    }

    public void setMapimage(Bitmap mapimage) {
        this.mapimage = mapimage;
    }

    public String getMapimageurl() {
        return mapimageurl;
    }

    public void setMapimageurl(String mapimageurl) {
        this.mapimageurl = mapimageurl;
    }

    public float getRuler() {
        return ruler;
    }

    public void setRuler(float ruler) {
        this.ruler = ruler;
    }

    public Coordinate getPointO() {
        return pointO;
    }

    public void setPointO(Coordinate pointO) {
        this.pointO = pointO;
    }

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", floor=" + floor +
                ", mapimageurl='" + mapimageurl + '\'' +
                ", mapimage=" + mapimage +
                ", ruler=" + ruler +
                ", iconList=" + iconList +
                ", pointO=" + pointO +
                '}';
    }

}

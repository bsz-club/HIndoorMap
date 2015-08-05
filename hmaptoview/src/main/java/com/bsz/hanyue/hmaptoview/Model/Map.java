package com.bsz.hanyue.hmaptoview.Model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by hanyue on 2015/7/28.
 */
public class Map {

    long id;
    int floor;
    String mapimageurl;
    Bitmap mapimage;
    float ruler;//m per px
    List<Icon> iconList;

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

    @Override
    public String toString() {
        return "Map{" +
                "floor=" + floor +
                ", id=" + id +
                ", mapimageurl='" + mapimageurl + '\'' +
                ", mapimage=" + mapimage +
                ", ruler=" + ruler +
                ", iconList=" + iconList +
                '}';
    }
}

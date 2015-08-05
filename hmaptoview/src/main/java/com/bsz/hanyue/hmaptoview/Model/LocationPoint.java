package com.bsz.hanyue.hmaptoview.Model;

/**
 * Created by hanyue on 2015/7/28.
 */
public class LocationPoint {

    float x;
    float y;
    float roate;

    public float getRoate() {
        return roate;
    }

    public void setRoate(float roate) {
        this.roate = roate;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LocationPoint{" +
                "roate=" + roate +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}

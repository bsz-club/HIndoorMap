package com.bsz.hanyue.hlocatormodel.Model;

/**
 * Created by hanyue on 2015/8/10.
 */
public class Coordinate {

    float x;
    float y;

    public Coordinate() {
    }

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
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
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}

package com.bsz.hanyue.hmaptoview.Model;

import android.graphics.Bitmap;

/**
 * Created by hanyue on 2015/7/28.
 */
public class Icon {

    long id;
    float x;
    float y;
    float width;
    float height;
    String iconimageurl;
    Bitmap iconimage;
    long storeId;

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Bitmap getIconimage() {
        return iconimage;
    }

    public void setIconimage(Bitmap iconimage) {
        this.iconimage = iconimage;
    }

    public String getIconimageurl() {
        return iconimageurl;
    }

    public void setIconimageurl(String iconimageurl) {
        this.iconimageurl = iconimageurl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
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
        return "Icon{" +
                "height=" + height +
                ", id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", iconimageurl='" + iconimageurl + '\'' +
                ", iconimage=" + iconimage +
                ", storeId=" + storeId +
                '}';
    }
}

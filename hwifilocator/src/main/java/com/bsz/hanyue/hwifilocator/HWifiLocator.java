package com.bsz.hanyue.hwifilocator;

import android.app.Activity;

import com.bsz.hanyue.hwifilocator.Utils.LocatorRunController;

/**
 * Created by hanyue on 2015/8/10
 */
public class HWifiLocator {

    private static LocatorRunController locatorRunController;

    public static LocatorRunController with(Activity activity) {
        if (locatorRunController == null)
        {
            locatorRunController = new LocatorRunController(activity);
        }
        return locatorRunController;
    }

}

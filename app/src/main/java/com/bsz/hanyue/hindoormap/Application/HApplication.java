package com.bsz.hanyue.hindoormap.Application;

import android.app.Application;
import android.net.wifi.WifiManager;

import com.bsz.hanyue.hwifilocator.Utils.WifiScanManager;

/**
 * Created by hanyue on 2015/8/7.
 */
public class HApplication extends Application {

    private WifiScanManager wifiScanManager;

    public WifiScanManager getWifiScanManager() {
        return wifiScanManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiScanManager = new WifiScanManager(getApplicationContext());
    }



}

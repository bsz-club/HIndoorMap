package com.bsz.hanyue.hwifilocator.Utils;

import android.app.Activity;

import com.bsz.hanyue.hlocatormodel.Model.Wifi;
import com.bsz.hanyue.hwifilocator.Interface.OnGotWifiResultListener;

import java.util.List;

/**
 * Created by hanyue on 2015/8/10
 */
public class LocatorRunController {

    private static WifiScanManager wifiScanManager;
    private static DatabaseManager databaseManager;
    private static ScanLimitModelManager scanLimitModelManager;

    public LocatorRunController(Activity activity){
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(activity);
        }
        if (wifiScanManager == null) {
            wifiScanManager = new WifiScanManager(activity);
            wifiScanManager.setOnGotWifiResultListener(new OnGotWifiResultListener() {
                @Override
                public void getScanResult(List<Wifi> wifis) {
                    databaseManager.add(wifis);
                }
            });
        }
    }

    public ScanLimitModelManager 强制扫描() {
        wifiScanManager.强制扫描();
        if (scanLimitModelManager == null) {
            scanLimitModelManager = new ScanLimitModelManager(wifiScanManager, databaseManager);
        }
        return scanLimitModelManager;
    }

    public ScanLimitModelManager 自循环扫描() {
        wifiScanManager.自循环扫描();
        if (scanLimitModelManager == null) {
            scanLimitModelManager = new ScanLimitModelManager(wifiScanManager, databaseManager);
        }
        return scanLimitModelManager;
    }

}

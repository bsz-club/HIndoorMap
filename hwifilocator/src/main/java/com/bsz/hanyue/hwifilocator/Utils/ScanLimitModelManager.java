package com.bsz.hanyue.hwifilocator.Utils;

import android.app.Activity;
import android.widget.Toast;

import com.bsz.hanyue.hwifilocator.Interface.OnGetWifiResultListener;
import com.bsz.hanyue.hwifilocator.Interface.OnLimitListener;
import com.bsz.hanyue.hwifilocator.Model.Wifi;

import java.util.Date;
import java.util.List;

/**
 * Created by hanyue on 2015/7/24.
 */
public class ScanLimitModelManager {

    private Activity activity;
    private WifiScanManager wifiScanManager;
    private DatabaseManager databaseManager;
    private OnLimitListener onLimitListener;
    private OnGetWifiResultListener onGetWifiResultListener;
    private Wifi wifi;
    private long time;
    private int count;

    public ScanLimitModelManager(Activity activity, WifiScanManager wifiScanManager, DatabaseManager databaseManager) {
        this.activity = activity;
        this.wifiScanManager = wifiScanManager;
        this.databaseManager = databaseManager;
        timesLimit();
    }

    public void timesLimit() {
        Toast.makeText(activity, "正在记录扫描次数", Toast.LENGTH_SHORT).show();
        initState();
        wifiScanManager.setOnGetWifiResultListener(onGetWifiResultListener = new OnGetWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                count++;
                if (count > 5) {
                    onLimitListener.onLimit();
                }
            }
        });
    }

    public void 距离最近的WIFI的储存次数为限() {
        Toast.makeText(activity, "正在记录WIFI储存次数", Toast.LENGTH_SHORT).show();
        initState();
        wifiScanManager.setOnGetWifiResultListener(onGetWifiResultListener = new OnGetWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                if (wifis.size() != 0 && count == 0) {
                    wifi = wifis.get(0);
                    count++;
                }
                if (databaseManager.getPreWifisByBSSID(wifi.getBssID()).size()>5){
                    onLimitListener.onLimit();
                }
            }
        });
    }

    public void 时间为限() {
        Toast.makeText(activity, "正在记录时间次数", Toast.LENGTH_SHORT).show();
        initState();
        wifiScanManager.setOnGetWifiResultListener(onGetWifiResultListener = new OnGetWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                if (count==0){
                    time = new Date().getTime();
                }
                if (((new Date().getTime()-time)/1000)>10){
                    onLimitListener.onLimit();
                }
            }
        });
    }

    private void initState() {
        if (onGetWifiResultListener != null) {
            wifiScanManager.removeListener(onGetWifiResultListener);
            onGetWifiResultListener = null;
        }
        count = 0;
        time = 0;
        wifi = null;
    }

    public void setOnLimitListener(OnLimitListener onLimitListener) {
        this.onLimitListener = onLimitListener;
    }

}

package com.bsz.hanyue.hwifilocator.Utils;

import com.bsz.hanyue.hlocatormodel.Model.Wifi;
import com.bsz.hanyue.hwifilocator.Interface.OnGotCalculateResultListener;
import com.bsz.hanyue.hwifilocator.Interface.OnGotWifiResultListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hanyue on 2015/7/24
 */
public class ScanLimitModelManager {

    private WifiScanManager wifiScanManager;
    private DatabaseManager databaseManager;
    private ResultCalculator resultCalculator;

    private OnGotWifiResultListener onGotWifiResultListener;
    private Wifi wifi;
    private long time;
    private int count;

    public ScanLimitModelManager(WifiScanManager wifiScanManager, DatabaseManager databaseManager) {
        this.wifiScanManager = wifiScanManager;
        this.databaseManager = databaseManager;
        timesLimit();
    }

    public ResultCalculator timesLimit() {
        if (resultCalculator == null){
            resultCalculator = new ResultCalculator();
        }
        initState();
        removeOldListener();
        wifiScanManager.setOnGotWifiResultListener(onGotWifiResultListener = new OnGotWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                count++;
                if (count > 5) {
                    resultCalculator.Calculate(databaseManager);
                }
            }
        });
        return resultCalculator;
    }

    public ResultCalculator 距离最近的WIFI的储存次数为限() {
        if (resultCalculator == null){
            resultCalculator = new ResultCalculator();
        }
        initState();
        removeOldListener();
        wifiScanManager.setOnGotWifiResultListener(onGotWifiResultListener = new OnGotWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                if (wifis.size() != 0 && count == 0) {
                    wifi = wifis.get(0);
                    count++;
                }
                if (databaseManager.getPreWifisByBSSID(wifi.getBssID()).size() > 5) {
                    resultCalculator.Calculate(databaseManager);
                }
            }
        });
        return resultCalculator;
    }

    public ResultCalculator 时间为限() {
        if (resultCalculator == null){
            resultCalculator = new ResultCalculator();
        }
        initState();
        removeOldListener();
        wifiScanManager.setOnGotWifiResultListener(onGotWifiResultListener = new OnGotWifiResultListener() {
            @Override
            public void getScanResult(List<Wifi> wifis) {
                if (count == 0) {
                    time = new Date().getTime();
                }
                if (((new Date().getTime() - time) / 1000) > 10) {
                    resultCalculator.Calculate(databaseManager);
                }
            }
        });
        return resultCalculator;
    }

    private void removeOldListener() {
        if (onGotWifiResultListener != null) {
            wifiScanManager.removeListener(onGotWifiResultListener);
        }
    }

    private void initState() {
        if (onGotWifiResultListener != null) {
            wifiScanManager.removeListener(onGotWifiResultListener);
            onGotWifiResultListener = null;
        }
        count = 0;
        time = 0;
        wifi = null;
    }

    public class ResultCalculator {

        private OnGotCalculateResultListener onGotCalculateResultListener;

        private void Calculate(DatabaseManager databaseManager){
            List<Wifi> properWifis = getProperValue(databaseManager);
            databaseManager.clearPreWifi();
            if (onGotCalculateResultListener != null){
                onGotCalculateResultListener.getWifiEnvironment(properWifis);
            }
        }

        //得到临时数据库内wifi的特征值
        private List<Wifi> getProperValue(DatabaseManager databaseManager){
            return getAverageWifis(databaseManager);
        }

        private List<Wifi> getAverageWifis(DatabaseManager databaseManager) {
            List<Wifi> wifis = new ArrayList<>();
            List<String> bssIDs = getWifiInDatabase(databaseManager);
            for (int i = 0; i < bssIDs.size(); i++) {
                Wifi wifi = getAverage(databaseManager.getPreWifisByBSSID(bssIDs.get(i)));
                wifis.add(wifi);
            }
            return wifis;
        }

        private List<String> getWifiInDatabase(DatabaseManager databaseManager) {
            List<String> allBssids = databaseManager.getAllPreBSSID();
            List<String> bssids = new ArrayList<>();
            for (int i = 0; i < allBssids.size(); i++) {
                boolean flag = true;
                for (int j = 0; j < bssids.size(); j++) {
                    if (bssids.get(j).equals(allBssids.get(i))) {
                        flag = false;
                    }
                }
                if (flag) {
                    bssids.add(allBssids.get(i));
                }
            }
            return bssids;
        }

        private Wifi getAverage(List<Wifi> wifis) {
            int sub = 0;
            for (int i = 0; i < wifis.size(); i++) {
                sub += wifis.get(i).getLevel();
            }
            int averageLevel = sub / wifis.size();
            Wifi wifi = wifis.get(0);
            wifi.setLevel(averageLevel);
            return wifi;
        }

        public void setOnGotCalculateResultListener(OnGotCalculateResultListener onGotCalculateResultListener){
            this.onGotCalculateResultListener = onGotCalculateResultListener;
        }

    }

}

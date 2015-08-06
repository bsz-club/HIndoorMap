package com.bsz.hanyue.hwifilocator.Utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.bsz.hanyue.hwifilocator.Interface.OnGetWifiResultListener;
import com.bsz.hanyue.hwifilocator.Model.Wifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanyue on 2015/7/23.
 */
public class WifiScanManager {

    private Activity activity;
    private WifiManager wifiManager;
    private List<OnGetWifiResultListener> getWifiResultListeners;
    private int state = 2;

    public WifiScanManager(Context context) {
        this.activity = (Activity) context;
        wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        getWifiResultListeners = new ArrayList<>();
    }

    private Timer timer;

    public void 强制扫描() {
        Toast.makeText(activity,"正在强制扫描",Toast.LENGTH_SHORT).show();
        if (state == 1){
            unRegisterWifiScanReceiver(broadcastReceiver);
        }
        state = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wifiManager.startScan();
                try {
                    Thread.sleep((long) (1000 * 0.5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<ScanResult> scanResultList = wifiManager.getScanResults();
                        deleteLowerWifi(scanResultList);
                        sortResult(scanResultList);
                        for (OnGetWifiResultListener getWifiResultListener : getWifiResultListeners) {
                            getWifiResultListener.getScanResult(changeType(scanResultList));
                        }
                    }
                });
            }
        }, 1000 * 1, 1000 * 1);
    }

    private BroadcastReceiver broadcastReceiver;

    public void 自循环扫描() {
        Toast.makeText(activity,"正在自循环扫描",Toast.LENGTH_SHORT).show();
        if (state == 0){
            timer.cancel();
        }
        state = 1;
        wifiManager.startScan();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<ScanResult> scanResultList = wifiManager.getScanResults();
                deleteLowerWifi(scanResultList);
                sortResult(scanResultList);
                for (OnGetWifiResultListener getWifiResultListener:getWifiResultListeners){
                    getWifiResultListener.getScanResult(changeType(scanResultList));
                }
                wifiManager.startScan();
            }
        };
        registerWifiScanReceiver(broadcastReceiver);
    }

    public String getScanState() {
        switch (state) {
            case 0:
                return "强制扫描";
            case 1:
                return "自循环扫描";
            default:
                return "无扫描状态";
        }
    }

    public void stop() {
        switch (state) {
            case 0:
                timer.cancel();
                break;
            case 1:
                unRegisterWifiScanReceiver(broadcastReceiver);
                break;
            default:
                Log.e("WifiScanManager","NoThing to stop");
                break;
        }
    }

    public void reStart(){
        switch (state){
            case 0:
                强制扫描();
                break;
            case 1:
                自循环扫描();
                break;
            default:
                Log.e("WifiScanManager","NoThing to reStart");
                break;
        }
    }

    private void deleteLowerWifi(List<ScanResult> scanResultList) {
        for (int i = 0; i < scanResultList.size(); i++) {
            if (scanResultList.get(i).level <= -90) {
                scanResultList.remove(i);
                i--;
            }
        }
    }

    private void sortResult(List<ScanResult> scanResultList) {
        ScanResult result;
        for (int i = 0; i < scanResultList.size(); i++) {
            for (int j = 0; j < scanResultList.size() - 1 - i; j++) {
                if (scanResultList.get(j).level < scanResultList.get(j + 1).level) {
                    result = scanResultList.get(j);
                    scanResultList.set(j, scanResultList.get(j + 1));
                    scanResultList.set(j + 1, result);
                }
            }
        }
    }

    private List<Wifi> changeType(List<ScanResult> scanResults){
        List<Wifi> wifis = new ArrayList<>();
        for (int i = 0;i<scanResults.size();i++){
            Wifi wifi = new Wifi();
            wifi.setBssID(scanResults.get(i).BSSID);
            wifi.setLevel(scanResults.get(i).level);
            wifi.setFrequency(scanResults.get(i).frequency);
            wifi.setName(scanResults.get(i).SSID);
            wifis.add(wifi);
        }
        return wifis;
    }

    private void registerWifiScanReceiver(BroadcastReceiver broadcastReceiver) {
        activity.registerReceiver(broadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private void unRegisterWifiScanReceiver(BroadcastReceiver broadcastReceiver) {
        activity.unregisterReceiver(broadcastReceiver);
    }

    public void setOnGetWifiResultListener(OnGetWifiResultListener getWifiResultListener) {
        this.getWifiResultListeners.add(getWifiResultListener);
    }

    public void removeListener(OnGetWifiResultListener getWifiResultListener){
        this.getWifiResultListeners.remove(getWifiResultListener);
    }

}

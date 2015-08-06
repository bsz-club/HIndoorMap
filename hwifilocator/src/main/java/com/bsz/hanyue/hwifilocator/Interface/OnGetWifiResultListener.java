package com.bsz.hanyue.hwifilocator.Interface;

import com.bsz.hanyue.hwifilocator.Model.Wifi;

import java.util.List;

/**
 * Created by hanyue on 2015/7/23.
 */
public interface OnGetWifiResultListener {

    public abstract void getScanResult(List<Wifi> wifis);

}

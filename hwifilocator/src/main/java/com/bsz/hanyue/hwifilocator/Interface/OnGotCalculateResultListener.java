package com.bsz.hanyue.hwifilocator.Interface;

import com.bsz.hanyue.hlocatormodel.Model.Map;
import com.bsz.hanyue.hlocatormodel.Model.Wifi;

import java.util.List;

/**
 * Created by hanyue on 2015/8/11
 */
public interface OnGotCalculateResultListener {

    public abstract void getMap(Map map);

    public abstract void getWifiEnvironment(List<Wifi> wifis);

}

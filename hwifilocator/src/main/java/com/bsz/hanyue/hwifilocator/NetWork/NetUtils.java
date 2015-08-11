package com.bsz.hanyue.hwifilocator.NetWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hanyue on 2015/8/11
 */
public class NetUtils {

    public static final String SEND_WIFI_ENVIRONMENT = "/checkwifienvironment";

    public static final String URL = "http://127.0.0.1/hlocator";

    public static String buildUrl(String address){
        return URL+address;
    }

    public static boolean isConnect(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean is = false;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnected()) {
            is = true;
        }
        return is;
    }

    public static void post(final Context context,String url,HashMap content, final RequestRawDataCallBack rawDataCallBack){
        if (!isConnect(context)){
            Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        new AQuery(context).ajax(url,content,JSONObject.class,new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                try {
                    if (object.getString("status").equals("success")){
                        if (rawDataCallBack == null){
                            return;
                        }
                        Object localObject = object.get("data");
                        if(localObject == null || (localObject).equals("null")){
                            rawDataCallBack.OnFailed(object.getString("mesg"));
                            return;
                        }
                        rawDataCallBack.OnSuccess(localObject);
                    }
                } catch (JSONException e) {
                    Log.e("HLocator", "JSON解析错误");
                    if(rawDataCallBack != null) {
                        rawDataCallBack.onJsonErr();
                    }
                    e.printStackTrace();
                }catch (Exception e){
                    rawDataCallBack.onUnknownErr();
                }
            }
        });
    }

}

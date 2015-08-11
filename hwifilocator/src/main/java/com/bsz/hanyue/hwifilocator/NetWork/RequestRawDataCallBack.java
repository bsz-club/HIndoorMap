package com.bsz.hanyue.hwifilocator.NetWork;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

/**
 * Created by hanyue on 2015/8/11.
 */
public abstract class RequestRawDataCallBack {

    private Context context;
    protected ProgressDialog waitDialog;

    public RequestRawDataCallBack(Context context)
    {
        this.context = context;
    }

    public void OnFailed(String string)
    {
        if (this.waitDialog != null) {
            this.waitDialog.dismiss();
        }
        Toast.makeText(this.context, string, Toast.LENGTH_SHORT).show();
    }

    public void OnSuccess(Object jasonObject)
            throws JsonSyntaxException, JSONException
    {
        if (this.waitDialog != null)
            this.waitDialog.dismiss();
    }

    public void onJsonErr()
    {
        if (this.waitDialog != null)
            this.waitDialog.dismiss();
        Toast.makeText(this.context, "JSON解析错误", Toast.LENGTH_SHORT).show();
    }

    public void onUnknownErr()
    {
        if (this.waitDialog != null)
            this.waitDialog.dismiss();
        Toast.makeText(this.context, "未知异常", Toast.LENGTH_SHORT).show();
    }

    public RequestRawDataCallBack showDialog(String message)
    {
        waitDialog = new ProgressDialog(context);
        waitDialog.setMessage(message);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.show();
        return this;
    }

}

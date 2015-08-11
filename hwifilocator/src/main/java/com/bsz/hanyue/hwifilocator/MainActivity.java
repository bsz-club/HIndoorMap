package com.bsz.hanyue.hwifilocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.bsz.hanyue.hlocatormodel.Model.Map;
import com.bsz.hanyue.hlocatormodel.Model.Wifi;
import com.bsz.hanyue.hwifilocator.Interface.OnGotCalculateResultListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HWifiLocator.with(this).强制扫描().时间为限().setOnGotCalculateResultListener(new OnGotCalculateResultListener() {
            @Override
            public void getMap(Map map) {

            }

            @Override
            public void getWifiEnvironment(List<Wifi> wifis) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

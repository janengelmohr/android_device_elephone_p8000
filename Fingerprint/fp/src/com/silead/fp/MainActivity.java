package com.silead.fp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String DATA_KEY = "data_preference";
    private final Intent intent = new Intent();

    private static final String APPS_CALIBRATION = "www.sileadinc.com";
    private static final String APPS_CALIBRATION_ACTIVITY = "www.sileadinc.com.Silead_fpActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG," onCreate ");
        intent.setAction("com.silead.fp.lockscreen.fpservice.ACTION");

        Button bt = (Button) findViewById(R.id.btcalibration);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent_manger = new Intent();
                intent_manger.setClassName(APPS_CALIBRATION, APPS_CALIBRATION_ACTIVITY);
                startActivity(intent_manger);
                //stopService(intent);

            }
        });

        Button setting_bt = (Button) findViewById(R.id.btsetting);
        setting_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(
                        "com.silead.fp.setting.Settings.ACTION");
                // intent.setAction("com.silead.fp.lockscreen.fpservice.ACTION");
                // startService(intent);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

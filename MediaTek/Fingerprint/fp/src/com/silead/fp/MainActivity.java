package com.silead.fp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent("com.silead.fp.setting.Settings.ACTION");
        startActivity(intent);
        finish();
    }
}
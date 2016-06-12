package com.silead.fp.receiver;

import com.silead.fp.lockscreen.FpService;
import com.silead.fp.setting.Settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        String action = arg1.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intent = new Intent(arg0, FpService.class);
            arg0.startService(intent);
        }
    }
}


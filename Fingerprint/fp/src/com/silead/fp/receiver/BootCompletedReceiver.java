package com.silead.fp.receiver;

import com.silead.fp.lockscreen.FpService;
import com.silead.fp.setting.Settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompletedReceiver";

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String action = arg1.getAction();
		//Log.d(TAG,"$$$$$$ onReceive action = "+action);
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)){
			Intent intent = new Intent(arg0, FpService.class);  
			arg0.startService(intent);
		} else if (action.equals(Settings.ACTION_NONSUPPORT_LOCKSCREEN_ACTIVE)) {
		    //Log.d(TAG,"$$$$$$ onReceive ACTION_NONSUPPORT_LOCKSCREEN_ACTIVE ");
            Settings.DisableFp();
		}
	}
}


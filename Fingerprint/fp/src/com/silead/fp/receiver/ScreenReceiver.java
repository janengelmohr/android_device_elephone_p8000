package com.silead.fp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

	private static final String TAG = "ScreenReceiver";

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		//String action = arg1.getAction();
		//Log.d(TAG,"$$$$$$ onReceive action = "+action);
		/*if (action.equals(Intent.ACTION_SCREEN_OFF)){
			Log.d(TAG, "#######onReceive 2 ");
		} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
			Log.d(TAG,"$$$$$$$ onReceive ACTION_SCREEN_ON");
		}*/
	}
}


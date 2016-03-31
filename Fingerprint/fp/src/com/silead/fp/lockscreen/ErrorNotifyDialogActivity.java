package com.silead.fp.lockscreen;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class ErrorNotifyDialogActivity extends Activity {
    private static final String TAG = "ErrorNotifyDialogActivity";
    private static final String DATA_KEY = "data_preference";
    private final Intent intent = new Intent();
	private	AlertDialog mAlertDialog;

    public static final String ERROR_NOTIFY_DIALOG_FINISH_ACTION = "com.silead.fp.lockscreen.action.finish";
    private static final String APPS_MANAGER_CATEGORY = "com.silead.fp.category.appsmanager";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().addFlags(
			WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER
			|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);		
        setContentView(R.layout.error_notify_dialog_activity);
        //Log.d(TAG," onCreate ");
		//int errorCount = 0;
		String msg = "";
		Intent intent = getIntent();
		if (intent != null) {
			//errorCount = intent.getIntExtra("error_count", 0);
			msg = intent.getStringExtra("error_msg");
		}

		
        mAlertDialog = new AlertDialog.Builder(this).create();  
        mAlertDialog.setTitle("identify failed !!");  
        mAlertDialog.setMessage(msg);
		mAlertDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				//Log.d(TAG,"alertDialog onDismiss ");
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ERROR_NOTIFY_DIALOG_FINISH_ACTION);
				sendBroadcast(broadCastIntent);
				finish();
			}
		});
        mAlertDialog.show();
 
        Handler handler = new Handler();  
        handler.postDelayed(new Runnable() {  
 
            public void run() {  
				//Log.d(TAG,"postDelayed alertDialog dismiss ");
                mAlertDialog.dismiss();  
            }  
        }, 3000);  		
		/*
        Button bt = (Button) findViewById(R.id.btappmanager);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent_manger = new Intent(APPS_MANAGER_ACTION);
                intent_manger.addCategory(APPS_MANAGER_CATEGORY);
                startActivity(intent_manger);
                stopService(intent);

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
		*/
    }
	
	@Override
	public void onPause() {
        //Log.d(TAG," onPause ");
		super.onPause();
	}    
    
	@Override
	public void onDestroy() {
        //Log.d(TAG," onDestroy 222 ");
		super.onDestroy();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		//Log.d(TAG," onRetainNonConfigurationInstance ");
		return null;

	}	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
		//Log.d(TAG," onConfigurationChanged ");	 
	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			//Log.d(TAG," onConfigurationChanged ORIENTATION_LANDSCAPE");	 
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			//Log.d(TAG," onConfigurationChanged ORIENTATION_PORTRAIT");	 
	    }
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
